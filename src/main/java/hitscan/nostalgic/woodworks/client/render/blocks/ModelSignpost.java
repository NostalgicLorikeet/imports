package hitscan.nostalgic.woodworks.client.render.blocks;

import hitscan.nostalgic.woodworks.blocks.BlockSignpost;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ModelSignpost implements IBakedModel {
    public ModelSignpost() {}

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quads = new ArrayList<>();

        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState ebs = (IExtendedBlockState) state;
            IBlockState originalFenceState = ebs.getValue(BlockSignpost.FENCE_ACTUAL_STATE);
            quads.addAll(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(originalFenceState).getQuads(originalFenceState,side,rand));
        }

        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
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
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/planks_oak");
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}
