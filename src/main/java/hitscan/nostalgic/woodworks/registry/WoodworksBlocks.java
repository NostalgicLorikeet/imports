package hitscan.nostalgic.woodworks.registry;

import hitscan.nostalgic.woodworks.Woodworks;
import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.blocks.BlockGlyphDummy;
import hitscan.nostalgic.woodworks.blocks.BlockPodium;
import hitscan.nostalgic.woodworks.blocks.BlockShelf;
import hitscan.nostalgic.woodworks.blocks.BlockGlyphHolder;
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
    static ArrayList<BlockGlyphHolder> GLYPH_HOLDERS = new ArrayList<>();
    static ArrayList<BlockGlyphDummy> GLYPH_HOLDER_DUMMIES = new ArrayList<>();

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
    public static final BlockGlyphHolder GLYPH_HOLDER_2X2 = new BlockGlyphHolder(false, false);
    public static final BlockGlyphHolder GLYPH_HOLDER_INVERTED_2X2 = new BlockGlyphHolder(true, false);
    public static final BlockGlyphHolder GLYPH_HOLDER_4X4 = new BlockGlyphHolder(false, true);
    public static final BlockGlyphHolder GLYPH_HOLDER_INVERTED_4X4 = new BlockGlyphHolder(true, true);
    public static final BlockGlyphDummy GLYPH_2X2 = new BlockGlyphDummy("glyph_dummy_2x2");
    public static final BlockGlyphDummy GLYPH_4X4 = new BlockGlyphDummy("glyph_dummy_4x4");

    @SubscribeEvent
    public static void blocks(RegistryEvent.Register<Block> event) {
        BLOCKS.add(SHELF_SHORT);
        BLOCKS.add(SHELF_LONG);
        BLOCKS.add(SHELF_DISPLAY);
        BLOCKS.add(SHELF_DISPLAY_CHAINED);
        BLOCKS.add(SHELF_DISPLAY_HANGING);
        BLOCKS.add(PODIUM);
        BLOCKS.add(PODIUM_TROPHY);
        BLOCKS.add(GLYPH_HOLDER_2X2);
        BLOCKS.add(GLYPH_HOLDER_INVERTED_2X2);
        BLOCKS.add(GLYPH_HOLDER_4X4);
        BLOCKS.add(GLYPH_HOLDER_INVERTED_4X4);

        SHELVES.add(SHELF_SHORT);
        SHELVES.add(SHELF_LONG);
        SHELVES.add(SHELF_DISPLAY);
        SHELVES.add(SHELF_DISPLAY_CHAINED);
        SHELVES.add(SHELF_DISPLAY_HANGING);

        PODIUMS.add(PODIUM);
        PODIUMS.add(PODIUM_TROPHY);

        GLYPH_HOLDERS.add(GLYPH_HOLDER_2X2);
        GLYPH_HOLDERS.add(GLYPH_HOLDER_INVERTED_2X2);
        GLYPH_HOLDERS.add(GLYPH_HOLDER_4X4);
        GLYPH_HOLDERS.add(GLYPH_HOLDER_INVERTED_4X4);

        for (Block block : BLOCKS) {
            block.setCreativeTab(Woodworks.CREATIVE_TAB);
            event.getRegistry().register(block);
        }

        event.getRegistry().register(GLYPH_2X2);
        event.getRegistry().register(GLYPH_4X4);

        GLYPH_HOLDER_DUMMIES.add(GLYPH_2X2);
        GLYPH_HOLDER_DUMMIES.add(GLYPH_4X4);
    }
}