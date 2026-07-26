package hitscan.nostalgic.woodworks.client.render.blocks;

import codechicken.lib.render.CCQuad;
import codechicken.lib.vec.uv.UVScale;
import codechicken.lib.vec.uv.UVTransformationList;
import codechicken.lib.vec.uv.UVTranslation;
import gregtech.client.utils.RenderUtil;
import hitscan.nostalgic.woodworks.blocks.BlockNeonGlyph;
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
import java.util.List;

public class ModelNeonGlyphBlock  implements IBakedModel {
    IBakedModel neonGlyphBlockModel;
    TextureAtlasSprite neonGlyphBlockFrontTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("woodworks:blocks/neon_glyph_block_front");
    TextureAtlasSprite asciiGlyphTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:font/ascii");

    public ModelNeonGlyphBlock(IBakedModel neonGlyphBlockModel) {
        this.neonGlyphBlockModel = neonGlyphBlockModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        boolean active = state.getValue(BlockNeonGlyph.ACTIVE);
        EnumFacing facing = state.getValue(BlockNeonGlyph.FACING);
        List<BakedQuad> quads = new ArrayList<>();

        for (BakedQuad quad : neonGlyphBlockModel.getQuads(state, side, rand)) {
            quads.add(quad);
            if (side == state.getValue(BlockNeonGlyph.FACING)) {
                BakedQuad glyphQuad = new BakedQuadRetextured(quad, asciiGlyphTexture);

                int targetColor = active ? 0xFFFFA663 : 0x7FFFA663; //abgr
                int[] vertexData = glyphQuad.getVertexData();

                for (int i = 3; i < vertexData.length; i += 7) {
                    vertexData[i] = targetColor;
                }

                CCQuad ccQuad = new CCQuad(glyphQuad);

                UVTransformationList uvTransformationList = new UVTransformationList(
                        new UVScale((2/16F)),
                        new UVTranslation(10/128F, 11/128F)
                );

                ccQuad.apply(uvTransformationList);

                glyphQuad = ccQuad.bake();

                if (active) glyphQuad = RenderUtil.makeEmissive(glyphQuad);

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
        return neonGlyphBlockFrontTexture;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}
