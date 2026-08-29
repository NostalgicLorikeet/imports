package hitscan.nostalgic.woodworks;

import hitscan.nostalgic.woodworks.gui.WoodworksGUIHandler;
import hitscan.nostalgic.woodworks.registry.WoodworksItems;
import hitscan.nostalgic.woodworks.registry.WoodworksTileEntities;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class Woodworks {
    public static final CreativeTabs CREATIVE_TAB = new WoodworksCreativeTab();
    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     *     Take a look at how many FMLStateEvents you can listen to via the @Mod.EventHandler annotation here
     * </a>
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        WoodworksTileEntities.registerTESRs();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new WoodworksGUIHandler());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        WoodworksTileEntities.regsiterTileEntities();
    }
}
