package net.space333.enchants.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;
import net.space333.enchants.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
    public ModEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS)
                .add(EntityType.SLIME)
                .add(EntityType.MAGMA_CUBE)
                .add(EntityType.GHAST)
                .add(EntityType.GUARDIAN)
                .add(EntityType.ELDER_GUARDIAN)
                .add(EntityType.SHULKER)
                .add(EntityType.BLAZE)
                .add(EntityType.VEX)
                .add(EntityType.BREEZE);

        getOrCreateTagBuilder(ModTags.EntityTypes.SENSITIVE_TO_INHUMAIN)
                .add(EntityType.VILLAGER)
                .add(EntityType.ZOMBIE_VILLAGER)
                .add(EntityType.WITCH)
                .add(EntityType.PLAYER)
                .add(EntityType.WANDERING_TRADER)
                .forceAddTag(EntityTypeTags.ILLAGER);

    }
}
