package net.space333.enchants.mixin.upgrades;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.space333.enchants.util.Upgrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Redirect(method = "modifyAppliedDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getProtectionAmount(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/damage/DamageSource;)F"))
    private float applyProtection(ServerWorld world, LivingEntity user, DamageSource damageSource) {
        LivingEntity self = ((LivingEntity) (Object) this);
        float protection = EnchantmentHelper.getProtectionAmount(world, user, damageSource);

        for (EquipmentSlot equipmentSlot : EquipmentSlot.VALUES) {
            if(!self.getEquippedStack(equipmentSlot).isEmpty()) {
                protection += (float) Upgrade.getProtection(self.getEquippedStack(equipmentSlot));
            }

        }
        return protection;
    }
}
