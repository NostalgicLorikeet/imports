package hitscan.nostalgic.woodworks;

import hitscan.nostalgic.woodworks.gui.WoodworksGUIHandler;
import hitscan.nostalgic.woodworks.proxy.CommonProxy;
import hitscan.nostalgic.woodworks.registry.WoodworksTileEntities;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class Woodworks {
    public static final CreativeTabs CREATIVE_TAB = new WoodworksCreativeTab();

    @SidedProxy(clientSide = "hitscan.nostalgic.woodworks.proxy.ClientProxy", serverSide = "hitscan.nostalgic.woodworks.proxy.CommonProxy")
    public static CommonProxy PROXY;
    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     *     Take a look at how many FMLStateEvents you can listen to via the @Mod.EventHandler annotation here
     * </a>
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new WoodworksGUIHandler());
        PROXY.init();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PROXY.preInit();
    }
}
