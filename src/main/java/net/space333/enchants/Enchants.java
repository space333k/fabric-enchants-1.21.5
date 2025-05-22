package net.space333.enchants;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.space333.enchants.Component.ModDataComponentType;
import net.space333.enchants.enchantment.custom.SoulBound;
import net.space333.enchants.util.ExcavatorUsageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Enchants implements ModInitializer {
	public static final String MOD_ID = "enchants";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModDataComponentType.registerDataComponentTypes();

		ServerPlayerEvents.COPY_FROM.register(SoulBound::copySoulBoundItems);
		PlayerBlockBreakEvents.BEFORE.register(new ExcavatorUsageEvent());

	}
}