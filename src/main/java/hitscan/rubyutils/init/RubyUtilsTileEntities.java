package hitscan.rubyutils.init;

import hitscan.rubyutils.Tags;
import hitscan.rubyutils.tileentities.TileEntityCrateWooden;
import hitscan.rubyutils.tileentities.TileEntityGlyphHolder;
import hitscan.rubyutils.tileentities.TileEntityPodium;
import hitscan.rubyutils.tileentities.TileEntityShelf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RubyUtilsTileEntities {
    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityShelf.class, new ResourceLocation(Tags.MOD_ID, "shelf"));
        GameRegistry.registerTileEntity(TileEntityPodium.class, new ResourceLocation(Tags.MOD_ID, "podium"));
        GameRegistry.registerTileEntity(TileEntityGlyphHolder.class, new ResourceLocation(Tags.MOD_ID, "glyph_holder"));
        GameRegistry.registerTileEntity(TileEntityCrateWooden.class, new ResourceLocation(Tags.MOD_ID, "crate"));
    }
}
