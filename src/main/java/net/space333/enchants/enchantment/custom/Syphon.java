package net.space333.enchants.enchantment.custom;

import net.minecraft.item.ItemStack;
import net.space333.enchants.enchantment.ModEnchantments;
import net.space333.enchants.util.ModEnchantmentHelper;

public class Syphon {

    public static boolean hasSyphon(ItemStack itemStack) {
        return ModEnchantmentHelper.hasEnchantment(itemStack, ModEnchantments.SYPHON);
    }

    public static int getSyphonLevel(ItemStack itemStack) {
        return ModEnchantmentHelper.getEnchantmentLevel(itemStack, ModEnchantments.SYPHON);
    }

}
