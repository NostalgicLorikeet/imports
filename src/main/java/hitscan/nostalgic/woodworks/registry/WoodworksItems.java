package hitscan.nostalgic.woodworks.registry;

import hitscan.nostalgic.woodworks.Woodworks;
import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.items.ItemDyedGasTube;
import hitscan.nostalgic.woodworks.items.ItemGlub;
import hitscan.nostalgic.woodworks.items.ItemNeonGlyph;
import hitscan.nostalgic.woodworks.items.ItemShelf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class WoodworksItems {
    static ArrayList<Item> items = new ArrayList<>();

    public static final Item SHELF_SHORT = new ItemShelf(WoodworksBlocks.SHELF_SHORT);
    public static final Item SHELF_LONG = new ItemShelf(WoodworksBlocks.SHELF_LONG);
    public static final Item SHELF_DISPLAY = new ItemShelf(WoodworksBlocks.SHELF_DISPLAY);
    public static final Item SHELF_DISPLAY_CHAIN = new ItemShelf(WoodworksBlocks.SHELF_DISPLAY_CHAINED);
    public static final Item SHELF_DISPLAY_HANGING = new ItemShelf(WoodworksBlocks.SHELF_DISPLAY_HANGING);
    public static final Item PODIUM = new ItemBlock(WoodworksBlocks.PODIUM);
    public static final Item PODIUM_TROPHY = new ItemBlock(WoodworksBlocks.PODIUM_TROPHY);
    public static final Item DYED_GAS_TUBE = new ItemDyedGasTube();
    public static final Item GLYPH_HOLDER_1X1 = new ItemBlock(WoodworksBlocks.GLYPH_HOLDER_1X1);
    public static final Item GLYPH_HOLDER_INVERTED_1X1 = new ItemBlock(WoodworksBlocks.GLYPH_HOLDER_INVERTED_1X1);
    public static final Item GLYPH_HOLDER_2X2 = new ItemBlock(WoodworksBlocks.GLYPH_HOLDER_2X2);
    public static final Item GLYPH_HOLDER_INVERTED_2X2 = new ItemBlock(WoodworksBlocks.GLYPH_HOLDER_INVERTED_2X2);
    public static final Item GLYPH_HOLDER_4X4 = new ItemBlock(WoodworksBlocks.GLYPH_HOLDER_4X4);
    public static final Item GLYPH_HOLDER_INVERTED_4X4 = new ItemBlock(WoodworksBlocks.GLYPH_HOLDER_INVERTED_4X4);
    public static final Item NEON_GLYPH = new ItemNeonGlyph();
    public static final Item TEST = new ItemGlub();

    @SubscribeEvent
    public static void blocks(RegistryEvent.Register<Item> event) {
        items.add(SHELF_SHORT);
        items.add(SHELF_LONG);
        items.add(SHELF_DISPLAY);
        items.add(SHELF_DISPLAY_CHAIN);
        items.add(SHELF_DISPLAY_HANGING);
        items.add(PODIUM);
        items.add(PODIUM_TROPHY);
        items.add(DYED_GAS_TUBE);
        items.add(GLYPH_HOLDER_1X1);
        items.add(GLYPH_HOLDER_2X2);
        items.add(GLYPH_HOLDER_4X4);
        items.add(GLYPH_HOLDER_INVERTED_1X1);
        items.add(GLYPH_HOLDER_INVERTED_2X2);
        items.add(GLYPH_HOLDER_INVERTED_4X4);
        items.add(NEON_GLYPH);
        items.add(TEST);

        for (Item item : items) {
            item.setCreativeTab(Woodworks.CREATIVE_TAB);
            if (item instanceof ItemBlock) {
                ItemBlock itemBlock = (ItemBlock) item;
                item.setRegistryName(itemBlock.getBlock().getRegistryName());
                item.setTranslationKey(itemBlock.getBlock().getRegistryName().toString());
            }
            event.getRegistry().register(item);
        }
    }
}