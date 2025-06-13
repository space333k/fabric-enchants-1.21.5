package net.space333.enchants;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.space333.enchants.Component.ModDataComponentType;
import net.space333.enchants.enchantment.custom.SoulBound;
import net.space333.enchants.item.ModItemGroups;
import net.space333.enchants.item.ModItems;
import net.space333.enchants.util.ExcavatorUsageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Enchants implements ModInitializer {
	public static final String MOD_ID = "enchants";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();

		ModDataComponentType.registerDataComponentTypes();

		ServerPlayerEvents.COPY_FROM.register(SoulBound::copySoulBoundItems);
		PlayerBlockBreakEvents.BEFORE.register(new ExcavatorUsageEvent());

		LootTableEvents.MODIFY.register(
				(key, tableBuilder, source, registries) -> {
					if (key.getValue().equals(Identifier.ofVanilla("entities/elder_guardian"))) {
						LootPool pool = LootPool.builder()
								.with(ItemEntry.builder(Items.TRIDENT))
								.build();
						tableBuilder.pool(pool);
					}
				});

	}
}