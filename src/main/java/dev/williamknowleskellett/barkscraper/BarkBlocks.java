package dev.williamknowleskellett.barkscraper;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class BarkBlocks {
    public static final BarkBlock OAK_BARK = new BarkBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.OAK_TAN).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    public static final BarkBlock SPRUCE_BARK = new BarkBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.SPRUCE_BROWN).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    public static final BarkBlock BIRCH_BARK = new BarkBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.PALE_YELLOW).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    public static final BarkBlock JUNGLE_BARK = new BarkBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.DIRT_BROWN).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    public static final BarkBlock ACACIA_BARK = new BarkBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.GRAY).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    public static final BarkBlock DARK_OAK_BARK = new BarkBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.BROWN).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    public static final BarkBlock WARPED_BARK = new BarkBlock(AbstractBlock.Settings.of(Material.NETHER_WOOD, MapColor.DARK_DULL_PINK).strength(2.0f).sounds(BlockSoundGroup.NETHER_STEM));
    public static final BarkBlock CRIMSON_BARK = new BarkBlock(AbstractBlock.Settings.of(Material.NETHER_WOOD, MapColor.DARK_CRIMSON).strength(2.0f).sounds(BlockSoundGroup.NETHER_STEM));
}
