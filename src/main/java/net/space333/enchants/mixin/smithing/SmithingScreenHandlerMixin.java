package net.space333.enchants.mixin.smithing;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.space333.enchants.util.Upgrade;
import net.space333.enchants.util.ModTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin extends ForgingScreenHandler {
    public SmithingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ForgingSlotsManager forgingSlotsManager) {
        super(type, syncId, playerInventory, context, forgingSlotsManager);
    }

    @Shadow protected abstract SmithingRecipeInput createRecipeInput();

    @Inject(method = "updateResult", at = @At(value = "TAIL"))
    private void addUpgrade(CallbackInfo ci) {
        SmithingScreenHandler self = (SmithingScreenHandler) (Object) this;
        SmithingRecipeInput smithingRecipeInput = createRecipeInput();

        ItemStack itemStack1 = smithingRecipeInput.getStackInSlot(0);
        ItemStack itemStack2 = smithingRecipeInput.getStackInSlot(1);
        ItemStack itemStack3 = smithingRecipeInput.getStackInSlot(2);
        ItemStack outputStack = itemStack2.copy();
        boolean changed = false;

        if(itemStack1.isEmpty() && itemStack2.isDamageable() && itemStack3.isIn(ModTags.Items.UPGRADE_MATERIAL)) {
            changed = Upgrade.changeAttribute(itemStack2, itemStack3, outputStack);
        }

        if(itemStack1.isEmpty() && itemStack2.isDamageable() && itemStack2.canRepairWith(itemStack3)) {
            /*
            int k = Math.min(outputStack.getDamage(), outputStack.getMaxDamage() / 3);
            if (k < 0) {
                this.output.setStack(0, ItemStack.EMPTY);
                return;
            }

            int n = outputStack.getDamage() - k;
            outputStack.setDamage(n);
            */
            Upgrade.incrementUnbreaking(outputStack);
            changed = true;
        }

        if(changed) {
            this.output.setStack(0,outputStack);
        }
    }

}
