package net.space333.enchants.mixin.upgrades;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.space333.enchants.Component.ModDataComponentType;
import net.space333.enchants.util.Upgrade;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract void damage(int amount, LivingEntity entity, EquipmentSlot slot);

    @Shadow public abstract ItemStack damage(int amount, ItemConvertible itemAfterBreaking, LivingEntity entity, EquipmentSlot slot);

    @ModifyArg(method = "onDurabilityChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setDamage(I)V"), index = 0)
    private int applyUnbreaking(int damage) {
        ItemStack self = ((ItemStack)(Object)this);
        int level = Upgrade.getUnbreaking(self);
        Random rand = new Random();
        float chance_unbreaking = ((float) level*level)/((float) (level*level + 1));

        if(chance_unbreaking > rand.nextFloat()) {
            damage--;
        }
        return damage;
    }

    @Inject(method = "canRepairWith", at = @At(value = "HEAD"), cancellable = true)
    private void addRepairIngredient(ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        ItemStack self = ((ItemStack) (Object) this);
        if(ingredient.getItem() == Items.STRING) {
            if(self.getItem() == Items.BOW || self.getItem() == Items.CROSSBOW || self.getItem() == Items.FISHING_ROD) {
                cir.setReturnValue(true);
            }
        }
        else if(ingredient.getItem() == Items.PRISMARINE_SHARD) {
            if(self.getItem() == Items.TRIDENT) {
                cir.setReturnValue(true);
            }
        }
        else if(ingredient.getItem() == Items.BLACKSTONE) {
            if(self.getItem() == Items.MACE) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "appendTooltip", at = @At(value = "TAIL"))
    private void addUpgrades(
            Item.TooltipContext context,
            TooltipDisplayComponent displayComponent,
            @Nullable PlayerEntity player,
            TooltipType type,
            Consumer<Text> textConsumer,
            CallbackInfo ci
    ) {
        ItemStack self = ((ItemStack)(Object)this);
        if(self.contains(ModDataComponentType.UNBREAKING)) {
            int level = Upgrade.getUnbreaking(self);
            if(level > 0) {
                textConsumer.accept(Text.literal("Unbreaking " + Upgrade.getUnbreaking(self)).formatted(Formatting.DARK_GREEN));
            }
        }
        if(self.contains(ModDataComponentType.EFFICIENCY)) {
            textConsumer.accept(Text.literal("Efficiency " + Upgrade.getEfficiency(self)).formatted(Formatting.DARK_GREEN));
        }
        if(self.contains(ModDataComponentType.PROTECTION)) {
            textConsumer.accept(Text.literal("Protection " + Upgrade.getProtection(self)).formatted(Formatting.DARK_GREEN));
        }
        if(self.contains(ModDataComponentType.SHARPNESS)) {
            textConsumer.accept(Text.literal("Sharpness " + Upgrade.getSharpness(self)).formatted(Formatting.DARK_GREEN));
        }
    }
}
