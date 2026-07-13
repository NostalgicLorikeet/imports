package hitscan.nostalgic.woodworks.registry;

import hitscan.nostalgic.woodworks.Woodworks;
import hitscan.nostalgic.woodworks.Tags;
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

    public static final BlockShelf SHELF_SHORT = new BlockShelf(
            "short_shelf",
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
    public static final BlockShelf SHELF_DISPLAY = new BlockShelf(
            "display_shelf",
            Material.WOOD,
            false,
            true,
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
            new AxisAlignedBB(0,3/8D,0,1,0.5D, 0.5D),
            new AxisAlignedBB(0,4/8D,0,1,3/8D, 0.5D)
    );

    @SubscribeEvent
    public static void blocks(RegistryEvent.Register<Block> event) {
        BLOCKS.add(SHELF_SHORT);
        BLOCKS.add(SHELF_LONG);
        BLOCKS.add(SHELF_DISPLAY);
        BLOCKS.add(SHELF_DISPLAY_CHAINED);

        SHELVES.add(SHELF_SHORT);
        SHELVES.add(SHELF_LONG);
        SHELVES.add(SHELF_DISPLAY);
        SHELVES.add(SHELF_DISPLAY_CHAINED);

        for (Block block : BLOCKS) {
            block.setCreativeTab(Woodworks.CREATIVE_TAB);
            event.getRegistry().register(block);
        }
    }
}