package net.space333.enchants.enchantment;

import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> SOULBOUND = of( "soulbound");
    public static final RegistryKey<Enchantment> REACH = of( "reach");
    public static final RegistryKey<Enchantment> EXCAVATOR = of( "excavator");

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

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
                        .addEffect(EnchantmentEffectComponentTypes.PREVENT_EQUIPMENT_DROP)
        );
    }


    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }

    private static RegistryKey<Enchantment> of(String id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of("enchants", id));
    }
}
