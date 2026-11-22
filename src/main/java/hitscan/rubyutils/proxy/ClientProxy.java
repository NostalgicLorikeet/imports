package hitscan.rubyutils.proxy;

import hitscan.rubyutils.client.render.entities.render.RenderDoll;
import hitscan.rubyutils.client.render.entities.render.RenderEncrusted;
import hitscan.rubyutils.client.render.entities.render.RenderStatue;
import hitscan.rubyutils.client.render.items.TileEntityItemStackRendererDoll;
import hitscan.rubyutils.client.render.tileentities.TileEntitySpecialRendererPodium;
import hitscan.rubyutils.client.render.tileentities.TileEntitySpecialRendererShelf;
import hitscan.rubyutils.config.RubyUtilsConfig;
import hitscan.rubyutils.entity.EntityDollStatue;
import hitscan.rubyutils.entity.EntityEncrusted;
import hitscan.rubyutils.entity.EntityStoneStatue;
import hitscan.rubyutils.entity.EntityStrawStatue;
import hitscan.rubyutils.events.EventAssetReloadListener;
import hitscan.rubyutils.events.EventBubbleRenderer;
import hitscan.rubyutils.events.EventTextureStitch;
import hitscan.rubyutils.init.RubyUtilsItems;
import hitscan.rubyutils.init.RubyUtilsRegisterModels;
import hitscan.rubyutils.tileentities.TileEntityPodium;
import hitscan.rubyutils.tileentities.TileEntityShelf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
        MinecraftForge.EVENT_BUS.register(EventTextureStitch.class);
        MinecraftForge.EVENT_BUS.register(RubyUtilsRegisterModels.class);
        MinecraftForge.EVENT_BUS.register(EventAssetReloadListener.class);
        MinecraftForge.EVENT_BUS.register(EventBubbleRenderer.class);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShelf.class, new TileEntitySpecialRendererShelf());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPodium.class, new TileEntitySpecialRendererPodium());

        if (RubyUtilsConfig.statueOptions.enableStatues) {
            RenderingRegistry.registerEntityRenderingHandler(EntityStrawStatue.class, RenderStatue::new);
            RenderingRegistry.registerEntityRenderingHandler(EntityStoneStatue.class, RenderStatue::new);
            RenderingRegistry.registerEntityRenderingHandler(EntityDollStatue.class, RenderDoll::new);

            RubyUtilsItems.STATUE.setTileEntityItemStackRenderer(TileEntityItemStackRendererDoll.INSTANCE);
        }

        if (RubyUtilsConfig.encrustedOptions.enableEncrusted) RenderingRegistry.registerEntityRenderingHandler(EntityEncrusted.class, RenderEncrusted::new);
    }

    @Override
    public void init() {
        super.init();
    }
}
