package net.space333.enchants.enchantment.custom;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.Component;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.space333.enchants.Enchants;
import net.space333.enchants.enchantment.ModEnchantments;
import net.space333.enchants.util.ModEnchantmentHelper;

import java.util.stream.Stream;

public class SoulBound {

    public static void copySoulBoundItems(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        if (!alive && !(oldPlayer.getServerWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY) || oldPlayer.isSpectator())) {
            for (int i = 0; i < oldPlayer.getInventory().size(); i++) {
                ItemStack oldStack = oldPlayer.getInventory().getStack(i);
                ItemStack newStack = newPlayer.getInventory().getStack(i);
                if (hasSoulbound(oldStack) && !ItemStack.areEqual(oldStack, newStack)) {
                    removeSoulbound(oldStack);
                    if (newStack.isEmpty()) {
                        newPlayer.getInventory().setStack(i, oldStack);
                    } else {
                        newPlayer.getInventory().offerOrDrop(oldStack);
                    }
                }
            }
        }
    }

    private static void removeSoulbound(ItemStack itemStack) {
        if(!itemStack.isOf(Items.ENCHANTED_BOOK)) {
            ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.apply(
                    itemStack, components -> components.remove(
                            enchantment -> enchantment.matchesKey(ModEnchantments.SOULBOUND)
                    )
            );
        }

    }

    public static boolean hasSoulbound(ItemStack itemStack) {
        return ModEnchantmentHelper.hasEnchantment(itemStack, ModEnchantments.SOULBOUND);
    }
}
