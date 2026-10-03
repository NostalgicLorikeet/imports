package hitscan.nostalgic.woodworks.client.render.entities.render;

import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.client.render.WoodworksSkinManager;
import hitscan.nostalgic.woodworks.client.render.entities.layers.LayerStatueHeldItem;
import hitscan.nostalgic.woodworks.client.render.entities.model.ModelDoll;
import hitscan.nostalgic.woodworks.client.render.entities.model.ModelStatue;
import hitscan.nostalgic.woodworks.entity.EntityDollStatue;
import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import hitscan.nostalgic.woodworks.gui.containers.client.GuiContainerStatue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class AbstractRenderStatue extends RenderLivingBase<EntityStrawStatue> {
    private static final ModelStatue STEVE = new ModelStatue(0.0F, false);
    private static final ModelStatue ALEX = new ModelStatue(0.0F, true);
    private static final ModelStatue STEVE_DOLL = new ModelDoll(0.0F, false);
    private static final ModelStatue ALEX_DOLL = new ModelDoll(0.0F, true);

    public static final ResourceLocation STRAW_STEVE_SKIN = new ResourceLocation(Tags.MOD_ID, "textures/entity/straw_steve.png");
    public static final ResourceLocation STRAW_ALEX_SKIN = new ResourceLocation(Tags.MOD_ID, "textures/entity/straw_alex.png");
    public static final ResourceLocation STONE_STEVE_SKIN = new ResourceLocation(Tags.MOD_ID, "textures/entity/stone_steve.png");
    public static final ResourceLocation STONE_ALEX_SKIN = new ResourceLocation(Tags.MOD_ID, "textures/entity/stone_alex.png");

    public AbstractRenderStatue(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelStatue(0.0F, false), 0.5F);
        this.addLayer(new LayerStatueHeldItem(this));
        this.mainModel = STEVE;
        this.shadowSize = 0.0F;
    }

    @Override
    public void doRender(EntityStrawStatue entity, double x, double y, double z, float entityYaw, float partialTicks) {
        boolean isSlim = entity.getPlayerUUID() != null ? WoodworksSkinManager.SKIN_USE_SLIM.getOrDefault(entity.getPlayerUUID(), false) : (entity.getUniqueID().hashCode() & 1) == 1;
        this.mainModel = (entity instanceof EntityDollStatue) ? (isSlim ? ALEX_DOLL : STEVE_DOLL) : (isSlim ? ALEX : STEVE);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(EntityStrawStatue entity, float partialTickTime) {
        if (entity.doRotation) GlStateManager.rotate(entity.getFullBodyRotation(), 0.0F, 1.0F, 0.0F);
        if (entity instanceof EntityDollStatue) GlStateManager.scale(0.5F, 0.5F, 0.5F);
    }

    @Override
    protected boolean canRenderName(EntityStrawStatue entity) {
        return entity.hasCustomName();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityStrawStatue entity) {
        if (entity.getPlayerUUID() != null) {
            return WoodworksSkinManager.getSkinLocation(entity.getPlayerUUID());
        }
        if (entity.getStatueType().isStraw()) {
            return (entity.getUniqueID().hashCode() & 1) == 1 ? STRAW_ALEX_SKIN : STRAW_STEVE_SKIN;
        } else {
            return (entity.getUniqueID().hashCode() & 1) == 1 ? STONE_ALEX_SKIN : STONE_STEVE_SKIN;
        }
    }
}
