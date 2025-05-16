package net.space333.enchants.mixin.enchantments;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.space333.enchants.util.TweakEnchants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @WrapOperation(
            method = "method_60106",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
            )
    )
    private static <E> boolean preventAdditionIfDisabled(List<EnchantmentLevelEntry> instance, E e, Operation<Boolean> original) {
        EnchantmentLevelEntry eh = (EnchantmentLevelEntry) e;
        if (
                TweakEnchants.DISABLED_ENCHANTMENTS.stream().anyMatch(s -> {
                    try {
                        return eh.enchantment().matchesId(Identifier.tryParse(s));
                    } catch (InvalidIdentifierException ex) {
                        TweakEnchants.handleIdentifierException(s ,ex);
                        return false;
                    }
                })
        ) {
            return false;
        }
        return original.call(instance, e);
    }
}
