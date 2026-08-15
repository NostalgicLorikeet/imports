package hitscan.nostalgic.woodworks.client.render.blocks;

import gregtech.client.utils.RenderUtil;
import hitscan.nostalgic.woodworks.blocks.BlockGlyphHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModelGlyphHolder implements IBakedModel {
    public static final HashMap<EnumFacing, IBakedModel> GLYPH_HOLDER_1x1_MODELS = new HashMap<>();
    public static final HashMap<EnumFacing, IBakedModel> GLYPH_HOLDER_2x2_MODELS = new HashMap<>();
    public static final HashMap<EnumFacing, IBakedModel> GLYPH_HOLDER_4x4_MODELS = new HashMap<>();

    public static final HashMap<TextureAtlasSprite, Integer> ATLAS_SPRITE_INDICES = new HashMap<>();

    final IBakedModel model;
    final int dim;

    public ModelGlyphHolder (IBakedModel model, int dim) {
        this.model = model;
        this.dim = dim;

        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/brick"), 0);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/bookshelf"), 1);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/bedrock"), 2);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/coal_ore"), 3);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/emerald_block"), 4);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/anvil_base"), 5);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/cobblestone"), 6);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/cobblestone_mossy"), 7);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/chorus_flower"), 8);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/clay"), 9);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/glazed_terracotta_light_blue"), 10);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/hay_block_side"), 11);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/diamond_block"), 12);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/end_bricks"), 13);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/jukebox_top"), 14);
        ATLAS_SPRITE_INDICES.put(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/log_jungle"), 15);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quads = new ArrayList<>();

        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState ebs = (IExtendedBlockState) state;

            boolean active = ebs.getValue(BlockGlyphHolder.ACTIVE);
            EnumFacing facing = ebs.getValue(BlockGlyphHolder.FACING);
            int[][] glyphsAndColors = ebs.getValue(BlockGlyphHolder.COLOR_GLYPH_PAIRS);
            boolean isEmpty = true;

            List<BakedQuad> glyphQuads = dim == 4 ? GLYPH_HOLDER_4x4_MODELS.get(facing).getQuads(state, side, rand) : (dim == 2 ? GLYPH_HOLDER_2x2_MODELS.get(facing).getQuads(state, side, rand) : GLYPH_HOLDER_1x1_MODELS.get(facing).getQuads(state, side, rand));

            for (BakedQuad quad : glyphQuads) {
                EnumFacing quadFacing = quad.getFace();
                if (quadFacing == facing.getOpposite() || quadFacing == facing) {
                    int currentQuadCharIndex = ATLAS_SPRITE_INDICES.getOrDefault(quad.getSprite(), 0);

                    if (glyphsAndColors[currentQuadCharIndex][2] != -1) {
                        isEmpty = false;
                        BakedQuad glyphQuad = new BakedQuadRetextured(quad, Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("woodworks:font/ascii_glyph_" + glyphsAndColors[currentQuadCharIndex][2]));

                        int[] vertexData = glyphQuad.getVertexData();
                        int colorAlt = glyphsAndColors[currentQuadCharIndex][1];
                        int targetColor = active ? glyphsAndColors[currentQuadCharIndex][0] : (colorAlt & 0xFF000000) | (((int)(((colorAlt >> 16) & 0xFF) * 0.5 + 40)) << 16) | (((int)(((colorAlt >> 8) & 0xFF) * 0.5 + 40)) << 8) | (int)((colorAlt & 0xFF) * 0.5 + 40);
                        for (int i = 3; i < vertexData.length; i += 7) {
                            vertexData[i] = targetColor;
                        }

                        if (active) glyphQuad = RenderUtil.makeEmissive(glyphQuad);

                        quads.add(glyphQuad);
                    }
                }
            }

            if (isEmpty) quads.addAll(model.getQuads(state, side, rand));
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
