package net.space333.enchants.mixin.enchantments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.village.TradeOffers;
import net.space333.enchants.util.TweakEnchants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TradeOffers.EnchantBookFactory.class)
public class EnchantBookFactoryMixin {

    @ModifyExpressionValue(
            method = "create",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;nextInt(Lnet/minecraft/util/math/random/Random;II)I"
            )
    )
    private int limitMaxEnchantmentLevel(int original, @Local RegistryEntry<Enchantment> enchantment) {
        int value = original;
        if (original > TweakEnchants.MAX_TRADE_LEVEL) {
            value = TweakEnchants.MAX_TRADE_LEVEL;
        }
        return value;
    }
}