package hitscan.rubyutils.events;

import hitscan.rubyutils.bubbles.chat.BubbleChatTypingClientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventBubbleRenderer {
    public static final ItemStack BOOK_STACK = new ItemStack(Items.WRITABLE_BOOK);

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<EntityLivingBase> event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (BubbleChatTypingClientManager.isPlayerTyping(player)) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(event.getX(), event.getY() + player.height + 0.4D, event.getZ());
                GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate((event.getEntity().getEntityWorld().getTotalWorldTime() + event.getPartialRenderTick() + (20F)) * 0.02F * 90, 0, 1.0F, 0);
                GlStateManager.scale(1.0F, 1.0F, 1.0F);
                Minecraft.getMinecraft().getRenderItem().renderItem(BOOK_STACK, ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.popMatrix();
            }
        }
    }
}