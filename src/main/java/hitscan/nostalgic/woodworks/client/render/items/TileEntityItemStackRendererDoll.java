package hitscan.nostalgic.woodworks.client.render.items;

import hitscan.nostalgic.woodworks.entity.EntityDollStatue;
import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityItemStackRendererDoll extends TileEntityItemStackRenderer {
    public static final TileEntityItemStackRendererDoll INSTANCE = new TileEntityItemStackRendererDoll();
    private static final NBTTagCompound BLANK_COMPOUND = new NBTTagCompound();
    private EntityDollStatue DOLL;
    private EntityDollStatue DOLL_SKINLESS;

    public void renderByItem(ItemStack itemStackIn, float partialTicks) {
        if (itemStackIn.getItemDamage() != 2) return;

        if (DOLL == null) {
            DOLL = new EntityDollStatue(Minecraft.getMinecraft().world);
            DOLL.doTranslation = false;
        }

        if (DOLL_SKINLESS == null) {
            DOLL_SKINLESS = new EntityDollStatue(Minecraft.getMinecraft().world);
            DOLL_SKINLESS.doTranslation = false;
        }

        DOLL.setPlayerName("");
        DOLL.setPlayerUUID(null);
        DOLL.setFullBodyRotation(0);
        DOLL.setBodyTranslation(0);
        DOLL.setLegsLockedToBody(false);
        DOLL_SKINLESS.setPlayerName("");
        DOLL_SKINLESS.setPlayerUUID(null);
        DOLL_SKINLESS.setFullBodyRotation(0);
        DOLL_SKINLESS.setBodyTranslation(0);
        DOLL_SKINLESS.setLegsLockedToBody(false);
        for (EntityStrawStatue.Part part : EntityStrawStatue.Part.values()) {
            for (EntityStrawStatue.Axis axis : EntityStrawStatue.Axis.values()) {
                DOLL.setLimbRotation(part, axis, 0);
                DOLL_SKINLESS.setLimbRotation(part, axis, 0);
            }
        }

        boolean renderSkinless;

        if (itemStackIn.hasTagCompound()) {
            NBTTagCompound ifYouSeeUsInTheClubWellBeActingRealNiceIfYouSeeUsOnTheFloorYoullBeWatchinAllNight = itemStackIn.getTagCompound();
            renderSkinless = !ifYouSeeUsInTheClubWellBeActingRealNiceIfYouSeeUsOnTheFloorYoullBeWatchinAllNight.hasKey("PlayerName");
            EntityDollStatue.readStatueDataFromNBT(ifYouSeeUsInTheClubWellBeActingRealNiceIfYouSeeUsOnTheFloorYoullBeWatchinAllNight, renderSkinless ? DOLL_SKINLESS : DOLL);
        } else {
            renderSkinless = true;
            EntityDollStatue.readStatueDataFromNBT(BLANK_COMPOUND, DOLL_SKINLESS);
        }

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.translate(0.5D, 0.0D, 0.5D);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.setRenderShadow(false);
        renderManager.renderEntity(renderSkinless ? DOLL_SKINLESS : DOLL, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
        renderManager.setRenderShadow(true);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}
