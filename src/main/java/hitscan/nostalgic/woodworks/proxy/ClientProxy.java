package hitscan.nostalgic.woodworks.proxy;

import hitscan.nostalgic.woodworks.client.render.tileentities.TileEntitySpecialRendererPodium;
import hitscan.nostalgic.woodworks.client.render.tileentities.TileEntitySpecialRendererShelf;
import hitscan.nostalgic.woodworks.events.AssetReloadListener;
import hitscan.nostalgic.woodworks.events.TextureStitch;
import hitscan.nostalgic.woodworks.registry.WoodworksRegisterModels;
import hitscan.nostalgic.woodworks.tileentities.TileEntityPodium;
import hitscan.nostalgic.woodworks.tileentities.TileEntityShelf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
        MinecraftForge.EVENT_BUS.register(TextureStitch.class);
        MinecraftForge.EVENT_BUS.register(WoodworksRegisterModels.class);
        MinecraftForge.EVENT_BUS.register(AssetReloadListener.class);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShelf.class, new TileEntitySpecialRendererShelf());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPodium.class, new TileEntitySpecialRendererPodium());
    }

    @Override
    public void init() {
        super.init();
    }
}
