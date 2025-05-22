package net.space333.enchants.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.item.equipment.trim.ArmorTrimMaterials;
import net.minecraft.registry.entry.RegistryEntry;

public class EnchantmentPowerHelper {

    public static int getCurrentEnchantmentPower(ItemStack itemStack) {
        int power = 0;
        ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(itemStack);
        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
            RegistryEntry<Enchantment> enchantmentRegistry = (RegistryEntry<Enchantment>) entry.getKey();
            Enchantment enchantment = enchantmentRegistry.value();
            if (enchantment.getMaxLevel() > 1) {
                power += entry.getIntValue();
            }
        }
        return power;
    }

    public static int getMaxEnchantmentPower(ItemStack itemStack) {
        Item item = itemStack.getItem();

        int maxEnchant = 0;

        maxEnchant = getToolMaxEnchant(item);

        if(maxEnchant == 0) {
            maxEnchant = getArmorMaxEnchant(item);
        }
        if(maxEnchant == 0) {
            if(item == Items.BOW || item == Items.CROSSBOW) {
                maxEnchant = 5;
            }
            else if(item == Items.TRIDENT || item == Items.MACE) {
                maxEnchant = 7;
            }
            else if(item == Items.ELYTRA) {
                maxEnchant = 5;
            }
            else if(item == Items.FISHING_ROD || item == Items.CARROT_ON_A_STICK || item == Items.WARPED_FUNGUS_ON_A_STICK) {
                maxEnchant = 5;
            }
        }

        maxEnchant += getArmorTrimBonus(itemStack);

        return maxEnchant;
    }

    private static int getToolMaxEnchant(Item item) {
        if(item == Items.NETHERITE_AXE || item == Items.NETHERITE_HOE || item == Items.NETHERITE_PICKAXE || item == Items.NETHERITE_SHOVEL || item == Items.NETHERITE_SWORD) {
            return 7;
        }
        else if(item == Items.DIAMOND_AXE || item == Items.DIAMOND_HOE || item == Items.DIAMOND_PICKAXE || item == Items.DIAMOND_SHOVEL || item == Items.DIAMOND_SWORD) {
            return 6;
        }
        else if(item == Items.GOLDEN_AXE || item == Items.GOLDEN_HOE || item == Items.GOLDEN_PICKAXE || item == Items.GOLDEN_SHOVEL || item == Items.GOLDEN_SWORD) {
            return 8;
        }
        else if(item == Items.IRON_AXE || item == Items.IRON_HOE || item == Items.IRON_PICKAXE || item == Items.IRON_SHOVEL || item == Items.IRON_SWORD) {
            return 5;
        }
        else if(item == Items.STONE_AXE || item == Items.STONE_HOE || item == Items.STONE_PICKAXE || item == Items.STONE_SHOVEL || item == Items.STONE_SWORD) {
            return 4;
        }
        else if(item == Items.WOODEN_AXE || item == Items.WOODEN_HOE || item == Items.WOODEN_PICKAXE || item == Items.WOODEN_SHOVEL || item == Items.WOODEN_SWORD) {
            return 3;
        }
        return 0;
    }

    private static int getArmorMaxEnchant(Item item) {
        if(item == Items.NETHERITE_HELMET || item == Items.NETHERITE_CHESTPLATE || item == Items.NETHERITE_LEGGINGS || item == Items.NETHERITE_BOOTS) {
            return 7;
        }
        else if(item == Items.DIAMOND_HELMET || item == Items.DIAMOND_CHESTPLATE || item == Items.DIAMOND_LEGGINGS || item == Items.DIAMOND_BOOTS || item == Items.DIAMOND_HORSE_ARMOR) {
            return 6;
        }
        else if(item == Items.GOLDEN_HELMET || item == Items.GOLDEN_CHESTPLATE || item == Items.GOLDEN_LEGGINGS || item == Items.GOLDEN_BOOTS || item == Items.GOLDEN_HORSE_ARMOR) {
            return 8;
        }
        else if(item == Items.IRON_HELMET || item == Items.IRON_CHESTPLATE || item == Items.IRON_LEGGINGS || item == Items.IRON_BOOTS || item == Items.IRON_HORSE_ARMOR) {
            return 5;
        }
        else if(item == Items.CHAINMAIL_HELMET || item == Items.CHAINMAIL_CHESTPLATE || item == Items.CHAINMAIL_LEGGINGS || item == Items.CHAINMAIL_BOOTS) {
            return 5;
        }
        else if(item == Items.LEATHER_HELMET || item == Items.LEATHER_CHESTPLATE || item == Items.LEATHER_LEGGINGS || item == Items.LEATHER_BOOTS || item == Items.LEATHER_HORSE_ARMOR) {
            return 4;
        }
        else if(item == Items.TURTLE_HELMET) {
            return 5;
        }
        else if(item == Items.WOLF_ARMOR) {
            return 5;
        }
        return 0;
    }

    private static int getArmorTrimBonus(ItemStack itemStack) {
        ArmorTrim trim = itemStack.get(DataComponentTypes.TRIM);

        if(trim == null) {
            return 0;
        }
        else if(trim.material() == ArmorTrimMaterials.IRON) {
            return 1;
        }
        else if(trim.material() == ArmorTrimMaterials.DIAMOND) {
            return 2;
        }
        else if(trim.material() == ArmorTrimMaterials.NETHERITE) {
            return 3;
        }
        else if(trim.material() == ArmorTrimMaterials.GOLD) {
            return 4;
        }
        return 0;
    }

}
