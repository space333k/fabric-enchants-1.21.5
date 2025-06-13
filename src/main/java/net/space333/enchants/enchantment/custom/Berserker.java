package net.space333.enchants.enchantment.custom;

import net.minecraft.item.ItemStack;
import net.space333.enchants.enchantment.ModEnchantments;
import net.space333.enchants.util.ModEnchantmentHelper;

public class Berserker {

    public static boolean hasBerserker(ItemStack itemStack) {
        return ModEnchantmentHelper.hasEnchantment(itemStack, ModEnchantments.BERSERKER);
    }

    public static int getBerserkerLevel(ItemStack itemStack) {
        return ModEnchantmentHelper.getEnchantmentLevel(itemStack, ModEnchantments.BERSERKER);
    }

}
