package net.space333.enchants.mixin.enchantments.disableEnchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.*;
import net.minecraft.util.math.random.Random;
import net.space333.enchants.Enchants;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin extends ScreenHandler {

    @Shadow @Final private ScreenHandlerContext context;
    @Shadow @Final private Inventory inventory;
    @Shadow @Final public int[] enchantmentPower;
    @Shadow @Final public int[] enchantmentId;
    @Shadow @Final public int[] enchantmentLevel;
    @Shadow @Final private Property seed;
    @Shadow @Final private Random random;

    protected EnchantmentScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }



    @Inject(method = "generateEnchantments", at = @At(value = "HEAD"), cancellable = true)
    private void getEnchantment(DynamicRegistryManager registryManager, ItemStack stack, int slot, int level, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        this.random.setSeed(this.seed.get() + slot);
        Optional<RegistryEntryList.Named<Enchantment>> optional = registryManager.getOrThrow(RegistryKeys.ENCHANTMENT)
                .getOptional(EnchantmentTags.IN_ENCHANTING_TABLE);
        if (optional.isEmpty()) {
            cir.setReturnValue(List.of());
        } else {
            List<EnchantmentLevelEntry> list = EnchantmentHelper.generateEnchantments(this.random, stack, level, ((RegistryEntryList.Named)optional.get()).stream());
            int powerOffset;
            for(powerOffset = 0; powerOffset < 30 && list.isEmpty(); powerOffset++) {
                list = EnchantmentHelper.generateEnchantments(this.random, stack, level + powerOffset, ((RegistryEntryList.Named)optional.get()).stream());
            }

            if (stack.isOf(Items.BOOK) && list.size() > 1) {
                list.remove(this.random.nextInt(list.size()));
            }

            cir.setReturnValue(list);
        }
    }

    /*
    @Inject(method = "onContentChanged", at = @At(value = "HEAD"), cancellable = true)
    private void getEnchantment(Inventory inventory, CallbackInfo ci) {
        if (inventory == this.inventory) {
            ItemStack itemStack = inventory.getStack(0);
            if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
                this.context.run((world, pos) -> {
                    IndexedIterable<RegistryEntry<Enchantment>> indexedIterable = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getIndexedEntries();
                    int ix = 0;

                    for (BlockPos blockPos : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
                        if (EnchantingTableBlock.canAccessPowerProvider(world, pos, blockPos)) {
                            ix++;
                        }
                    }

                    this.random.setSeed(this.seed.get());

                    for (int j = 0; j < 3; j++) {
                        this.enchantmentPower[j] = EnchantmentHelper.calculateRequiredExperienceLevel(this.random, j, ix, itemStack);
                        this.enchantmentId[j] = -1;
                        this.enchantmentLevel[j] = -1;
                        if (this.enchantmentPower[j] < j + 1) {
                            this.enchantmentPower[j] = 0;
                        }
                    }

                    for (int jx = 0; jx < 3; jx++) {
                        if (this.enchantmentPower[jx] > 0) {
                            List<EnchantmentLevelEntry> list = this.generateEnchantments(world.getRegistryManager(), itemStack, jx, this.enchantmentPower[jx]);
                            int powerOffset = 0;
                            for(powerOffset = 0; powerOffset < 30 && list.isEmpty(); powerOffset++) {
                                list = this.generateEnchantments(world.getRegistryManager(), itemStack, jx, this.enchantmentPower[jx] + powerOffset);
                            }

                            if (list != null && !list.isEmpty()) {
                                EnchantmentLevelEntry enchantmentLevelEntry = (EnchantmentLevelEntry)list.get(this.random.nextInt(list.size()));
                                this.enchantmentId[jx] = indexedIterable.getRawId(enchantmentLevelEntry.enchantment());
                                this.enchantmentLevel[jx] = enchantmentLevelEntry.level();
                            }
                        }
                    }

                    this.sendContentUpdates();
                });
            } else {
                for (int i = 0; i < 3; i++) {
                    this.enchantmentPower[i] = 0;
                    this.enchantmentId[i] = -1;
                    this.enchantmentLevel[i] = -1;
                }
            }
        }
        ci.cancel();
    }

     */

}