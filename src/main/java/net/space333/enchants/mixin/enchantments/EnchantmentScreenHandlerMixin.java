package net.space333.enchants.mixin.enchantments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Optional;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin {

    @Shadow @Final private ScreenHandlerContext context;
    @Shadow @Final private Random random;

    @SuppressWarnings("unchecked")
    @ModifyExpressionValue(
            method = "onContentChanged",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isEnchantable()Z"
            )
    )
    private boolean dontUpdateIfNoEnchantmentsAvailable(boolean original, @Local ItemStack itemStack) {
        final boolean[] con = {true};
        context.run((level, pos) -> {
            Optional<RegistryEntryList.Named<Enchantment>> possibleEnchantments =
                        level.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOptional(EnchantmentTags.IN_ENCHANTING_TABLE);
            List<EnchantmentLevelEntry> list = EnchantmentHelper.getPossibleEntries(
                    EnchantmentHelper.calculateRequiredExperienceLevel(random, 2, 15, itemStack),
                    itemStack,
                    ((RegistryEntryList.Named)possibleEnchantments.get()).stream()
            );
            if (list.isEmpty()) con[0] = false;
        });
        return original && con[0];
    }
}