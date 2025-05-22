package net.space333.enchants.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.space333.enchants.Component.ModDataComponentType;

public class Upgrade {

    static int REPAIR_AMOUNT_TO_UPGRADE = 1;

    static int MAX_LEVEL = 2;

    public static Integer getUnbreaking(ItemStack itemStack) {
        if(itemStack.contains(ModDataComponentType.UNBREAKING)) {
            int level = itemStack.getOrDefault(ModDataComponentType.UNBREAKING, 0);
            if(level < REPAIR_AMOUNT_TO_UPGRADE) {
                return 0;
            }
            else if(level < 2* REPAIR_AMOUNT_TO_UPGRADE) {
                return 1;
            }
            return 2;
        }
        else {
            return 0;
        }
    }

    public static void setUnbreaking(ItemStack itemStack, int x) {
        if(x >= MAX_LEVEL * REPAIR_AMOUNT_TO_UPGRADE) {
            x = MAX_LEVEL * REPAIR_AMOUNT_TO_UPGRADE;
        }
        if(x > 0) {
            itemStack.set(ModDataComponentType.UNBREAKING, x);
        }

    }

    public static boolean isMaxUnbreaking(ItemStack itemStack) {
        return getUnbreaking(itemStack) >= MAX_LEVEL;
    }

    public static boolean incrementUnbreaking(ItemStack itemStack) {
        int level = itemStack.getOrDefault(ModDataComponentType.UNBREAKING, 0);
        boolean success = false;
        if(level >= MAX_LEVEL * REPAIR_AMOUNT_TO_UPGRADE) {
            level = MAX_LEVEL * REPAIR_AMOUNT_TO_UPGRADE;
        }
        else {
            level++;
            success = true;
        }
        setUnbreaking(itemStack, level);
        return success;
    }


    public static Integer getEfficiency(ItemStack itemStack) {
        if(itemStack.contains(ModDataComponentType.EFFICIENCY)) {
            return itemStack.getOrDefault(ModDataComponentType.EFFICIENCY, 0);
        }
        else {
            return 0;
        }
    }

    public static void setEfficiency(ItemStack itemStack, int x) {
        if(x >= MAX_LEVEL) {
            x = MAX_LEVEL;
        }
        if(x > 0) {
            itemStack.set(ModDataComponentType.EFFICIENCY, x);
        }
    }

    public static boolean incrementEfficiency(ItemStack itemStack) {
        int level = getEfficiency(itemStack);
        boolean success = false;
        if(level >= MAX_LEVEL) {
            level = MAX_LEVEL;
        }
        else {
            level++;
            success = true;
        }
        setEfficiency(itemStack, level);
        return success;
    }


    public static Integer getProtection(ItemStack itemStack) {
        if(itemStack.contains(ModDataComponentType.PROTECTION)) {
            return itemStack.getOrDefault(ModDataComponentType.PROTECTION, 0);
        }
        else {
            return 0;
        }
    }

    public static void setProtection(ItemStack itemStack, int x) {
        if(x >= MAX_LEVEL) {
            x = MAX_LEVEL;
        }
        if(x > 0) {
            itemStack.set(ModDataComponentType.PROTECTION, x);
        }
    }

    public static boolean incrementProtection(ItemStack itemStack) {
        int level = getProtection(itemStack);
        boolean success = false;
        if(level >= MAX_LEVEL) {
            level = MAX_LEVEL;
        }
        else {
            level++;
            success = true;
        }
        setProtection(itemStack, level);
        return success;
    }


    public static AttributeModifiersComponent increaseComponent(ItemStack itemStack) {
        EntityAttributeModifier modifier;
        double attack = 1;
        double speed = 0;

        AttributeModifiersComponent modifiers = itemStack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if(modifiers != null) {
            modifier = modifiers.modifiers().getFirst().modifier();
            if (modifier.idMatches(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID)) {
                attack += modifier.value();
            }

            modifier = modifiers.modifiers().getLast().modifier();
            if (modifier.idMatches(Item.BASE_ATTACK_SPEED_MODIFIER_ID)) {
                speed += modifier.value();
            }
        }


        EntityAttributeModifier attack_modifier = new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, attack, EntityAttributeModifier.Operation.ADD_VALUE);
        EntityAttributeModifier speed_modifier = new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, speed, EntityAttributeModifier.Operation.ADD_VALUE);

        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE, attack_modifier, AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED, speed_modifier, AttributeModifierSlot.MAINHAND).build();
    }

    public static Integer getSharpness(ItemStack itemStack) {
        if(itemStack.contains(ModDataComponentType.SHARPNESS)) {
            return itemStack.getOrDefault(ModDataComponentType.SHARPNESS, 0);
        }
        else {
            return 0;
        }
    }

    public static void setSharpness(ItemStack itemStack, int x) {
        if(x >= MAX_LEVEL) {
            x = MAX_LEVEL;
        }
        if(x > 0) {
            itemStack.set(ModDataComponentType.SHARPNESS, x);
        }
    }

    public static boolean incrementSharpness(ItemStack itemStack) {
        int level = getSharpness(itemStack);
        boolean success = false;
        if(level >= MAX_LEVEL) {
            level = MAX_LEVEL;
        }
        else {
            level++;
            itemStack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, increaseComponent(itemStack));
            success = true;
        }
        setSharpness(itemStack, level);
        return success;
    }


    public static boolean changeAttribute(ItemStack itemStack1, ItemStack itemStack2, ItemStack outputStack) {
        if (itemStack1.isIn(ItemTags.MINING_ENCHANTABLE) && itemStack2.isOf(Items.GOLD_INGOT)) {
            return incrementEfficiency(outputStack);
        } else if (itemStack1.isIn(ItemTags.ARMOR_ENCHANTABLE) && itemStack2.isOf(Items.IRON_INGOT)) {
            return incrementProtection(outputStack);
        } else if (itemStack1.isIn(ItemTags.SHARP_WEAPON_ENCHANTABLE) && itemStack2.isOf(Items.AMETHYST_SHARD)) {
            return incrementSharpness(outputStack);
        } else if (itemStack1.isIn(ItemTags.TRIDENT_ENCHANTABLE) && itemStack2.isOf(Items.AMETHYST_SHARD)) {
            return incrementSharpness(outputStack);
        } else if (itemStack1.isIn(ItemTags.MACE_ENCHANTABLE) && itemStack2.isOf(Items.AMETHYST_SHARD)) {
            return incrementSharpness(outputStack);
        }
        return false;
    }
}
