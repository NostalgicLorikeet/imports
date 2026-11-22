package hitscan.rubyutils.client.render.entities.layers;

import hitscan.rubyutils.Tags;
import hitscan.rubyutils.client.render.entities.render.RenderEncrusted;
import hitscan.rubyutils.entity.EntityEncrusted;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class LayerEncrustedGem implements LayerRenderer<EntityEncrusted> {
    private final RenderEncrusted renderer;
    private static final ResourceLocation GEM_TEXTURE = new ResourceLocation(Tags.MOD_ID, "textures/entity/encrusted_gem.png");

    final ModelRenderer ENCRUSTED_HEAD;
    final ModelRenderer ENCRUSTED_TORSO;
    final ModelRenderer ENCRUSTED_LEFT_ARM;
    final ModelRenderer ENCRUSTED_RIGHT_ARM;
    final ModelRenderer ENCRUSTED_LEFT_LEG;
    final ModelRenderer ENCRUSTED_RIGHT_LEG;

    final float[] xTranslation;
    final float[] yTranslation;
    final float[] zTranslation;
    final ModelRenderer[] part;

    public LayerEncrustedGem(RenderEncrusted rendererIn) {
        this.renderer = rendererIn;
        this.ENCRUSTED_HEAD = ((ModelBiped) this.renderer.getMainModel()).bipedHead;
        this.ENCRUSTED_TORSO = ((ModelBiped) this.renderer.getMainModel()).bipedBody;
        this.ENCRUSTED_LEFT_ARM = ((ModelBiped) this.renderer.getMainModel()).bipedLeftArm;
        this.ENCRUSTED_RIGHT_ARM = ((ModelBiped) this.renderer.getMainModel()).bipedRightArm;
        this.ENCRUSTED_LEFT_LEG = ((ModelBiped) this.renderer.getMainModel()).bipedLeftLeg;
        this.ENCRUSTED_RIGHT_LEG = ((ModelBiped) this.renderer.getMainModel()).bipedRightLeg;

        xTranslation = new float[]{0.0F, 0.0F};
        yTranslation = new float[]{-0.25F, -0.25F};
        zTranslation = new float[]{-0.35F, 0.35F};
        part = new ModelRenderer[]{ENCRUSTED_HEAD, ENCRUSTED_HEAD};
    }

    @Override
    public void doRenderLayer(EntityEncrusted encrustedIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (encrustedIn.isInvisible()) return;

        Minecraft.getMinecraft().getTextureManager().bindTexture(GEM_TEXTURE);

        for (int i = 0; i < xTranslation.length; i++) {
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

            float time = encrustedIn.ticksExisted + partialTicks;
            float pulse = (float) ((float) (Math.sin(time * 0.015D) + 1.0D) * 0.5D);
            float lightmapCoord = 0.0F + (pulse * 240.0F);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapCoord, lightmapCoord);

            if (this.renderer.getMainModel().isChild) {
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
                GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            }

            part[i].postRender(scale);

            float spriteSize = 0.25F;
            GlStateManager.translate(xTranslation[i], yTranslation[i], zTranslation[i]);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(spriteSize, spriteSize, spriteSize);

            for (int a = 0; a < 2; a++) {
                GlStateManager.color(1.0F, 1.0F, 0.0F, 1.0F);
                GlStateManager.rotate(90.0F, 0.0F, a == 0 ? -1.0F : 1.0F, 0.0F);

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();

                buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
                buffer.pos(-0.5D, -0.5D, 0.0D).tex(0.0D, 1.0D).endVertex();
                buffer.pos(0.5D, -0.5D, 0.0D).tex(1.0D, 1.0D).endVertex();
                buffer.pos(0.5D, 0.5D, 0.0D).tex(1.0D, 0.0D).endVertex();
                buffer.pos(-0.5D, 0.5D, 0.0D).tex(0.0D, 0.0D).endVertex();

                tessellator.draw();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
