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

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.registry.Registry;

public class BarkScraper implements ModInitializer  {

    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, "barkscraper:oak_bark", BarkBlocks.OAK_BARK);
        Registry.register(Registry.BLOCK, "barkscraper:spruce_bark", BarkBlocks.SPRUCE_BARK);
        Registry.register(Registry.BLOCK, "barkscraper:birch_bark", BarkBlocks.BIRCH_BARK);
        Registry.register(Registry.BLOCK, "barkscraper:jungle_bark", BarkBlocks.JUNGLE_BARK);
        Registry.register(Registry.BLOCK, "barkscraper:acacia_bark", BarkBlocks.ACACIA_BARK);
        Registry.register(Registry.BLOCK, "barkscraper:dark_oak_bark", BarkBlocks.DARK_OAK_BARK);
        Registry.register(Registry.BLOCK, "barkscraper:warped_bark", BarkBlocks.WARPED_BARK);
        Registry.register(Registry.BLOCK, "barkscraper:crimson_bark", BarkBlocks.CRIMSON_BARK);

        Registry.register(Registry.ITEM, "barkscraper:oak_bark", BarkItems.OAK_BARK);
        Registry.register(Registry.ITEM, "barkscraper:spruce_bark", BarkItems.SPRUCE_BARK);
        Registry.register(Registry.ITEM, "barkscraper:birch_bark", BarkItems.BIRCH_BARK);
        Registry.register(Registry.ITEM, "barkscraper:jungle_bark", BarkItems.JUNGLE_BARK);
        Registry.register(Registry.ITEM, "barkscraper:acacia_bark", BarkItems.ACACIA_BARK);
        Registry.register(Registry.ITEM, "barkscraper:dark_oak_bark", BarkItems.DARK_OAK_BARK);
        Registry.register(Registry.ITEM, "barkscraper:warped_bark", BarkItems.WARPED_BARK);
        Registry.register(Registry.ITEM, "barkscraper:crimson_bark", BarkItems.CRIMSON_BARK);
    }
}
