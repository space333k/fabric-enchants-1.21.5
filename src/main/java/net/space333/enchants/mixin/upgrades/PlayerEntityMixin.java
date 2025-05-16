package net.space333.enchants.mixin.upgrades;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.space333.enchants.util.Upgrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Redirect(method = "getBlockBreakingSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D", ordinal = 0))
    private double applyEfficiency(PlayerEntity instance, RegistryEntry registryEntry) {
        PlayerEntity self = ((PlayerEntity) (Object) this);
        ItemStack itemStack = instance.getInventory().getSelectedStack();
        int level = Upgrade.getEfficiency(itemStack);
        float output = (float) self.getAttributeValue(EntityAttributes.MINING_EFFICIENCY);

        if(level == 1) {
            output = 5;
        }
        else if(level >= 2) {
            output = 25;
        }

        return output;
    }

    /*
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D", ordinal = 0))
    private double applySharpness(PlayerEntity instance, RegistryEntry registryEntry) {
        PlayerEntity self = ((PlayerEntity) (Object) this);
        ItemStack itemStack = instance.getInventory().getSelectedStack();
        int level = Upgrade.getSharpness(itemStack);
        float output = (float) self.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);

        if(level == 1) {
            output += 1;
        }
        else if(level >= 2) {
            output += 2;
        }

        return output;
    }
     */

}
