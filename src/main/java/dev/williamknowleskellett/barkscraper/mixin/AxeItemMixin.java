package dev.williamknowleskellett.barkscraper.mixin;

// BarkScraper Minecraft Mod
// Copyright (C) 2023 William Knowles-Kellett

// This program is free software; you can redistribute it and/or modify
// it under the terms of version 2 of the GNU General Public License as published by
// the Free Software Foundation.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License along
// with this program; if not, write to the Free Software Foundation, Inc.,
// 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

import com.google.common.collect.ImmutableMap;
import dev.williamknowleskellett.barkscraper.BarkBlock;
import dev.williamknowleskellett.barkscraper.BarkBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    private static final ImmutableMap<Block, Pair<BarkBlock, Integer>> BARK_BLOCKS = new ImmutableMap.Builder<Block, Pair<BarkBlock, Integer>>().put(Blocks.OAK_WOOD, new Pair<BarkBlock, Integer>(BarkBlocks.OAK_BARK, 6))
                                                                                                   .put(Blocks.OAK_LOG, new Pair<BarkBlock, Integer>(BarkBlocks.OAK_BARK, 4))
                                                                                                   .put(Blocks.DARK_OAK_WOOD, new Pair<BarkBlock, Integer>(BarkBlocks.DARK_OAK_BARK, 6))
                                                                                                   .put(Blocks.DARK_OAK_LOG, new Pair<BarkBlock, Integer>(BarkBlocks.DARK_OAK_BARK, 4))
                                                                                                   .put(Blocks.ACACIA_WOOD, new Pair<BarkBlock, Integer>(BarkBlocks.ACACIA_BARK, 6))
                                                                                                   .put(Blocks.ACACIA_LOG, new Pair<BarkBlock, Integer>(BarkBlocks.ACACIA_BARK, 4))
                                                                                                   .put(Blocks.BIRCH_WOOD, new Pair<BarkBlock, Integer>(BarkBlocks.BIRCH_BARK, 6))
                                                                                                   .put(Blocks.BIRCH_LOG, new Pair<BarkBlock, Integer>(BarkBlocks.BIRCH_BARK, 4))
                                                                                                   .put(Blocks.JUNGLE_WOOD, new Pair<BarkBlock, Integer>(BarkBlocks.JUNGLE_BARK, 6))
                                                                                                   .put(Blocks.JUNGLE_LOG, new Pair<BarkBlock, Integer>(BarkBlocks.JUNGLE_BARK, 4))
                                                                                                   .put(Blocks.SPRUCE_WOOD, new Pair<BarkBlock, Integer>(BarkBlocks.SPRUCE_BARK, 6))
                                                                                                   .put(Blocks.SPRUCE_LOG, new Pair<BarkBlock, Integer>(BarkBlocks.SPRUCE_BARK, 4))
                                                                                                   .put(Blocks.WARPED_STEM, new Pair<BarkBlock, Integer>(BarkBlocks.WARPED_BARK, 4))
                                                                                                   .put(Blocks.WARPED_HYPHAE, new Pair<BarkBlock, Integer>(BarkBlocks.WARPED_BARK, 6))
                                                                                                   .put(Blocks.CRIMSON_STEM, new Pair<BarkBlock, Integer>(BarkBlocks.CRIMSON_BARK, 4))
                                                                                                   .put(Blocks.CRIMSON_HYPHAE, new Pair<BarkBlock, Integer>(BarkBlocks.CRIMSON_BARK, 6)).build();

    @Inject(method = "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V",
                    ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD)
//    @Inject(method = "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;",
//            at = @At("HEAD"),
//            locals = LocalCapture.CAPTURE_FAILHARD)
    public void useOnBlockMixin(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir, World world, BlockPos pos, PlayerEntity playerEntity, BlockState blockState) {
        if (!world.isClient) {
            Pair<BarkBlock, Integer> pair = BARK_BLOCKS.get(blockState.getBlock());
            Block block = pair.getLeft();
            int count = pair.getRight();

            Block.dropStack(world, pos, new ItemStack(block, count));
        }

//        world.spawnEntity(new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, new ItemStack(Items.DIAMOND)));
    }
}
