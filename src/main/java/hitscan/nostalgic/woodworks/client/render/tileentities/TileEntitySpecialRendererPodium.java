package hitscan.nostalgic.woodworks.client.render.tileentities;

import hitscan.nostalgic.woodworks.tileentities.TileEntityPodium;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public class TileEntitySpecialRendererPodium  extends TileEntitySpecialRenderer<TileEntityPodium> {
    private static final RenderItem RENDER_ITEM = Minecraft.getMinecraft().getRenderItem();

    @Override
    public void render(TileEntityPodium podium, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!podium.stack.isEmpty()) {
            boolean isBlock = RENDER_ITEM.getItemModelWithOverrides(podium.stack, podium.getWorld(), null).isGui3d();

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);

            if (!podium.single) {
                for (int i = 0; i < (!isBlock ? (podium.stack.getCount() + 15)/16 : 1); i++) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0.5, !isBlock ? 0.89F + (1/32F * i) : 1, 0.5);
                    double scale = isBlock ? 0.25 : 0.5;
                    GlStateManager.scale(scale, scale, scale);
                    if (!isBlock) GlStateManager.rotate(90, 1.0F, 0, 0);
                    float angleShift = (float) MathHelper.clamp(((double) MathHelper.getPositionRandom(new Vec3i(podium.getPos().getX(), podium.getPos().getY() + i, podium.getPos().getZ())) / Long.MAX_VALUE), -1.0, 1.0);
                    if (!isBlock) {
                        GlStateManager.rotate(podium.facing.getHorizontalAngle() + angleShift * 45F, 0, 0, 1.0F);
                    } else {
                        GlStateManager.rotate(podium.facing.getHorizontalAngle() + angleShift * 45F, 0, 1.0F, 0);
                    }
                    RENDER_ITEM.renderItem(podium.stack, ItemCameraTransforms.TransformType.NONE);
                    GlStateManager.popMatrix();
                }
            } else {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.5, (isBlock ? 18/16F : 19/16F) + MathHelper.sin((Minecraft.getMinecraft().player.ticksExisted + partialTicks) * 0.1F) * 0.04F, 0.5);
                double scale = isBlock ? 0.25 : 0.5;
                GlStateManager.scale(scale, scale, scale);
                float angleShift = (float) MathHelper.clamp(((double) MathHelper.getPositionRandom(new Vec3i(podium.getPos().getX(), podium.getPos().getY(), podium.getPos().getZ())) / Long.MAX_VALUE), -1.0, 1.0);
                GlStateManager.rotate((podium.getWorld().getTotalWorldTime() + partialTicks + (angleShift * 20F)) * 0.02F * 90, 0, 1.0F, 0);
                RENDER_ITEM.renderItem(podium.stack, ItemCameraTransforms.TransformType.NONE);
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
        }
    }
}
