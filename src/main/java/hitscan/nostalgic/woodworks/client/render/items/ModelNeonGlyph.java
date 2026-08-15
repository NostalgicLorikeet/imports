package hitscan.nostalgic.woodworks.client.render.items;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import hitscan.nostalgic.woodworks.glyphs.EnumGlyphColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ItemLayerModel;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ModelNeonGlyph implements IBakedModel {
    public static final Cache<Integer, List<BakedQuad>> NEON_GLYPH_QUAD_LIST_CACHE = CacheBuilder.newBuilder()
            .maximumSize(256)
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();

    final IBakedModel model;
    final int character;
    final EnumGlyphColor color;

    public ModelNeonGlyph(IBakedModel model) {
        this.model = model;
        this.character = 2;
        this.color = EnumGlyphColor.WHITE;
    }

    public ModelNeonGlyph(IBakedModel model, int character, EnumGlyphColor color) {
        this.model = model;
        this.character = character;
        this.color = color;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quads = NEON_GLYPH_QUAD_LIST_CACHE.getIfPresent(color.colorInt * 256 + character);

        if (quads == null) {
            quads = new ArrayList<>();

            quads.addAll(ItemLayerModel.getQuadsForSprite(0, Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("woodworks:items/neon_glyph"), DefaultVertexFormats.ITEM, Optional.empty()));

            for (BakedQuad quad : ItemLayerModel.getQuadsForSprite(0, Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("woodworks:font/ascii_glyph_" + character), DefaultVertexFormats.ITEM, Optional.empty())) {
                int[] vertexData = quad.getVertexData();
                for (int i = 3; i < vertexData.length; i += 7) {
                    vertexData[i] = color.colorInt;
                }

                quads.add(quad);
            }

            NEON_GLYPH_QUAD_LIST_CACHE.put(color.colorInt * 256 + character, quads);
        }

        return quads;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, model.handlePerspective(cameraTransformType).getRight());
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ModelNeonGlyphItemOverrideList.INSTANCE;
    }

    private static class ModelNeonGlyphItemOverrideList extends ItemOverrideList {
        public static final ModelNeonGlyphItemOverrideList INSTANCE = new ModelNeonGlyphItemOverrideList();

        public ModelNeonGlyphItemOverrideList() {
            super(Collections.emptyList());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel model, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
            int character = 0;
            EnumGlyphColor color;

            color = EnumGlyphColor.values()[stack.getMetadata()];

            if (stack.hasTagCompound()) {
                NBTTagCompound tagCompound = stack.getTagCompound();
                if (tagCompound.hasKey("Character")) character = tagCompound.getInteger("Character");
            }

            return new ModelNeonGlyph(model, character, color);
        }
    }
}
