package net.space333.enchants.enchantment;

import net.minecraft.block.Block;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.AllOfLootCondition;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntityTypePredicate;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.space333.enchants.util.ModTags;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> SOULBOUND = of( "soulbound");
    public static final RegistryKey<Enchantment> REACH = of( "reach");
    public static final RegistryKey<Enchantment> EXCAVATOR = of( "excavator");
    public static final RegistryKey<Enchantment> MAGIC_PROTECTION = of( "magic_protection");
    public static final RegistryKey<Enchantment> INHUMAIN = of( "inhumain");
    public static final RegistryKey<Enchantment> BERSERKER = of( "berserker");
    public static final RegistryKey<Enchantment> SYPHON = of( "syphon");

    public static void bootstrap(Registerable<Enchantment> registerable) {
        RegistryEntryLookup<DamageType> damageTypes = registerable.getRegistryLookup(RegistryKeys.DAMAGE_TYPE);
        RegistryEntryLookup<Enchantment> enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        RegistryEntryLookup<Item> items = registerable.getRegistryLookup(RegistryKeys.ITEM);
        RegistryEntryLookup<Block> blocks = registerable.getRegistryLookup(RegistryKeys.BLOCK);
        RegistryEntryLookup<EntityType<?>> entityTypes = registerable.getRegistryLookup(RegistryKeys.ENTITY_TYPE);

        register(
                registerable,
                SOULBOUND,
                Enchantment.builder(
                                Enchantment.definition(
                                        items.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                                        2,
                                        1,
                                        Enchantment.leveledCost(25, 25),
                                        Enchantment.leveledCost(75, 25),
                                        4,
                                        AttributeModifierSlot.ANY
                                )
                        )
                        .addEffect(EnchantmentEffectComponentTypes.PREVENT_EQUIPMENT_DROP)
        );
        register(
                registerable,
                REACH,
                Enchantment.builder(
                                Enchantment.definition(
                                        items.getOrThrow(ItemTags.MINING_ENCHANTABLE),
                                        3,
                                        3,
                                        Enchantment.leveledCost(5, 15),
                                        Enchantment.leveledCost(20, 15),
                                        2,
                                        AttributeModifierSlot.MAINHAND
                                )
                        )
                        .addEffect(EnchantmentEffectComponentTypes.PREVENT_EQUIPMENT_DROP)
        );
        register(
                registerable,
                EXCAVATOR,
                Enchantment.builder(
                                Enchantment.definition(
                                        items.getOrThrow(ItemTags.PICKAXES),
                                        3,
                                        3,
                                        Enchantment.leveledCost(10, 25),
                                        Enchantment.leveledCost(35, 25),
                                        2,
                                        AttributeModifierSlot.MAINHAND
                                )
                        )
                        .exclusiveSet(registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT).getOrThrow(EnchantmentTags.MINING_EXCLUSIVE_SET))
        );
        register(
                registerable,
                MAGIC_PROTECTION,
                Enchantment.builder(
                                Enchantment.definition(
                                        items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                        5,
                                        3,
                                        Enchantment.leveledCost(10, 12),
                                        Enchantment.leveledCost(22, 12),
                                        2,
                                        AttributeModifierSlot.ARMOR
                                )
                        )
                        .addEffect(
                                EnchantmentEffectComponentTypes.DAMAGE_PROTECTION,
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(2.0F)),
                                AllOfLootCondition.builder(
                                        DamageSourcePropertiesLootCondition.builder(
                                                DamageSourcePredicate.Builder.create()
                                                        .tag(TagPredicate.expected(DamageTypeTags.WITCH_RESISTANT_TO))
                                                        .tag(TagPredicate.unexpected(DamageTypeTags.BYPASSES_INVULNERABILITY))
                                        )
                                )
                        )

        );
        register(
                registerable,
                INHUMAIN,
                Enchantment.builder(
                                Enchantment.definition(
                                        items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                        5,
                                        3,
                                        Enchantment.leveledCost(5, 12),
                                        Enchantment.leveledCost(25, 12),
                                        2,
                                        AttributeModifierSlot.MAINHAND
                                )
                        )
                        .addEffect(
                                EnchantmentEffectComponentTypes.DAMAGE,
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(2.5F)),
                                EntityPropertiesLootCondition.builder(
                                        LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.create().type(
                                                EntityTypePredicate.create(entityTypes, ModTags.EntityTypes.SENSITIVE_TO_INHUMAIN)
                                        )
                                )
                        )
        );
        register(
                registerable,
                BERSERKER,
                Enchantment.builder(
                                Enchantment.definition(
                                        items.getOrThrow(ItemTags.AXES),
                                        5,
                                        3,
                                        Enchantment.leveledCost(5, 12),
                                        Enchantment.leveledCost(25, 12),
                                        2,
                                        AttributeModifierSlot.MAINHAND
                                )
                        )
        );
        register(
                registerable,
                SYPHON,
                Enchantment.builder(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                                5,
                                3,
                                Enchantment.leveledCost(15, 15),
                                Enchantment.leveledCost(30, 15),
                                2,
                                AttributeModifierSlot.MAINHAND
                        )
                )
        );
    }


    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }

    private static RegistryKey<Enchantment> of(String id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of("enchants", id));
    }
}
