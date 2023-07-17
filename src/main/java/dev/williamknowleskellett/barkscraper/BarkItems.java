package dev.williamknowleskellett.barkscraper;

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

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class BarkItems {
    public static final Item OAK_BARK = new BlockItem(BarkBlocks.OAK_BARK, new Item.Settings().group(ItemGroup.DECORATIONS));
    public static final Item SPRUCE_BARK = new BlockItem(BarkBlocks.SPRUCE_BARK, new Item.Settings().group(ItemGroup.DECORATIONS));
    public static final Item BIRCH_BARK = new BlockItem(BarkBlocks.BIRCH_BARK, new Item.Settings().group(ItemGroup.DECORATIONS));
    public static final Item JUNGLE_BARK = new BlockItem(BarkBlocks.JUNGLE_BARK, new Item.Settings().group(ItemGroup.DECORATIONS));
    public static final Item ACACIA_BARK = new BlockItem(BarkBlocks.ACACIA_BARK, new Item.Settings().group(ItemGroup.DECORATIONS));
    public static final Item DARK_OAK_BARK = new BlockItem(BarkBlocks.DARK_OAK_BARK, new Item.Settings().group(ItemGroup.DECORATIONS));
    public static final Item WARPED_BARK = new BlockItem(BarkBlocks.WARPED_BARK, new Item.Settings().group(ItemGroup.DECORATIONS));
    public static final Item CRIMSON_BARK = new BlockItem(BarkBlocks.CRIMSON_BARK, new Item.Settings().group(ItemGroup.DECORATIONS));
}
