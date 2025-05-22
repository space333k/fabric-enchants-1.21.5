package net.space333.enchants.enchantment.custom;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.space333.enchants.Enchants;
import net.space333.enchants.enchantment.ModEnchantments;
import net.space333.enchants.util.ModEnchantmentHelper;

public class Reach {
    private static final Identifier identifier = Identifier.of(Enchants.MOD_ID, "reach_boost");

    public static void addReach(PlayerEntity player, double extraReach) {
        var attributeInstance = player.getAttributeInstance(EntityAttributes.BLOCK_INTERACTION_RANGE);
        if (attributeInstance != null && attributeInstance.getModifier(identifier) == null) {
            attributeInstance.addPersistentModifier(
                    new EntityAttributeModifier(identifier, extraReach, EntityAttributeModifier.Operation.ADD_VALUE)
            );
        }
    }

    public static void removeReach(PlayerEntity player) {
        var attributeInstance = player.getAttributeInstance(EntityAttributes.BLOCK_INTERACTION_RANGE);
        if (attributeInstance != null) {
            attributeInstance.removeModifier(identifier);
        }
    }

    public static boolean hasReach(ItemStack itemStack) {
        return ModEnchantmentHelper.hasEnchantment(itemStack, ModEnchantments.REACH);
    }

    public static int getReachLevel(ItemStack itemStack) {
        return ModEnchantmentHelper.getEnchantmentLevel(itemStack, ModEnchantments.REACH);
    }

}
