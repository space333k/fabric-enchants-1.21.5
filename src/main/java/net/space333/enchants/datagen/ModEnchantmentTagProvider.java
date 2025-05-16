package net.space333.enchants.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryWrapper;
import net.space333.enchants.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagProvider extends FabricTagProvider.EnchantmentTagProvider {
    public ModEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ModTags.Enchantments.MUTUAL_EXCLUSION)
                .add(Enchantments.SILK_TOUCH)
                .add(Enchantments.FORTUNE)
                .add(Enchantments.CHANNELING)
                .add(Enchantments.RIPTIDE)
                .add(Enchantments.LOYALTY)
                .add(Enchantments.INFINITY)
                .add(Enchantments.MENDING);

    }
}
