package net.space333.enchants.mixin.enchantments;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.space333.enchants.enchantment.custom.Reach;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "getBlockInteractionRange", at = @At(value = "RETURN"), cancellable = true)
    private void addReachEnchantment(CallbackInfoReturnable<Double> cir) {
        double value = cir.getReturnValue();
        PlayerEntity player = ((PlayerEntity) (Object) this);
        ItemStack itemStack = player.getMainHandStack();

        value += Reach.getReachLevel(itemStack) * 0.5;
        cir.setReturnValue(value);
    }
}
