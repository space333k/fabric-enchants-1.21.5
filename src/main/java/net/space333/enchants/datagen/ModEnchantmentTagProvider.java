package net.space333.enchants.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import net.space333.enchants.enchantment.ModEnchantments;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagProvider extends FabricTagProvider.EnchantmentTagProvider {
    public ModEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(EnchantmentTags.ARMOR_EXCLUSIVE_SET)
                .setReplace(true);
        getOrCreateTagBuilder(EnchantmentTags.BOOTS_EXCLUSIVE_SET)
                .setReplace(true);
        getOrCreateTagBuilder(EnchantmentTags.CROSSBOW_EXCLUSIVE_SET)
                .setReplace(true);
        getOrCreateTagBuilder(EnchantmentTags.DAMAGE_EXCLUSIVE_SET)
                .setReplace(true);

        getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE)
                .setReplace(false)
                .add(ModEnchantments.REACH)
                .add(ModEnchantments.MAGIC_PROTECTION);

        getOrCreateTagBuilder(EnchantmentTags.ON_RANDOM_LOOT)
                .add(ModEnchantments.SOULBOUND);
        getOrCreateTagBuilder(EnchantmentTags.TOOLTIP_ORDER)
                .add(ModEnchantments.SOULBOUND);
        getOrCreateTagBuilder(EnchantmentTags.TREASURE)
                .add(ModEnchantments.SOULBOUND);

    }
}
