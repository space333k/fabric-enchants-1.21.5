package net.space333.enchants.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.space333.enchants.Enchants;

public class ModItemGroups {
    public static final ItemGroup TOME_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Enchants.MOD_ID, "tome_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.DUPLICATION_TOME))
                    .displayName(Text.translatable("itemgroup.enchants.tome_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.DUPLICATION_TOME);
                    }).build());


    public static void registerItemGroups() {
        Enchants.LOGGER.info("Registering Item Groups for " + Enchants.MOD_ID);
    }

}
