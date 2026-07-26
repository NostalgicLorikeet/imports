package hitscan.nostalgic.woodworks.registry;

import hitscan.nostalgic.woodworks.Woodworks;
import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.blocks.BlockLetterBoard;
import hitscan.nostalgic.woodworks.blocks.BlockNeonGlyph;
import hitscan.nostalgic.woodworks.blocks.BlockPodium;
import hitscan.nostalgic.woodworks.blocks.BlockShelf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class WoodworksBlocks {
    static ArrayList<Block> BLOCKS = new ArrayList<>();
    static ArrayList<BlockShelf> SHELVES = new ArrayList<>();
    static ArrayList<BlockPodium> PODIUMS = new ArrayList<>();
    static ArrayList<BlockNeonGlyph> NEON_GLYPHS = new ArrayList<>();

    public static final BlockShelf SHELF_SHORT = new BlockShelf(
            "short_shelf",
            Material.WOOD,
            false,
            false,
            true,
            false,
            new AxisAlignedBB(0,7/8D,0,1,1, 0.5D),
            new AxisAlignedBB(0,0.5,0,1,1, 0.5D)
    );
    public static final BlockShelf SHELF_LONG = new BlockShelf(
            "long_shelf",
            Material.WOOD,
            true,
            false,
            true,
            false,
            new AxisAlignedBB(0,7/8D,0,1,1, 0.75D),
            new AxisAlignedBB(0,0.5,0,1,1, 0.75D)
    );
    public static final BlockShelf SHELF_DISPLAY = new BlockShelf(
            "display_shelf",
            Material.WOOD,
            false,
            true,
            false,
            false,
            new AxisAlignedBB(0,3/8D,0,1,0.5D, 3/8D),
            new AxisAlignedBB(0,1/8D,0,1,0.5D, 3/8D)
    );
    public static final BlockShelf SHELF_DISPLAY_CHAINED = new BlockShelf(
            "chained_display_shelf",
            Material.WOOD,
            false,
            true,
            false,
            false,
            new AxisAlignedBB(0,3/8D,0,1,0.5D, 0.5D),
            new AxisAlignedBB(0,4/8D,0,1,3/8D, 0.5D)
    );
    public static final BlockShelf SHELF_DISPLAY_HANGING = new BlockShelf(
            "hanging_display_shelf",
            Material.WOOD,
            false,
            true,
            false,
            true,
            new AxisAlignedBB(0,1/2D,5/16D,1,3/8D, 11/16D),
            new AxisAlignedBB(0,1/2D,5/16D,1,3/8D, 11/16D)
    );
    public static final BlockPodium PODIUM = new BlockPodium("podium");
    public static final BlockPodium PODIUM_TROPHY = new BlockPodium("podium_trophy");
    public static final BlockLetterBoard LETTER_BOARD = new BlockLetterBoard(false);
    public static final BlockLetterBoard LETTER_BOARD_INVERTED = new BlockLetterBoard(!false);
    public static final BlockNeonGlyph NEON_GLYPH_BLOCK = new BlockNeonGlyph(false);
    public static final BlockNeonGlyph NEON_GLYPH_BLOCK_INVERTED = new BlockNeonGlyph(true);

    @SubscribeEvent
    public static void blocks(RegistryEvent.Register<Block> event) {
        BLOCKS.add(SHELF_SHORT);
        BLOCKS.add(SHELF_LONG);
        BLOCKS.add(SHELF_DISPLAY);
        BLOCKS.add(SHELF_DISPLAY_CHAINED);
        BLOCKS.add(SHELF_DISPLAY_HANGING);
        BLOCKS.add(PODIUM);
        BLOCKS.add(PODIUM_TROPHY);
        BLOCKS.add(LETTER_BOARD);
        BLOCKS.add(LETTER_BOARD_INVERTED);
        BLOCKS.add(NEON_GLYPH_BLOCK);
        BLOCKS.add(NEON_GLYPH_BLOCK_INVERTED);

        SHELVES.add(SHELF_SHORT);
        SHELVES.add(SHELF_LONG);
        SHELVES.add(SHELF_DISPLAY);
        SHELVES.add(SHELF_DISPLAY_CHAINED);
        SHELVES.add(SHELF_DISPLAY_HANGING);

        PODIUMS.add(PODIUM);
        PODIUMS.add(PODIUM_TROPHY);

        NEON_GLYPHS.add(NEON_GLYPH_BLOCK);
        NEON_GLYPHS.add(NEON_GLYPH_BLOCK_INVERTED);

        for (Block block : BLOCKS) {
            block.setCreativeTab(Woodworks.CREATIVE_TAB);
            event.getRegistry().register(block);
        }
    }
}