package net.space333.enchants.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.space333.enchants.Enchants;

import java.util.ArrayList;
import java.util.List;

public class TweakEnchants {
    public static List<String> DISABLED_ENCHANTMENTS = List.of(
            "minecraft:unbreaking",
            "minecraft:efficiency",
            "minecraft:protection",
            "minecraft:sharpness",
            "minecraft:power"
    );

    //public static List<String> MAX_LEVELS = List.of("minecraft:blast_protection/3");
    public static int MAX_TRADE_LEVEL = 2;

    private static final List<String> INVALID_ENTRIES = new ArrayList<>();

    public static boolean filterStacks(ItemStack stack) {
        for (String s : DISABLED_ENCHANTMENTS) {
            ItemEnchantmentsComponent storedEnchantments = stack.get(DataComponentTypes.STORED_ENCHANTMENTS);
            if (storedEnchantments != null) {
                try {
                    if (storedEnchantments.getEnchantments().stream().anyMatch(holder -> holder.matchesId(Identifier.tryParse(s))))
                        return true;
                } catch (InvalidIdentifierException e) {
                    handleIdentifierException(s, e);
                }
            }
        }
        return false;
    }

    public static void handleIdentifierException(String s, InvalidIdentifierException e) {
        if (!INVALID_ENTRIES.contains(s)) {
            Enchants.LOGGER.error("[Enchantment Disabler] Failed to parse enchantment {}:", s);
            Enchants.LOGGER.error(e.getMessage());
            Enchants.LOGGER.error("Verify that enchantments added in the disabled enchantments list inside the mod config are valid.");
            INVALID_ENTRIES.add(s);
        }
    }

    public static ItemEnchantmentsComponent getItemEnchantments(ItemStack stack) {
        if (stack.contains(DataComponentTypes.STORED_ENCHANTMENTS)) return stack.get(DataComponentTypes.STORED_ENCHANTMENTS);
        else return stack.get(DataComponentTypes.ENCHANTMENTS);
    }
}
