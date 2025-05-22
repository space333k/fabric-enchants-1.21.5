package net.space333.enchants.mixin.anvil;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldEvents;
import net.space333.enchants.Enchants;
import net.space333.enchants.util.EnchantmentPowerHelper;
import net.space333.enchants.util.ModTags;
import net.space333.enchants.util.Upgrade;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
    @Shadow @Final
    private Property levelCost;
    @Shadow
    private int repairItemUsage;
    @Shadow
    private @Nullable String newItemName;

    @Unique
    private static int repairFraction = 3;
    @Unique
    private static int combineBonus = 12;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ForgingSlotsManager forgingSlotsManager) {
        super(type, syncId, playerInventory, context, forgingSlotsManager);
    }

    @Inject(method = "onTakeOutput", at = @At(value = "HEAD"), cancellable = true)
    private void IncreaseUnbreaking(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        if (!player.isInCreativeMode()) {
            player.addExperienceLevels(-this.levelCost.get());
        }

        if (this.repairItemUsage > 0) {
            ItemStack itemStack = this.input.getStack(1);
            if (!itemStack.isEmpty() && itemStack.getCount() > this.repairItemUsage) {
                itemStack.decrement(this.repairItemUsage);
                this.input.setStack(1, itemStack);
            } else {
                this.input.setStack(1, ItemStack.EMPTY);
            }
        }
        else {
            this.input.setStack(1, ItemStack.EMPTY);
        }

        this.levelCost.set(0);
        this.input.setStack(0, ItemStack.EMPTY);
        this.context.run((world, pos) -> {
            BlockState blockState = world.getBlockState(pos);
            if (!player.isInCreativeMode() && blockState.isIn(BlockTags.ANVIL) && player.getRandom().nextFloat() < 0.12F) {
                BlockState blockState2 = AnvilBlock.getLandingState(blockState);
                if (blockState2 == null) {
                    world.removeBlock(pos, false);
                    world.syncWorldEvent(WorldEvents.ANVIL_DESTROYED, pos, 0);
                } else {
                    world.setBlockState(pos, blockState2, Block.NOTIFY_LISTENERS);
                    world.syncWorldEvent(WorldEvents.ANVIL_USED, pos, 0);
                }
            } else {
                world.syncWorldEvent(WorldEvents.ANVIL_USED, pos, 0);
            }
        });
        ci.cancel();
    }

    @Inject(method = "updateResult", at = @At(value = "HEAD"), cancellable = true)
    private void calculateCost(CallbackInfo ci) {
        ItemStack itemStack1 = this.input.getStack(0);
        ItemStack itemStack2 = this.input.getStack(1);
        ItemStack outStack = itemStack1.copy();

        boolean validOutput = false;

        this.levelCost.set(1);
        int repairCost = 0;

        if (!itemStack1.isEmpty() && EnchantmentHelper.canHaveEnchantments(itemStack1)) {
            this.repairItemUsage = 0;

            if (!itemStack2.isEmpty()) {

                if (itemStack1.isDamageable()) {
                    int durability = itemStack1.getMaxDamage();
                    int repairIteration = 0;
                    int upgradeIteration = 0;

                    if(itemStack2.isIn(ModTags.Items.UPGRADE_MATERIAL)) {
                        boolean canUpgraded = true;
                        while (canUpgraded && upgradeIteration < itemStack2.getCount()) {
                            canUpgraded = Upgrade.changeAttribute(itemStack1, itemStack2, outStack);
                            if(canUpgraded) {
                                upgradeIteration++;
                                validOutput = true;
                            }
                        }
                    }

                    if(itemStack1.canRepairWith(itemStack2)) {
                        int repairValue = Math.min(itemStack1.getDamage(), durability / repairFraction);

                        while (repairValue > 0 && repairIteration < itemStack2.getCount()) {
                            int newDamage = outStack.getDamage() - repairValue;
                            outStack.setDamage(newDamage);

                            repairValue = Math.min(outStack.getDamage(), durability / repairFraction);

                            Upgrade.incrementUnbreaking(outStack);
                            repairIteration++;
                            validOutput = true;
                        }

                        while (!Upgrade.isMaxUnbreaking(outStack) && repairIteration < itemStack2.getCount()) {
                            Upgrade.incrementUnbreaking(outStack);
                            repairIteration++;
                            validOutput = true;

                        }
                    }

                    this.repairItemUsage = Math.max(repairIteration, upgradeIteration);

                    if (itemStack1.isOf(itemStack2.getItem())) {
                        int currentDurability1 = durability - itemStack1.getDamage();
                        int currentDurability2 = durability - itemStack2.getDamage();
                        int bonus = durability * combineBonus / 100;
                        int newDurability = currentDurability2 + currentDurability1 + bonus;
                        int newDamage = durability - newDurability;
                        if (newDamage < 0) {
                            newDamage = 0;
                        }

                        if (newDamage < itemStack1.getDamage()) {
                            outStack.setDamage(newDamage);
                            combineUpgrades(itemStack1, itemStack2, outStack);
                            validOutput = true;
                        }
                    }
                }

                boolean incompatibleEnchantments;
                incompatibleEnchantments = combineEnchantments(itemStack1, itemStack2, outStack);

                if (incompatibleEnchantments) {
                    this.output.setStack(0, ItemStack.EMPTY);
                    this.levelCost.set(0);
                    return;
                }
                else if(itemStack2.contains(DataComponentTypes.STORED_ENCHANTMENTS) || itemStack1.isOf(itemStack2.getItem())){
                    validOutput = true;
                }
            }

            if (this.newItemName != null && !StringHelper.isBlank(this.newItemName)) {
                if (!this.newItemName.equals(itemStack1.getName().getString())) {
                    outStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.newItemName));
                    validOutput = true;
                }
            } else if (itemStack1.contains(DataComponentTypes.CUSTOM_NAME)) {
                outStack.remove(DataComponentTypes.CUSTOM_NAME);
                validOutput = true;
            }
            repairCost = calculateCost(outStack);
            repairCost = (int) MathHelper.clamp(repairCost, 0L, 2147483647L);
            this.levelCost.set(repairCost);

            if (!validOutput || repairCost == 0) {
                outStack = ItemStack.EMPTY;
            }

            this.output.setStack(0, outStack);
            this.sendContentUpdates();
        } else {
            this.output.setStack(0, ItemStack.EMPTY);
            this.levelCost.set(0);
        }
        ci.cancel();
    }

    @Unique
    private Boolean combineEnchantments(ItemStack itemStack1, ItemStack itemStack2, ItemStack outStack) {
        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(itemStack1));
        ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(itemStack2);

        boolean bl2 = false;
        boolean bl3 = false;

        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> enchantmentEntry2 : itemEnchantmentsComponent.getEnchantmentEntries()) {
            RegistryEntry<Enchantment> enchantmentRegistry2 = (RegistryEntry<Enchantment>)enchantmentEntry2.getKey();
            int level1 = builder.getLevel(enchantmentRegistry2);
            int level2 = enchantmentEntry2.getIntValue();
            int levelOut = level1 == level2 ? level2 + 1 : Math.max(level2, level1);
            Enchantment enchantment2 = enchantmentRegistry2.value();

            boolean validEnchantment = enchantment2.isAcceptableItem(itemStack1);
            if (this.player.isInCreativeMode() || itemStack1.isOf(Items.ENCHANTED_BOOK)) {
                validEnchantment = true;
            }

            for (RegistryEntry<Enchantment> enchantmentRegistry1 : builder.getEnchantments()) {
                if (!(enchantmentRegistry1.equals(enchantmentRegistry2) || Enchantment.canBeCombined(enchantmentRegistry2, enchantmentRegistry1))) {
                    validEnchantment = false;
                }
            }

            if (!validEnchantment) {
                bl3 = true;
            } else {
                bl2 = true;
                if (levelOut > enchantment2.getMaxLevel()) {
                    levelOut = enchantment2.getMaxLevel();
                }

                builder.set(enchantmentRegistry2, levelOut);
            }
        }

        EnchantmentHelper.set(outStack, builder.build());

        return bl3 && !bl2;
    }

    @Unique
    private void combineUpgrades(ItemStack itemStack1, ItemStack itemStack2, ItemStack outStack) {
        int unbreaking1 = Upgrade.getUnbreaking(itemStack1);
        int unbreaking2 = Upgrade.getUnbreaking(itemStack2);
        int unbreakingOut = Math.max(unbreaking1, unbreaking2) + 1;
        Upgrade.setUnbreaking(outStack, unbreakingOut);

        int efficiency1 = Upgrade.getEfficiency(itemStack1);
        int efficiency2 = Upgrade.getEfficiency(itemStack2);
        int efficiencyOut = Math.max(efficiency1, efficiency2);
        Upgrade.setEfficiency(outStack, efficiencyOut);

        int sharpness1 = Upgrade.getSharpness(itemStack1);
        int sharpness2 = Upgrade.getSharpness(itemStack2);
        int sharpnessOut = Math.max(sharpness1, sharpness2);
        Upgrade.setSharpness(outStack, sharpnessOut);

        int protection1 = Upgrade.getProtection(itemStack1);
        int protection2 = Upgrade.getProtection(itemStack2);
        int protectionOut = Math.max(protection1, protection2);
        Upgrade.setProtection(outStack, protectionOut);
    }

    @Unique
    private int calculateCost(ItemStack outStack) {
        int upgradeCost = Upgrade.getUnbreaking(outStack);
        upgradeCost += Upgrade.getEfficiency(outStack);
        upgradeCost += Upgrade.getSharpness(outStack);
        upgradeCost += Upgrade.getProtection(outStack);

        int enchantmentCost = 0;
        int specialEnchantmentCost = 0;

        ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(outStack);
        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
            RegistryEntry<Enchantment> enchantmentRegistry = (RegistryEntry<Enchantment>)entry.getKey();
            Enchantment enchantment = enchantmentRegistry.value();
            if(enchantment.getMaxLevel() > 1) {
                enchantmentCost += entry.getIntValue();
            }
            else {
                specialEnchantmentCost++;
            }
        }
        if(enchantmentCost > EnchantmentPowerHelper.getMaxEnchantment(outStack)) {
            return 0;
        }

        return upgradeCost + enchantmentCost * 2 + specialEnchantmentCost * 4 + 1;
    }
}




