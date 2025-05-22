package net.space333.enchants.enchantment.custom;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.space333.enchants.Enchants;
import net.space333.enchants.enchantment.ModEnchantments;
import net.space333.enchants.util.ModEnchantmentHelper;

import java.util.ArrayList;
import java.util.List;

public class Excavator {

    public static List<BlockPos> getBlocksToBeDestroyed(int level, BlockPos initalBlockPos, ServerPlayerEntity player) {
        List<BlockPos> positions = new ArrayList<>();
        HitResult hit = player.raycast(20, 0, false);
        Direction playerFacing = player.getHorizontalFacing();

        Enchants.LOGGER.info("facing: " + playerFacing);
        if (hit.getType() == HitResult.Type.BLOCK && level <= 3) {
            BlockHitResult blockHit = (BlockHitResult) hit;

            if(blockHit.getSide() == Direction.DOWN || blockHit.getSide() == Direction.UP) {
                if(level == 1) {
                    getBlockLine(positions, initalBlockPos, playerFacing);
                }
                else if(level == 2) {
                    getBlockLine(positions, initalBlockPos, playerFacing);
                    if(playerFacing == Direction.NORTH || playerFacing == Direction.SOUTH) {
                        positions.add(new BlockPos(initalBlockPos.getX() - 1, initalBlockPos.getY(), initalBlockPos.getZ()));
                        positions.add(new BlockPos(initalBlockPos.getX() + 1, initalBlockPos.getY(), initalBlockPos.getZ()));
                    }
                    else if(playerFacing == Direction.EAST || playerFacing == Direction.WEST) {
                        positions.add(new BlockPos(initalBlockPos.getX(), initalBlockPos.getY(), initalBlockPos.getZ() - 1));
                        positions.add(new BlockPos(initalBlockPos.getX(), initalBlockPos.getY(), initalBlockPos.getZ() + 1));
                    }

                }
                else if(level == 3) {
                    for(int x = -1; x <= 1; x++) {
                        getBlockLine(positions, initalBlockPos.add(x,0,0), playerFacing);
                    }
                }

            }

            if(blockHit.getSide() == Direction.NORTH || blockHit.getSide() == Direction.SOUTH) {
                if(level == 1) {
                    getBlockLine(positions, initalBlockPos, Direction.UP);
                }
                else if(level == 2) {
                    getBlockLine(positions, initalBlockPos, Direction.UP);
                    positions.add(new BlockPos(initalBlockPos.getX() - 1, initalBlockPos.getY(), initalBlockPos.getZ()));
                    positions.add(new BlockPos(initalBlockPos.getX() + 1, initalBlockPos.getY(), initalBlockPos.getZ()));
                }
                else if(level == 3) {
                    for(int x = -1; x <= 1; x++) {
                        getBlockLine(positions, initalBlockPos.add(x,0,0), Direction.UP);
                    }
                }

            }

            if(blockHit.getSide() == Direction.EAST || blockHit.getSide() == Direction.WEST) {
                if(level == 1) {
                    getBlockLine(positions, initalBlockPos, Direction.UP);
                }
                else if(level == 2) {
                    getBlockLine(positions, initalBlockPos, Direction.UP);
                    positions.add(new BlockPos(initalBlockPos.getX(), initalBlockPos.getY(), initalBlockPos.getZ() - 1));
                    positions.add(new BlockPos(initalBlockPos.getX(), initalBlockPos.getY(), initalBlockPos.getZ() + 1));
                }
                else if(level == 3) {
                    for(int z = -1; z <= 1; z++) {
                        getBlockLine(positions, initalBlockPos.add(0,0,z), Direction.UP);
                    }
                }
            }
        }
        else {
            positions.add(initalBlockPos);
        }

        return positions;
    }

    private static void getBlockLine(List<BlockPos> positions, BlockPos initalBlockPos, Direction direction) {
        if(direction == Direction.UP || direction == Direction.DOWN) {
            for(int y = -1; y <= 1; y++) {
                positions.add(new BlockPos(initalBlockPos.getX(), initalBlockPos.getY() + y, initalBlockPos.getZ()));
            }
        }
        else if(direction == Direction.NORTH || direction == Direction.SOUTH) {
            for(int z = -1; z <= 1; z++) {
                positions.add(new BlockPos(initalBlockPos.getX(), initalBlockPos.getY(), initalBlockPos.getZ() + z));
            }
        }
        else if(direction == Direction.EAST || direction == Direction.WEST) {
            for(int x = -1; x <= 1; x++) {
                positions.add(new BlockPos(initalBlockPos.getX() + x, initalBlockPos.getY(), initalBlockPos.getZ()));
            }
        }
    }

    public static boolean hasExcavator(ItemStack itemStack) {
        return ModEnchantmentHelper.hasEnchantment(itemStack, ModEnchantments.EXCAVATOR);
    }

    public static int getExcavatorLevel(ItemStack itemStack) {
        return ModEnchantmentHelper.getEnchantmentLevel(itemStack, ModEnchantments.EXCAVATOR);
    }


}
