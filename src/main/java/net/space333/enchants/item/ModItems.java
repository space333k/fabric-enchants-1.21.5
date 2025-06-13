package net.space333.enchants.item;

import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.space333.enchants.Enchants;

import java.util.List;

public class ModItems {
    public static final Item DUPLICATION_TOME = registerItem("duplication_tome", new Item((new Item.Settings().maxCount(1)
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Enchants.MOD_ID, "duplication_tome"))))));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Enchants.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Enchants.LOGGER.info("Registering Mod Items for " + Enchants.MOD_ID);
    }
}
