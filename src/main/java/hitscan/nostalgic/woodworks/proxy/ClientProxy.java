package hitscan.nostalgic.woodworks.proxy;

import hitscan.nostalgic.woodworks.client.render.entities.render.RenderDoll;
import hitscan.nostalgic.woodworks.client.render.entities.render.RenderStatue;
import hitscan.nostalgic.woodworks.client.render.items.TileEntityItemStackRendererDoll;
import hitscan.nostalgic.woodworks.client.render.tileentities.TileEntitySpecialRendererPodium;
import hitscan.nostalgic.woodworks.client.render.tileentities.TileEntitySpecialRendererShelf;
import hitscan.nostalgic.woodworks.entity.EntityDollStatue;
import hitscan.nostalgic.woodworks.entity.EntityStoneStatue;
import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import hitscan.nostalgic.woodworks.events.EventAssetReloadListener;
import hitscan.nostalgic.woodworks.events.EventTextureStitch;
import hitscan.nostalgic.woodworks.registry.WoodworksItems;
import hitscan.nostalgic.woodworks.registry.WoodworksRegisterModels;
import hitscan.nostalgic.woodworks.tileentities.TileEntityPodium;
import hitscan.nostalgic.woodworks.tileentities.TileEntityShelf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
        MinecraftForge.EVENT_BUS.register(EventTextureStitch.class);
        MinecraftForge.EVENT_BUS.register(WoodworksRegisterModels.class);
        MinecraftForge.EVENT_BUS.register(EventAssetReloadListener.class);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShelf.class, new TileEntitySpecialRendererShelf());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPodium.class, new TileEntitySpecialRendererPodium());

        RenderingRegistry.registerEntityRenderingHandler(EntityStrawStatue.class, RenderStatue::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityStoneStatue.class, RenderStatue::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDollStatue.class, RenderDoll::new);

        WoodworksItems.STATUE.setTileEntityItemStackRenderer(TileEntityItemStackRendererDoll.INSTANCE);
    }

    @Override
    public void init() {
        super.init();
    }
}
