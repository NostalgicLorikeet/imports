package hitscan.nostalgic.woodworks;

import hitscan.nostalgic.woodworks.client.render.tileentities.TileEntitySpecialRendererPodium;
import hitscan.nostalgic.woodworks.client.render.tileentities.TileEntitySpecialRendererShelf;
import hitscan.nostalgic.woodworks.events.AssetReloadListener;
import hitscan.nostalgic.woodworks.events.TextureStitch;
import hitscan.nostalgic.woodworks.registry.WoodworksRegisterModels;
import hitscan.nostalgic.woodworks.tileentities.TileEntityPodium;
import hitscan.nostalgic.woodworks.tileentities.TileEntityShelf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WoodworksClient {
    public static void registerClientEvents() {
        MinecraftForge.EVENT_BUS.register(TextureStitch.class);
        MinecraftForge.EVENT_BUS.register(WoodworksRegisterModels.class);
        MinecraftForge.EVENT_BUS.register(AssetReloadListener.class);
    }

    public static void registerTESRs() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShelf.class, new TileEntitySpecialRendererShelf());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPodium.class, new TileEntitySpecialRendererPodium());
    }
}
