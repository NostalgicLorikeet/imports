package hitscan.nostalgic.woodworks.client.render.blocks;

import com.google.common.cache.CacheBuilder;
import hitscan.nostalgic.woodworks.blocks.BlockPodium;
import hitscan.nostalgic.woodworks.registry.WoodworksBlocks;
import hitscan.nostalgic.woodworks.registry.WoodworksItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelPodium implements IBakedModel {
    public static final TextureAtlasSprite oakPlankSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/planks_oak");
    public static final TextureAtlasSprite stoneSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone");

    IBakedModel podiumModel;

    String idWood = "minecraft:planks";
    int damageWood = 0;
    String idStone = "minecraft:stone";
    int damageStone = 0;

    boolean isItemStack = false;

    public ModelPodium(IBakedModel podiumModel, String idWood, int damageWood, String idStone, int damageStone) {
        this.podiumModel = podiumModel;
        this.idWood = idWood;
        this.damageWood = damageWood;
        this.idStone = idStone;
        this.damageStone = damageStone;
        this.isItemStack = true;
    }

    public ModelPodium(IBakedModel podiumModel) {
        this.podiumModel = podiumModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quads = new ArrayList<>();

        TextureAtlasSprite newWoodSprite;
        TextureAtlasSprite newStoneSprite;

        try {
            if (!isItemStack) {
                if (state != null) {
                    if (state instanceof IExtendedBlockState) {
                        IExtendedBlockState ebs = (IExtendedBlockState) state;

                        idWood = ebs.getValue(BlockPodium.ID_WOOD);
                        damageWood = ebs.getValue(BlockPodium.DAMAGE_WOOD);
                        idStone =  ebs.getValue(BlockPodium.ID_STONE);
                        damageStone = ebs.getValue(BlockPodium.DAMAGE_STONE);
                    }
                }
            }

            newWoodSprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(idWood)).getStateFromMeta(damageWood));
            newStoneSprite =  Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(idStone)).getStateFromMeta(damageStone));

            for (BakedQuad quad : podiumModel.getQuads(state, side, rand)) {
                if (quad.getSprite() == oakPlankSprite) {
                    quads.add(new BakedQuadRetextured(quad, newWoodSprite));
                } else if (quad.getSprite() == stoneSprite) {
                    quads.add(new BakedQuadRetextured(quad, newStoneSprite));
                } else {
                    quads.add(quad);
                }
            }
        } catch (Exception ignored) {
            return podiumModel.getQuads(state, side, rand);
        }

        return quads;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, podiumModel.handlePerspective(cameraTransformType).getRight());
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
        return oakPlankSprite;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return new ItemOverrideList(Collections.emptyList()) {
            @Override
            public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                String idWood = "minecraft:planks";
                int damageWood = 0;
                String idStone = "minecraft:stone";
                int damageStone = 0;

                if (stack.hasTagCompound()) {
                    NBTTagCompound tag = stack.getTagCompound();
                    NBTTagCompound texture = tag.getCompoundTag("Texture");
                    NBTTagCompound wood = texture.getCompoundTag("Wood");
                    NBTTagCompound stone = texture.getCompoundTag("Stone");

                    idWood = wood.getString("ID");
                    damageWood = wood.getInteger("Damage");
                    idStone = stone.getString("ID");
                    damageStone = stone.getInteger("Damage");
                }

                return new ModelPodium(originalModel, idWood, damageWood, idStone, damageStone);
            }
        };
    }
}
