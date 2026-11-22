package hitscan.rubyutils.init;

import hitscan.rubyutils.Tags;
import hitscan.rubyutils.RubyUtilsCreativeTabs;
import hitscan.rubyutils.config.RubyUtilsConfig;
import hitscan.rubyutils.items.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class RubyUtilsItems {
    static ArrayList<Item> ITEMS = new ArrayList<>();

    public static final Item SHELF_SHORT = new ItemShelf(RubyUtilsBlocks.SHELF_SHORT);
    public static final Item SHELF_LONG = new ItemShelf(RubyUtilsBlocks.SHELF_LONG);
    public static final Item SHELF_DISPLAY = new ItemShelf(RubyUtilsBlocks.SHELF_DISPLAY);
    public static final Item SHELF_SHORT_ALL_WOOD = new ItemShelf(RubyUtilsBlocks.SHELF_SHORT_ALL_WOOD);
    public static final Item SHELF_LONG_ALL_WOOD = new ItemShelf(RubyUtilsBlocks.SHELF_LONG_ALL_WOOD);
    public static final Item SHELF_DISPLAY_ALL_WOOD = new ItemShelf(RubyUtilsBlocks.SHELF_DISPLAY_ALL_WOOD);
    public static final Item SHELF_DISPLAY_CHAIN = new ItemShelf(RubyUtilsBlocks.SHELF_DISPLAY_CHAINED);
    public static final Item PODIUM = new ItemBlock(RubyUtilsBlocks.PODIUM);
    public static final Item PODIUM_TROPHY = new ItemBlock(RubyUtilsBlocks.PODIUM_TROPHY);
    public static final Item DYED_GAS_TUBE = new ItemDyedGasTube();
    public static final Item GLYPH_HOLDER_1X1 = new ItemBlock(RubyUtilsBlocks.GLYPH_HOLDER_1X1);
    public static final Item GLYPH_HOLDER_INVERTED_1X1 = new ItemBlock(RubyUtilsBlocks.GLYPH_HOLDER_INVERTED_1X1);
    public static final Item GLYPH_HOLDER_2X2 = new ItemBlock(RubyUtilsBlocks.GLYPH_HOLDER_2X2);
    public static final Item GLYPH_HOLDER_INVERTED_2X2 = new ItemBlock(RubyUtilsBlocks.GLYPH_HOLDER_INVERTED_2X2);
    public static final Item GLYPH_HOLDER_4X4 = new ItemBlock(RubyUtilsBlocks.GLYPH_HOLDER_4X4);
    public static final Item GLYPH_HOLDER_INVERTED_4X4 = new ItemBlock(RubyUtilsBlocks.GLYPH_HOLDER_INVERTED_4X4);
    public static final Item NEON_GLYPH = new ItemNeonGlyph();
    public static final Item CRATE_WOODEN = new ItemCrate(RubyUtilsBlocks.CRATE_WOODEN);
    public static final Item STATUE = new ItemStatue();
    public static final Item TRACKLAYER_BOOTS = new ItemTracklayerBoots("tracklayer_boots");

    @SubscribeEvent
    public static void blocks(RegistryEvent.Register<Item> event) {
        if (RubyUtilsConfig.shelfOptions.enableShelves) {
            ITEMS.add(SHELF_SHORT);
            ITEMS.add(SHELF_LONG);
            ITEMS.add(SHELF_DISPLAY);
            ITEMS.add(SHELF_SHORT_ALL_WOOD);
            ITEMS.add(SHELF_LONG_ALL_WOOD);
            ITEMS.add(SHELF_DISPLAY_ALL_WOOD);
            ITEMS.add(SHELF_DISPLAY_CHAIN);
        }

        if (RubyUtilsConfig.podiumOptions.enablePodiums) {
            ITEMS.add(PODIUM);
            ITEMS.add(PODIUM_TROPHY);
        }

        if (RubyUtilsConfig.neonGlyphHolderOptions.enableNeonGlyphHolders) {
            ITEMS.add(GLYPH_HOLDER_1X1);
            ITEMS.add(GLYPH_HOLDER_2X2);
            ITEMS.add(GLYPH_HOLDER_4X4);
            ITEMS.add(GLYPH_HOLDER_INVERTED_1X1);
            ITEMS.add(GLYPH_HOLDER_INVERTED_2X2);
            ITEMS.add(GLYPH_HOLDER_INVERTED_4X4);
            ITEMS.add(NEON_GLYPH);
        }

        if (RubyUtilsConfig.neonGlyphHolderOptions.enableDyedGasTubes) ITEMS.add(DYED_GAS_TUBE);

        if (RubyUtilsConfig.crateOptions.enableCrates) ITEMS.add(CRATE_WOODEN);

        if (RubyUtilsConfig.statueOptions.enableStatues) ITEMS.add(STATUE);

        if (RubyUtilsConfig.tracklayerBootsOptions.enableTracklayerBoots) ITEMS.add(TRACKLAYER_BOOTS);

        for (Item item : ITEMS) {
            item.setCreativeTab(RubyUtilsCreativeTabs.RUBYUTILS_TAB);
            if (item instanceof ItemBlock) {
                ItemBlock itemBlock = (ItemBlock) item;
                item.setRegistryName(itemBlock.getBlock().getRegistryName());
                item.setTranslationKey(itemBlock.getBlock().getRegistryName().toString());
            }
            event.getRegistry().register(item);
        }
    }
}