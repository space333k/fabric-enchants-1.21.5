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
import net.minecraft.util.Identifier;
import net.space333.enchants.Component.ModDataComponentType;

import java.util.UUID;

public class Upgrade {

    static int repair_amount_levelup = 1;

    public static Integer getUnbreaking(ItemStack itemStack) {
        if(itemStack.contains(ModDataComponentType.UNBREAKING)) {
            int level = itemStack.getOrDefault(ModDataComponentType.UNBREAKING, 0);
            if(level < repair_amount_levelup) {
                return 0;
            }
            else if(level < 2*repair_amount_levelup) {
                return 1;
            }
            return 2;
        }
        else {
            return 0;
        }
    }

    public static void setUnbreaking(ItemStack itemStack, int x) {
        itemStack.set(ModDataComponentType.UNBREAKING, x);
    }

    public static void incrementUnbreaking(ItemStack itemStack) {
        int level = itemStack.getOrDefault(ModDataComponentType.UNBREAKING, 0);
        if(level >= 2*repair_amount_levelup) {
            level = 2*repair_amount_levelup;
        }
        else {
            level++;
        }
        setUnbreaking(itemStack, level);
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
        itemStack.set(ModDataComponentType.EFFICIENCY, x);
    }

    public static void incrementEfficiency(ItemStack itemStack) {
        int level = getEfficiency(itemStack);
        if(level >= 2) {
            level = 2;
        }
        else {
            level++;
        }
        setEfficiency(itemStack, level);
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
        itemStack.set(ModDataComponentType.PROTECTION, x);
    }

    public static void incrementProtection(ItemStack itemStack) {
        int level = getProtection(itemStack);
        if(level >= 2) {
            level = 2;
        }
        else {
            level++;
        }
        setProtection(itemStack, level);
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
        itemStack.set(ModDataComponentType.SHARPNESS, x);
    }

    public static void incrementSharpness(ItemStack itemStack) {
        int level = getSharpness(itemStack);
        if(level >= 2) {
            level = 2;
        }
        else {
            level++;
            itemStack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, increaseComponent(itemStack));
        }
        setSharpness(itemStack, level);
    }


    public static Integer getPower(ItemStack itemStack) {
        if(itemStack.contains(ModDataComponentType.POWER)) {
            return itemStack.getOrDefault(ModDataComponentType.POWER, 0);
        }
        else {
            return 0;
        }
    }

    public static void setPower(ItemStack itemStack, int x) {
        itemStack.set(ModDataComponentType.POWER, x);
    }

    public static void incrementPower(ItemStack itemStack) {
        int level = getPower(itemStack);
        if(level >= 2) {
            level = 2;
        }
        else {
            level++;
        }
        setPower(itemStack, level);
    }


    public static Integer getImpaling(ItemStack itemStack) {
        if(itemStack.contains(ModDataComponentType.IMPALING)) {
            return itemStack.getOrDefault(ModDataComponentType.IMPALING, 0);
        }
        else {
            return 0;
        }
    }

    public static void setImpaling(ItemStack itemStack, int x) {
        itemStack.set(ModDataComponentType.IMPALING, x);
    }

    public static void incrementImpaling(ItemStack itemStack) {
        int level = getImpaling(itemStack);
        if(level >= 2) {
            level = 2;
        }
        else {
            level++;
        }
        setImpaling(itemStack, level);
    }


    public static Integer getDensity(ItemStack itemStack) {
        if(itemStack.contains(ModDataComponentType.DENSITY)) {
            return itemStack.getOrDefault(ModDataComponentType.DENSITY, 0);
        }
        else {
            return 0;
        }
    }

    public static void setDensity(ItemStack itemStack, int x) {
        itemStack.set(ModDataComponentType.DENSITY, x);
    }

    public static void incrementDensity(ItemStack itemStack) {
        int level = getDensity(itemStack);
        if(level >= 2) {
            level = 2;
        }
        else {
            level++;
        }
        setDensity(itemStack, level);
    }

    public static boolean changeAttribute(ItemStack itemStack1, ItemStack itemStack2, ItemStack outputStack) {
        boolean changed = false;
        if(itemStack1.isIn(ItemTags.MINING_ENCHANTABLE) && itemStack2.isOf(Items.GOLD_INGOT)) {
            Upgrade.incrementEfficiency(outputStack);
            changed = true;
        }
        else if(itemStack1.isIn(ItemTags.ARMOR_ENCHANTABLE) && itemStack2.isOf(Items.IRON_INGOT)) {
            Upgrade.incrementProtection(outputStack);
            changed = true;
        }
        else if(itemStack1.isIn(ItemTags.SHARP_WEAPON_ENCHANTABLE) && itemStack2.isOf(Items.AMETHYST_SHARD)) {
            Upgrade.incrementSharpness(outputStack);
            changed = true;
        }
        else if((itemStack1.isIn(ItemTags.BOW_ENCHANTABLE) || itemStack1.isIn(ItemTags.CROSSBOW_ENCHANTABLE)) && itemStack2.isOf(Items.AMETHYST_SHARD)) {
            Upgrade.incrementPower(outputStack);
            changed = true;
        }
        else if(itemStack1.isIn(ItemTags.TRIDENT_ENCHANTABLE) && itemStack2.isOf(Items.AMETHYST_SHARD)) {
            Upgrade.incrementSharpness(outputStack);
            changed = true;
        }
        else if(itemStack1.isIn(ItemTags.MACE_ENCHANTABLE) && itemStack2.isOf(Items.AMETHYST_SHARD)) {
            Upgrade.incrementSharpness(outputStack);
            changed = true;
        }
        return changed;
    }



}
