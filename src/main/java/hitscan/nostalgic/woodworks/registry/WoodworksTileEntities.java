package hitscan.nostalgic.woodworks.registry;

import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.client.render.tileentities.TileEntitySpecialRendererPodium;
import hitscan.nostalgic.woodworks.client.render.tileentities.TileEntitySpecialRendererShelf;
import hitscan.nostalgic.woodworks.tileentities.TileEntityPodium;
import hitscan.nostalgic.woodworks.tileentities.TileEntityShelf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WoodworksTileEntities {
    public static void regsiterTileEntities() {
        GameRegistry.registerTileEntity(TileEntityShelf.class, new ResourceLocation(Tags.MOD_ID, "shelf"));
        GameRegistry.registerTileEntity(TileEntityPodium.class, new ResourceLocation(Tags.MOD_ID, "podium"));
    }

    public static void registerTESRs() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShelf.class, new TileEntitySpecialRendererShelf());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPodium.class, new TileEntitySpecialRendererPodium());
    }
}
