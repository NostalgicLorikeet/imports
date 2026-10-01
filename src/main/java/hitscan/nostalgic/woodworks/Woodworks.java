package hitscan.nostalgic.woodworks;

import hitscan.nostalgic.woodworks.gui.WoodworksGUIHandler;
import hitscan.nostalgic.woodworks.proxy.CommonProxy;
import hitscan.nostalgic.woodworks.registry.WoodworksBlocks;
import hitscan.nostalgic.woodworks.registry.WoodworksItems;
import hitscan.nostalgic.woodworks.registry.WoodworksTileEntities;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class Woodworks {
    @SidedProxy(clientSide = "hitscan.nostalgic.woodworks.proxy.ClientProxy", serverSide = "hitscan.nostalgic.woodworks.proxy.CommonProxy")
    public static CommonProxy PROXY;

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);
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
