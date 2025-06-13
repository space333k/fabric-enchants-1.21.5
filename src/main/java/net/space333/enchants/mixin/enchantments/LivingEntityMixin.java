package net.space333.enchants.mixin.enchantments;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.space333.enchants.enchantment.custom.Berserker;
import net.space333.enchants.enchantment.custom.Syphon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow protected abstract float applyArmorToDamage(DamageSource source, float amount);

    @Redirect(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"))
    public float applyBerserker(LivingEntity instance, DamageSource source, float amount) {
        Entity entity = source.getAttacker();

        if(entity instanceof LivingEntity attacker) {
            float missingHealth = ((float) attacker.defaultMaxHealth) - attacker.getHealth();
            ItemStack weapon = attacker.getMainHandStack();
            if(Berserker.hasBerserker(weapon)) {
                int level = Berserker.getBerserkerLevel(weapon);

                float bonusDamage = missingHealth/6 * level;
                amount += bonusDamage;
            }
        }

        return this.applyArmorToDamage(source, amount);
    }

    @Inject(method = "onDeath", at = @At(value = "HEAD"))
    public void applySyphon(DamageSource damageSource, CallbackInfo ci) {
        ItemStack weapon = damageSource.getWeaponStack();

        if(weapon != null) {
            if(Syphon.hasSyphon(weapon)) {
                Entity entity = damageSource.getAttacker();
                if(entity instanceof LivingEntity attacker) {
                    int level = Syphon.getSyphonLevel(weapon);
                    attacker.setHealth(attacker.getHealth() + level);

                }
            }
        }
    }

}
