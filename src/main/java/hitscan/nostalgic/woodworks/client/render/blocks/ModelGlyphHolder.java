package hitscan.nostalgic.woodworks.client.render.blocks;

import codechicken.lib.render.CCQuad;
import codechicken.lib.vec.TransformationList;
import codechicken.lib.vec.Translation;
import hitscan.nostalgic.woodworks.blocks.BlockGlyphHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModelGlyphHolder implements IBakedModel {
    public static final HashMap<EnumFacing, IBakedModel> GLYPH_HOLDER_2x2_MODELS = new HashMap<>();
    public static final HashMap<EnumFacing, IBakedModel> GLYPH_HOLDER_4x4_MODELS = new HashMap<>();

    public static final TextureAtlasSprite[] ATLAS_SPRITE_INDICES = {
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/brick"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/bookshelf"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/bedrock"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/coal_ore"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/emerald_block"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/anvil_base"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/cobblestone"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/cobblestone_mossy"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/chorus_flower"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/clay"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/glazed_terracotta_light_blue"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/hay_block_side"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/diamond_block"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/end_bricks"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/jukebox_top"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/log_jungle")
    };

    final IBakedModel model;
    final boolean doubled;

    public ModelGlyphHolder (IBakedModel model, boolean doubled) {
        this.model = model;
        this.doubled = doubled;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quads = new ArrayList<>();

        boolean active = state.getValue(BlockGlyphHolder.ACTIVE);
        EnumFacing facing = state.getValue(BlockGlyphHolder.FACING);

        quads.addAll(model.getQuads(state, side, rand));

        List<BakedQuad> glyphQuads = doubled ? GLYPH_HOLDER_4x4_MODELS.get(facing).getQuads(state, side, rand) : GLYPH_HOLDER_2x2_MODELS.get(facing).getQuads(state, side, rand);

        for (BakedQuad quad : glyphQuads) {
            EnumFacing quadFacing = quad.getFace();
            if (quadFacing == facing.getOpposite() || quadFacing == facing) {
                BakedQuad glyphQuad = quad;

                for (int i = 0; i < 16; i++) {
                    if (quad.getSprite() == ATLAS_SPRITE_INDICES[i]) {
                        glyphQuad = new BakedQuadRetextured(quad, Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("woodworks:font/ascii_glyph_"+i));
                    }
                }

                int[] vertexData = glyphQuad.getVertexData();
                int targetColor = active ? 0xFFFFFF00 : 0x7FFFFF00;
                for (int i = 3; i < vertexData.length; i += 7) {
                    vertexData[i] = targetColor;
                }

                CCQuad glyphQuadAsCCQuad = new CCQuad(glyphQuad);

                float xTranslation = 0;
                float zTranslation = 0;

                xTranslation += (quadFacing == EnumFacing.WEST ? -0.001F : (quadFacing == EnumFacing.EAST ? 0.001F : 0));
                zTranslation += (quadFacing == EnumFacing.NORTH ? -0.001F : (quadFacing == EnumFacing.SOUTH ? 0.001F : 0));

                TransformationList transformationList = new TransformationList(
                    new Translation(xTranslation, 0, zTranslation)
                );

                glyphQuadAsCCQuad.apply(transformationList);
                glyphQuad = glyphQuadAsCCQuad.bake();

                quads.add(glyphQuad);
            }
        }

        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return model.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}
