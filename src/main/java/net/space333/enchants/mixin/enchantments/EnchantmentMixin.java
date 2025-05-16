package net.space333.enchants.mixin.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.space333.enchants.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @Inject(method = "canBeCombined", at = @At(value = "RETURN"), cancellable = true)
    private static void  removeMutualExclusion(RegistryEntry<Enchantment> first, RegistryEntry<Enchantment> second, CallbackInfoReturnable<Boolean> cir) {
        boolean result = cir.getReturnValue();

        if(first.isIn(ModTags.Enchantments.MUTUAL_EXCLUSION) && second.isIn(ModTags.Enchantments.MUTUAL_EXCLUSION)) {
            cir.setReturnValue(result);
        }
        cir.setReturnValue(!first.equals(second));
    }
}
