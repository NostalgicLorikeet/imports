package hitscan.nostalgic.woodworks.client.render.tileentities;

import hitscan.nostalgic.woodworks.tileentities.TileEntityShelf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntitySpecialRendererShelf extends TileEntitySpecialRenderer<TileEntityShelf> {
    private static final RenderItem RENDER_ITEM = Minecraft.getMinecraft().getRenderItem();

    @Override
    public void render(TileEntityShelf shelf, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (shelf.canPlaceItemStacksIn() && shelf.rotation != null) {
            double distanceSq = (x * x) + (y * y) + (z * z);
            double drawDistance = (Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 1.0);
            double drawDistanceSq = drawDistance * drawDistance;
            boolean drawAllStacks = distanceSq < drawDistanceSq;

            EnumFacing facing = shelf.rotation;

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);

            for (int i = 0; i < 3; i++) {
                ItemStack stack = shelf.stacks[i];
                if (stack.isEmpty()) continue;
                boolean isBlock = RENDER_ITEM.getItemModelWithOverrides(stack, shelf.getWorld(), null).isGui3d();
                int maxIconCount = 16; //this is actually how many items each icon represents butim too lazy to change the variable name
                float iconSpace = isBlock ? 0.08F : 0.04F;

                int iconCount = (stack.getCount() + maxIconCount - 1) / maxIconCount;
                for (int a = (drawAllStacks ? 0 : Math.max(iconCount, 1) - 1); a < iconCount; a++) {
                    GlStateManager.pushMatrix();

                    float xTranslate = facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH ? i / 3.0F + 1F / 6F : (a + 1) * iconSpace;
                    float zTranslate = facing == EnumFacing.EAST || facing == EnumFacing.WEST ? i / 3.0F + 1F / 6F : (a + 1) * iconSpace;
                    if (facing == EnumFacing.SOUTH) zTranslate = 1 - zTranslate;
                    if (facing == EnumFacing.EAST) xTranslate = 1 - xTranslate;

                    GlStateManager.translate(xTranslate, isBlock ? 0.45F - (a * 0.001F) : 0.575F, zTranslate);
                    GlStateManager.scale(0.85F, 0.85F, 0.85F);
                    GlStateManager.rotate(facing.getHorizontalAngle(), 0, 1.0F, 0);
                    //float angleShift = (float) MathHelper.clamp(((double) MathHelper.getPositionRandom(new Vec3i(shelf.getPos().getX(), shelf.getPos().getY() + i, shelf.getPos().getZ())) / Long.MAX_VALUE), -1.0, 1.0);
                    float angle = isBlock ? 45F : facing == EnumFacing.NORTH || facing == EnumFacing.EAST ? 11.25F : 168.75F;
                    GlStateManager.rotate(angle, 0, 1.0F, 0);

                    RENDER_ITEM.renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
                    GlStateManager.popMatrix();
                }
            }
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean isGlobalRenderer(TileEntityShelf shelf) {
        return false;
    }
}
