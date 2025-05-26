package net.space333.enchants.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.space333.enchants.enchantment.ModEnchantments;

public class ModEnchantmentHelper {

    public static boolean hasEnchantment(ItemStack itemStack, RegistryKey<Enchantment> enchantment) {
        return EnchantmentHelper.getEnchantments(itemStack).getEnchantments().stream().anyMatch(
                enchantmentEntry -> enchantmentEntry.matchesKey(enchantment)
        );
    }

    public static int getEnchantmentLevel(ItemStack itemStack, RegistryKey<Enchantment> enchantment) {
        if(hasEnchantment(itemStack, enchantment)) {
            int level = 0;
            ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(itemStack);
            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
                RegistryEntry<Enchantment> enchantmentEntry = (RegistryEntry<Enchantment>)entry.getKey();
                if(enchantmentEntry.matchesKey(enchantment)) {
                    level = entry.getIntValue();
                }
            }

            return level;
        }
        else {
            return 0;
        }
    }
}
