package hitscan.nostalgic.woodworks.client.render.entities.layers;

import hitscan.nostalgic.woodworks.client.render.entities.model.ModelStatue;
import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;

public class LayerStatueHeldItem extends LayerHeldItem {
    private EntityStrawStatue statue;

    public LayerStatueHeldItem(RenderLivingBase<?> livingEntityRendererIn) {
        super(livingEntityRendererIn);
    }

    @Override
    public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entity instanceof EntityStrawStatue) {
            statue = (EntityStrawStatue) entity;
        } else {
            statue = null;
        }
        super.doRenderLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    protected void translateToHand(EnumHandSide handSide) {
        if (this.livingEntityRenderer.getMainModel() instanceof ModelStatue) {
            ModelStatue model = (ModelStatue) this.livingEntityRenderer.getMainModel();
            model.bipedBody.postRender(0.0625F);
        }

        ((ModelBiped) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, handSide);

        if (statue != null) {
            GlStateManager.translate(0.0F, -statue.getBodyTranslation(), 0.0F);
        }
    }
}
