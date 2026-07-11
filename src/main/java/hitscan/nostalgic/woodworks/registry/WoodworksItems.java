package hitscan.nostalgic.woodworks.registry;

import hitscan.nostalgic.woodworks.Woodworks;
import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.items.ItemShelf;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class WoodworksItems {
    static ArrayList<Item> items = new ArrayList<>();

    public static final Item SHELF_SHORT = new ItemShelf(WoodworksBlocks.SHELF_SHORT).setRegistryName("short_shelf").setTranslationKey("short_shelf");
    public static final Item SHELF_LONG = new ItemShelf(WoodworksBlocks.SHELF_LONG).setRegistryName("long_shelf").setTranslationKey("long_shelf");
    public static final Item SHELF_DISPLAY = new ItemShelf(WoodworksBlocks.SHELF_DISPLAY).setRegistryName("display_shelf").setTranslationKey("display_shelf");

    @SubscribeEvent
    public static void blocks(RegistryEvent.Register<Item> event) {
        items.add(SHELF_SHORT);
        items.add(SHELF_LONG);
        items.add(SHELF_DISPLAY);

        for (Item item : items) {
            item.setCreativeTab(Woodworks.CREATIVE_TAB);
            event.getRegistry().register(item);
        }
    }
}