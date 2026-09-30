package hitscan.nostalgic.woodworks.client.render.entities.render;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.client.render.WoodworksSkinManager;
import hitscan.nostalgic.woodworks.client.render.entities.layers.LayerStatueArmor;
import hitscan.nostalgic.woodworks.client.render.entities.model.ModelDoll;
import hitscan.nostalgic.woodworks.client.render.entities.model.ModelStatue;
import hitscan.nostalgic.woodworks.entity.EntityDollStatue;
import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;

public class RenderStatue extends RenderLivingBase<EntityStrawStatue> {
    private static final ModelStatue STEVE = new ModelStatue(0.0F, false);
    private static final ModelStatue ALEX = new ModelStatue(0.0F, true);
    private static final ModelStatue STEVE_DOLL = new ModelDoll(0.0F, false);
    private static final ModelStatue ALEX_DOLL = new ModelDoll(0.0F, true);

    public static final ResourceLocation STRAW_STEVE_SKIN = new ResourceLocation(Tags.MOD_ID, "textures/entity/straw_steve.png");
    public static final ResourceLocation STRAW_ALEX_SKIN = new ResourceLocation(Tags.MOD_ID, "textures/entity/straw_alex.png");
    public static final ResourceLocation STONE_STEVE_SKIN = new ResourceLocation(Tags.MOD_ID, "textures/entity/stone_steve.png");
    public static final ResourceLocation STONE_ALEX_SKIN = new ResourceLocation(Tags.MOD_ID, "textures/entity/stone_alex.png");

    public RenderStatue(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelStatue(0.0F, false), 0.5F);
        this.addLayer(new LayerStatueArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.mainModel = STEVE;
        this.shadowSize = 0.0F;
    }

    @Override
    public void doRender(EntityStrawStatue entity, double x, double y, double z, float entityYaw, float partialTicks) {
        UUID uuid = entity.getPlayerUUID() != null ? entity.getPlayerUUID() : entity.getUniqueID();
        boolean isSlim = WoodworksSkinManager.SKIN_USE_SLIM.getOrDefault(uuid, (uuid.hashCode() & 1) == 1);
        this.mainModel = (entity instanceof EntityDollStatue) ? (isSlim ? ALEX_DOLL : STEVE_DOLL) : (isSlim ? ALEX : STEVE);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(EntityStrawStatue entity, float partialTickTime) {
        GlStateManager.rotate(entity.getFullBodyRotation(), 0.0F, 1.0F, 0.0F);
        if (entity instanceof EntityDollStatue) GlStateManager.scale(0.5F, 0.5F, 0.5F);
    }

    @Override
    protected boolean canRenderName(EntityStrawStatue entity) {
        return false;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityStrawStatue entity) {
        UUID uuid = entity.getPlayerUUID();

        if (uuid != null) {
            return WoodworksSkinManager.getSkinLocation(uuid);
        }

        WoodworksSkinManager.SKIN_USE_SLIM.put(entity.getUniqueID(), (entity.getUniqueID().hashCode() & 1) == 1);
        //return DefaultPlayerSkin.getDefaultSkin(entity.getUniqueID());
        if (entity.getStatueType().isStraw()) {
            return (entity.getUniqueID().hashCode() & 1) == 1 ? STRAW_ALEX_SKIN : STRAW_STEVE_SKIN;
        } else {
            return (entity.getUniqueID().hashCode() & 1) == 1 ? STONE_ALEX_SKIN : STONE_STEVE_SKIN;
        }
    }
}
