package net.space333.enchants.mixin.enchantments;

import net.minecraft.entity.EntityEquipment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.space333.enchants.enchantment.custom.SoulBound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Shadow @Final
    public PlayerEntity player;
    @Shadow @Final
    private EntityEquipment equipment;

    @Redirect(method = "dropAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    public boolean reserveItems(ItemStack itemStack) {
        return this.player.isAlive() || SoulBound.hasSoulbound(itemStack) || itemStack.isEmpty();
    }


    @Redirect(method = "dropAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityEquipment;dropAll(Lnet/minecraft/entity/LivingEntity;)V"))
    public void reserveEquipmentItems(EntityEquipment instance, LivingEntity entity) {
        ItemStack itemStack;
        for(int i = 1; i <= 5; i++) {
            itemStack = this.equipment.get(EquipmentSlot.FROM_INDEX.apply(i));
            if(!SoulBound.hasSoulbound(itemStack)) {
                entity.dropItem(itemStack, true, false);
                this.equipment.put(EquipmentSlot.FROM_INDEX.apply(i), ItemStack.EMPTY);
            }
        }
    }

}
