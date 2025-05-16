package net.space333.enchants;

import net.fabricmc.api.ModInitializer;

import net.minecraft.recipe.RecipePropertySet;
import net.space333.enchants.Component.ModDataComponentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Enchants implements ModInitializer {
	public static final String MOD_ID = "enchants";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModDataComponentType.registerDataComponentTypes();




	}
}