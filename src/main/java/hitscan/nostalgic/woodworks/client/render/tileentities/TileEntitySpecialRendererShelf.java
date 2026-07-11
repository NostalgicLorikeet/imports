package hitscan.nostalgic.woodworks.client.render.tileentities;

import hitscan.nostalgic.woodworks.tileentities.TileEntityShelf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

public class TileEntitySpecialRendererShelf extends TileEntitySpecialRenderer<TileEntityShelf> {
    @Override
    public void render(TileEntityShelf shelf, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (shelf.canPlaceItemStacksIn() && shelf.rotation != null) {
            EnumFacing facing = shelf.rotation;

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

            for (int i = 0; i < 3; i++) {
                if (shelf.stacks[i].isEmpty()) continue;
                boolean isBlock = renderItem.getItemModelWithOverrides(shelf.stacks[i], shelf.getWorld(), null).isGui3d();
                int maxIconCount = isBlock ? 16 : 8; //this is actually how many items each icon represents butim too lazy to change the variable name
                float iconSpace = isBlock ? 0.08F : 0.04F;

                int iconCount = (shelf.stacks[i].getCount() + maxIconCount - 1) / maxIconCount;
                for (int a = 0; a < iconCount; a++) {
                    try {
                        GlStateManager.pushMatrix();

                        float xTranslate = facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH ? i / 3.0F + 1F / 6F : (a + 1) * iconSpace;
                        float zTranslate = facing == EnumFacing.EAST || facing == EnumFacing.WEST ? i / 3.0F + 1F / 6F : (a + 1) * iconSpace;
                        if (facing == EnumFacing.SOUTH) zTranslate = 1 - zTranslate;
                        if (facing == EnumFacing.EAST) xTranslate = 1 - xTranslate;

                        GlStateManager.translate(xTranslate, isBlock ? 0.45F - (a * 0.001F) : 0.575F, zTranslate);
                        GlStateManager.scale(0.85F, 0.85F, 0.85F);
                        GlStateManager.rotate(facing.getHorizontalAngle(), 0, 1.0F, 0);
                        //float angleShift = (float) MathHelper.clamp(((double) MathHelper.getPositionRandom(new Vec3i(shelf.getPos().getX(), shelf.getPos().getY() + i, shelf.getPos().getZ())) / Long.MAX_VALUE), -1.0, 1.0);
                        if (iconCount > 1 || isBlock) {
                            float angle = isBlock ? 45F : facing == EnumFacing.NORTH || facing == EnumFacing.EAST ? 11.25F : 168.75F;
                            GlStateManager.rotate(angle, 0, 1.0F, 0);
                        }
                        renderItem.renderItem(shelf.stacks[i], ItemCameraTransforms.TransformType.GROUND);

                        GlStateManager.popMatrix();
                    } catch (Exception ignored) {
                        GlStateManager.popMatrix();
                    }
                }
            }

            GlStateManager.popMatrix();
        }
    }
}
