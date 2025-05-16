package net.space333.enchants.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.space333.enchants.Enchants;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> UPGRADE_MATERIAL = createTag("upgrade_material");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(Enchants.MOD_ID, name));
        }

    }

    public static class Enchantments {
        public static final TagKey<Enchantment> MUTUAL_EXCLUSION = createTag("upgrade_material");

        private static TagKey<Enchantment> createTag(String name) {
            return TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Enchants.MOD_ID, name));
        }

    }
}
