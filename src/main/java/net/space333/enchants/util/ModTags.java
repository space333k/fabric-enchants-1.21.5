package net.space333.enchants.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
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

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> SENSITIVE_TO_INHUMAIN = createTag("sensitive_to_inhumain");

        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Enchants.MOD_ID, name));
        }
    }

    public static class DamageTypes {
        public static final TagKey<DamageType> IS_MAGIC = createTag("is_magic");

        private static TagKey<DamageType> createTag(String name) {
            return TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(Enchants.MOD_ID, name));
        }

    }
}
