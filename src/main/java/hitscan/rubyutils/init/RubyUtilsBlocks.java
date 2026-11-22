package hitscan.rubyutils.init;

import hitscan.rubyutils.Tags;
import hitscan.rubyutils.RubyUtilsCreativeTabs;
import hitscan.rubyutils.blocks.*;
import hitscan.rubyutils.config.RubyUtilsConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class RubyUtilsBlocks {
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
            new AxisAlignedBB(0,7/8D,0,1,1, 0.5D),
            new AxisAlignedBB(0,0.5,0,1,1, 0.5D)
    );
    public static final BlockShelf SHELF_SHORT_ALL_WOOD = new BlockShelf(
            "short_shelf_all_wood",
            Material.WOOD,
            false,
            false,
            true,
            new AxisAlignedBB(0,7/8D,0,1,1, 0.5D),
            new AxisAlignedBB(0,0.5,0,1,1, 0.5D)
    );
    public static final BlockShelf SHELF_LONG = new BlockShelf(
            "long_shelf",
            Material.WOOD,
            true,
            false,
            true,
            new AxisAlignedBB(0,7/8D,0,1,1, 0.75D),
            new AxisAlignedBB(0,0.5,0,1,1, 0.75D)
    );
    public static final BlockShelf SHELF_LONG_ALL_WOOD = new BlockShelf(
            "long_shelf_all_wood",
            Material.WOOD,
            true,
            false,
            true,
            new AxisAlignedBB(0,7/8D,0,1,1, 0.75D),
            new AxisAlignedBB(0,0.5,0,1,1, 0.75D)
    );
    public static final BlockShelf SHELF_DISPLAY = new BlockShelf(
            "display_shelf",
            Material.WOOD,
            false,
            true,
            false,
            new AxisAlignedBB(0,3/8D,0,1,0.5D, 3/8D),
            new AxisAlignedBB(0,1/8D,0,1,0.5D, 3/8D)
    );
    public static final BlockShelf SHELF_DISPLAY_ALL_WOOD = new BlockShelf(
            "display_shelf_all_wood",
            Material.WOOD,
            false,
            true,
            false,
            new AxisAlignedBB(0,3/8D,0,1,0.5D, 3/8D),
            new AxisAlignedBB(0,1/8D,0,1,0.5D, 3/8D)
    );
    public static final BlockShelf SHELF_DISPLAY_CHAINED = new BlockShelfChained(
            "chained_display_shelf",
            Material.WOOD,
            false,
            true,
            false,
            new AxisAlignedBB(0,3/8D,0,1,0.5D, 0.5D),
            new AxisAlignedBB(0,4/8D,0,1,3/8D, 0.5D),
            new AxisAlignedBB(0,1/2D,5/16D,1,3/8D, 11/16D),
            new AxisAlignedBB(0,1/2D,5/16D,1,3/8D, 11/16D)
    );
    public static final BlockPodium PODIUM = new BlockPodium("podium");
    public static final BlockPodium PODIUM_TROPHY = new BlockPodium("podium_trophy");
    public static final BlockGlyphHolder GLYPH_HOLDER_1X1 = new BlockGlyphHolder(false, 1);
    public static final BlockGlyphHolder GLYPH_HOLDER_INVERTED_1X1 = new BlockGlyphHolder(true, 1);
    public static final BlockGlyphHolder GLYPH_HOLDER_2X2 = new BlockGlyphHolder(false, 2);
    public static final BlockGlyphHolder GLYPH_HOLDER_INVERTED_2X2 = new BlockGlyphHolder(true, 2);
    public static final BlockGlyphHolder GLYPH_HOLDER_4X4 = new BlockGlyphHolder(false, 4);
    public static final BlockGlyphHolder GLYPH_HOLDER_INVERTED_4X4 = new BlockGlyphHolder(true, 4);
    public static final BlockGlyphDummy GLYPH_1X1 = new BlockGlyphDummy("glyph_dummy_1x1");
    public static final BlockGlyphDummy GLYPH_2X2 = new BlockGlyphDummy("glyph_dummy_2x2");
    public static final BlockGlyphDummy GLYPH_4X4 = new BlockGlyphDummy("glyph_dummy_4x4");
    public static final BlockCrateWooden CRATE_WOODEN = new BlockCrateWooden();

    @SubscribeEvent
    public static void blocks(RegistryEvent.Register<Block> event) {
        if (RubyUtilsConfig.shelfOptions.enableShelves) {
            BLOCKS.add(SHELF_SHORT);
            BLOCKS.add(SHELF_LONG);
            BLOCKS.add(SHELF_DISPLAY);
            BLOCKS.add(SHELF_SHORT_ALL_WOOD);
            BLOCKS.add(SHELF_LONG_ALL_WOOD);
            BLOCKS.add(SHELF_DISPLAY_ALL_WOOD);
            BLOCKS.add(SHELF_DISPLAY_CHAINED);

            SHELVES.add(SHELF_SHORT);
            SHELVES.add(SHELF_LONG);
            SHELVES.add(SHELF_DISPLAY);
            SHELVES.add(SHELF_SHORT_ALL_WOOD);
            SHELVES.add(SHELF_LONG_ALL_WOOD);
            SHELVES.add(SHELF_DISPLAY_ALL_WOOD);
            SHELVES.add(SHELF_DISPLAY_CHAINED);
        }

        if (RubyUtilsConfig.podiumOptions.enablePodiums) {
            BLOCKS.add(PODIUM);
            BLOCKS.add(PODIUM_TROPHY);

            PODIUMS.add(PODIUM);
            PODIUMS.add(PODIUM_TROPHY);
        }

        if (RubyUtilsConfig.neonGlyphHolderOptions.enableNeonGlyphHolders) {
            BLOCKS.add(GLYPH_HOLDER_1X1);
            BLOCKS.add(GLYPH_HOLDER_INVERTED_1X1);
            BLOCKS.add(GLYPH_HOLDER_2X2);
            BLOCKS.add(GLYPH_HOLDER_INVERTED_2X2);
            BLOCKS.add(GLYPH_HOLDER_4X4);
            BLOCKS.add(GLYPH_HOLDER_INVERTED_4X4);

            GLYPH_HOLDERS.add(GLYPH_HOLDER_1X1);
            GLYPH_HOLDERS.add(GLYPH_HOLDER_INVERTED_1X1);
            GLYPH_HOLDERS.add(GLYPH_HOLDER_2X2);
            GLYPH_HOLDERS.add(GLYPH_HOLDER_INVERTED_2X2);
            GLYPH_HOLDERS.add(GLYPH_HOLDER_4X4);
            GLYPH_HOLDERS.add(GLYPH_HOLDER_INVERTED_4X4);

            event.getRegistry().register(GLYPH_1X1);
            event.getRegistry().register(GLYPH_2X2);
            event.getRegistry().register(GLYPH_4X4);

            GLYPH_HOLDER_DUMMIES.add(GLYPH_1X1);
            GLYPH_HOLDER_DUMMIES.add(GLYPH_2X2);
            GLYPH_HOLDER_DUMMIES.add(GLYPH_4X4);
        }

        if (RubyUtilsConfig.crateOptions.enableCrates) BLOCKS.add(CRATE_WOODEN);

        for (Block block : BLOCKS) {
            block.setCreativeTab(RubyUtilsCreativeTabs.RUBYUTILS_TAB);
            event.getRegistry().register(block);
        }
    }
}