package net.space333.enchants.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.space333.enchants.Enchants;
import net.space333.enchants.util.EnchantmentPowerHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {

    @Shadow @Final private PlayerEntity player;
    @Unique int BAR_X1 = 60;
    @Unique int BAR_X2 = 168;
    @Unique int BAR_Y1 = 38;
    @Unique int BAR_Y2 = 42;

    @Unique int INPUT_POWER_COLOR = new Color(39, 174, 53).hashCode();
    @Unique int OUTPUT_POWER_COLOR = new Color(0, 255, 0).hashCode();
    @Unique int ILLEGAL_POWER_COLOR = new Color(255, 0, 0).hashCode();
    @Unique int DOT_COLOR = new Color(255, 255, 255).hashCode();
    @Unique int BORDER_COLOR = new Color(119, 119, 119).hashCode();
    @Unique int BACKGROUND_COLOR = new Color(81, 81, 81).hashCode();


    @Inject(method = "drawForeground", at = @At(value = "HEAD"))
    private void drawBar(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {

        AnvilScreen self = (AnvilScreen) (Object) this;
        AnvilScreenHandler screenHandler = self.getScreenHandler();
        ItemStack inputStack = ItemStack.EMPTY;
        ItemStack modifierStack = ItemStack.EMPTY;
        ItemStack outputStack = ItemStack.EMPTY;
        if (screenHandler.getSlot(0).hasStack()) {
            inputStack = screenHandler.slots.get(0).getStack();
        }
        if (screenHandler.getSlot(1).hasStack()) {
            modifierStack = screenHandler.slots.get(1).getStack();
        }
        if (screenHandler.getSlot(2).hasStack()) {
            outputStack = screenHandler.slots.get(2).getStack();
        }

        context.drawBorder(BAR_X1-1, BAR_Y1 -1, BAR_X2 - BAR_X1 + 2, BAR_Y2 - BAR_Y1 + 2, BORDER_COLOR);
        context.fill(BAR_X1, BAR_Y1, BAR_X2, BAR_Y2, BACKGROUND_COLOR);

        int maxPower = EnchantmentPowerHelper.getMaxEnchantmentPower(inputStack);
        int inputPower = EnchantmentPowerHelper.getCurrentEnchantmentPower(inputStack);
        int modifierPower = EnchantmentPowerHelper.getCurrentEnchantmentPower(modifierStack);
        int outputPower = EnchantmentPowerHelper.getCurrentEnchantmentPower(outputStack);

        if (!inputStack.isEmpty()) {
            context.fill(BAR_X1, BAR_Y1, barPos(inputPower, maxPower), BAR_Y2, INPUT_POWER_COLOR);

            if (inputPower > maxPower) {
                context.fill(BAR_X1, BAR_Y1, barPos(inputPower - maxPower, maxPower), BAR_Y2, ILLEGAL_POWER_COLOR);
            }
            else if(!modifierStack.isEmpty()) {
                if ((inputPower + modifierPower) > maxPower) {
                    context.fill(barPos(inputPower, maxPower), BAR_Y1, BAR_X2, BAR_Y2, OUTPUT_POWER_COLOR);
                    context.fill(BAR_X1, BAR_Y1, barPos(0.5, maxPower), BAR_Y2, ILLEGAL_POWER_COLOR);
                }
            }
            if(!outputStack.isEmpty() && !this.player.isInCreativeMode()) {
                if (outputPower > inputPower) {
                    context.fill(barPos(inputPower, maxPower), BAR_Y1, barPos(outputPower, maxPower), BAR_Y2, OUTPUT_POWER_COLOR);
                } else {
                    context.fill(BAR_X1, BAR_Y1, barPos(inputPower, maxPower), BAR_Y2, INPUT_POWER_COLOR);
                }
            }

        }



        for (int i = 1; i < maxPower; i++) {
            context.fill(barPos(i, maxPower) - 1, BAR_Y1+1, barPos(i, maxPower), BAR_Y2-1, DOT_COLOR);
        }
    }

    @Unique
    private int barPos(double x, int isc) {return BAR_X1 + Math.min((int) ((BAR_X2 - BAR_X1) * (x / (isc + 0.0f))), BAR_X2 - BAR_X1);}


    @ModifyConstant(method = "drawForeground", constant = @Constant(intValue = 40, ordinal = 0))
    private int newMax(int i, @Local(argsOnly = true) DrawContext context) {
        return 0;
    }
    /*
    @Redirect(method = "drawForeground", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/AnvilScreenHandler;getLevelCost()I"))
    private int bypassCostCheck(AnvilScreenHandler instance) {
        int cost = instance.getLevelCost();
        if(cost == 0) {
            return 1;
        }
        return cost;
    }
    */

    @Redirect(method = "drawForeground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isInCreativeMode()Z"))
    private boolean tooEnchanted(ClientPlayerEntity instance) {
        AnvilScreen self = (AnvilScreen) (Object) this;
        AnvilScreenHandler screenHandler = self.getScreenHandler();
        ItemStack inputStack = ItemStack.EMPTY;
        ItemStack modifierStack = ItemStack.EMPTY;
        if (screenHandler.getSlot(0).hasStack()) {
            inputStack = screenHandler.slots.get(0).getStack();
        }
        if (screenHandler.getSlot(1).hasStack()) {
            modifierStack = screenHandler.slots.get(1).getStack();
        }

        int maxPower = EnchantmentPowerHelper.getMaxEnchantmentPower(inputStack);
        int inputPower = EnchantmentPowerHelper.getCurrentEnchantmentPower(inputStack);
        int modifierPower = EnchantmentPowerHelper.getCurrentEnchantmentPower(modifierStack);

        if(!inputStack.isEmpty() && !modifierStack.isEmpty()) {
            if ((inputPower + modifierPower) > maxPower) {
                return instance.isInCreativeMode();
            }
        }
        return true;
    }
}
