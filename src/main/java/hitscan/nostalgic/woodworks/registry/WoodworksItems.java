package hitscan.nostalgic.woodworks.registry;

import hitscan.nostalgic.woodworks.Woodworks;
import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.items.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class WoodworksItems {
    static ArrayList<Item> ITEMS = new ArrayList<>();

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
    public static final Item CRATE_WOODEN = new ItemCrate(WoodworksBlocks.CRATE_WOODEN);
    public static final Item CRATE_FRAMING = new Item().setTranslationKey("crate_framing").setRegistryName("crate_framing");

    @SubscribeEvent
    public static void blocks(RegistryEvent.Register<Item> event) {
        ITEMS.add(SHELF_SHORT);
        ITEMS.add(SHELF_LONG);
        ITEMS.add(SHELF_DISPLAY);
        ITEMS.add(SHELF_DISPLAY_CHAIN);
        ITEMS.add(SHELF_DISPLAY_HANGING);
        ITEMS.add(PODIUM);
        ITEMS.add(PODIUM_TROPHY);
        ITEMS.add(DYED_GAS_TUBE);
        ITEMS.add(GLYPH_HOLDER_1X1);
        ITEMS.add(GLYPH_HOLDER_2X2);
        ITEMS.add(GLYPH_HOLDER_4X4);
        ITEMS.add(GLYPH_HOLDER_INVERTED_1X1);
        ITEMS.add(GLYPH_HOLDER_INVERTED_2X2);
        ITEMS.add(GLYPH_HOLDER_INVERTED_4X4);
        ITEMS.add(NEON_GLYPH);
        ITEMS.add(TEST);
        ITEMS.add(CRATE_WOODEN);
        ITEMS.add(CRATE_FRAMING);

        for (Item item : ITEMS) {
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