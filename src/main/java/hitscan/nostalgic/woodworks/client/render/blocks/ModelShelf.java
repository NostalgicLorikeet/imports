package hitscan.nostalgic.woodworks.client.render.blocks;

import hitscan.nostalgic.woodworks.blocks.BlockShelf;
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

public class ModelShelf implements IBakedModel {
    public static final TextureAtlasSprite oakPlankSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/planks_oak");
    IBakedModel shelfModel;
    String id = "minecraft:planks";
    int damage = 0;
    boolean isItemStack = false;

    public ModelShelf(IBakedModel shelfModel, String id, int damage) {
        isItemStack = true;
        this.id = id;
        this.damage = damage;
        this.shelfModel = shelfModel;
    }

    public ModelShelf(IBakedModel shelfModel) {
        this.shelfModel = shelfModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quads = new ArrayList<>();

        TextureAtlasSprite newSprite = oakPlankSprite;

        try {
            if (!isItemStack) {
                if (state != null) {
                    if (state instanceof IExtendedBlockState) {
                        IExtendedBlockState ebs = (IExtendedBlockState) state;

                        newSprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ebs.getValue(BlockShelf.ID))).getStateFromMeta(ebs.getValue(BlockShelf.DAMAGE)));
                    }
                }
            } else {
                newSprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id)).getStateFromMeta(damage));
            }

            for (BakedQuad quad : shelfModel.getQuads(state, side, rand)) {
                if (quad.getSprite() == oakPlankSprite) {
                    quads.add(new BakedQuadRetextured(quad, newSprite));
                } else {
                    quads.add(quad);
                }
            }
        } catch (Exception ignored) {
            return shelfModel.getQuads(state, side, rand);
        }

        return quads;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, shelfModel.handlePerspective(cameraTransformType).getRight());
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
                String id = "minecraft:planks";
                int damage = 0;

                if (stack.hasTagCompound()) {
                    NBTTagCompound tag = stack.getTagCompound();
                    if (tag.hasKey("Texture")) {
                        NBTTagCompound texture = tag.getCompoundTag("Texture");
                        if (texture.hasKey("id")) id = texture.getString("id");
                        if (texture.hasKey("Damage")) damage = texture.getInteger("Damage");
                    }
                }

                return new ModelShelf(originalModel, id, damage);
            }
        };
    }
}
