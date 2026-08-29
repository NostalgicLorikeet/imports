package hitscan.nostalgic.woodworks.client.render.blocks;

import hitscan.nostalgic.woodworks.blocks.BlockCrateWooden;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelCrate implements IBakedModel {
    boolean isStack = false;
    final IBakedModel defaultModel;
    public static final TextureAtlasSprite[] placeholderPlanks = {
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/planks_oak"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/planks_acacia"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/planks_big_oak"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/planks_birch"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/planks_jungle"),
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/planks_spruce")
    };
    ItemStack[] planksInventory = new ItemStack[6];

    public ModelCrate(IBakedModel model) {
        this.defaultModel = model;
    }

    public ModelCrate(IBakedModel model, ItemStack[] planks) {
        this.defaultModel = model;
        this.planksInventory = planks;
        this.isStack = true;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quads = new ArrayList<>();
        ItemStack[] planks = new ItemStack[6];

        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState ebs = (IExtendedBlockState) state;
            if (ebs.getValue(BlockCrateWooden.PLANKS) != null) {
                planks = ebs.getValue(BlockCrateWooden.PLANKS).toArray(new ItemStack[0]);
            }
        }

        if (isStack) planks = planksInventory;

        for (BakedQuad quad : defaultModel.getQuads(state, side, rand)) {
            int select = 0;

            for (int i = 0; i < 6; i++) {
                if (quad.getSprite() == placeholderPlanks[i]) {
                    select = i;
                }
            }

            //i dont understand why this works
            if (planks[select] != null) {
                if (!planks[select].isEmpty()) {
                    quads.add(new BakedQuadRetextured(quad, Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
                            .getTexture(ForgeRegistries.BLOCKS.getValue(Block.getBlockFromItem(planks[select].getItem()).getRegistryName()).getStateFromMeta(planks[select].getItemDamage()))
                    ));
                }
            } else {
                quads.add(quad);
            }
        }

        return quads;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, defaultModel.handlePerspective(cameraTransformType).getRight());
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
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
        return defaultModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return new ItemOverrideList(Collections.emptyList()) {
            @Override
            public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                ItemStack[] planks = new ItemStack[6];

                if (stack.hasTagCompound()) {
                    NBTTagCompound tag = stack.getTagCompound();

                    if (tag.hasKey("Planks")) {
                        NBTTagCompound planksTag = tag.getCompoundTag("Planks");
                        for (int i = 0; i < 6; i++) {
                            if (planksTag.hasKey("Plank" + i)) {
                                planks[i] = new ItemStack(planksTag.getCompoundTag("Plank" + i));
                            }
                        }
                    }
                }

                return new ModelCrate(originalModel, planks);
            }
        };
    }
}
