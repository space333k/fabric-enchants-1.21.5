package net.space333.enchants.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.space333.enchants.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ModTags.Items.UPGRADE_MATERIAL)
                .add(Items.GOLD_INGOT)
                .add(Items.IRON_INGOT)
                .add(Items.AMETHYST_SHARD)
                .add(Items.PRISMARINE_SHARD)
                .add(Items.BLACKSTONE);

        getOrCreateTagBuilder(ItemTags.TRIM_MATERIALS)
                .add(Items.PRISMARINE_SHARD)
                .add(Items.BLACKSTONE);
    }
}
