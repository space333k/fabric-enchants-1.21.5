package net.space333.enchants.mixin.enchantments;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.space333.enchants.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(method = {"isPrimaryItem", "isAcceptableItem", "isSupportedItem"}, at = @At(value = "HEAD"), cancellable = true)
    private void otherChecks(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Enchantment enchantment = (Enchantment)(Object)this;
        if (stack.getComponents().contains(DataComponentTypes.EQUIPPABLE)) {
            if (stack.getComponents().get(DataComponentTypes.EQUIPPABLE).equipSound() == SoundEvents.ENTITY_HORSE_ARMOR) {
                cir.setReturnValue(enchantment.isAcceptableItem(Items.DIAMOND_BOOTS.getDefaultStack()) && !enchantment.isAcceptableItem(Items.FLINT_AND_STEEL.getDefaultStack()));
                cir.cancel();
            }
        }
    }


    @ModifyVariable(method = "slotMatches", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private EquipmentSlot feetEnchantsOnHorse(EquipmentSlot slot){
        if (slot==EquipmentSlot.BODY) {
            return EquipmentSlot.FEET;
        }
        return slot;
    }
}
