package hitscan.rubyutils.events.chat;

import hitscan.rubyutils.Tags;
import hitscan.rubyutils.bubbles.chat.BubbleChatTypingClientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class EventRenderTypingOverly {
    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getMinecraft();
            GuiNewChat chat = mc.ingameGUI.getChatGUI();

            int xCoord = 4;
            int yCoord = event.getResolution().getScaledHeight() - 11;

            if (mc.currentScreen instanceof GuiChat) {
                yCoord -= 12;
            }

            List<EntityPlayer> typerList = BubbleChatTypingClientManager.getTyperList();
            if (!typerList.isEmpty()) {
                //please make translatable
                StringBuilder chatters = new StringBuilder(" typing");
                chatters.insert(0, (typerList.size() == 1 ? " is" : " are"));

                if (typerList.size() == 1) {
                    chatters.insert(0, typerList.get(0).getName());
                } else if (typerList.size() == 2) {
                    chatters.insert(0, typerList.get(0).getName() + " and " + typerList.get(1).getName());
                } else {
                    StringBuilder chatterList = new StringBuilder();
                    for (int i = 0; i < typerList.size(); i++) {
                        if (i != typerList.size() - 1) {
                            chatterList.append(typerList.get(i).getName()).append(", ");
                        } else {
                            chatterList.append("and ").append(typerList.get(i).getName());
                        }
                    }
                    chatters.insert(0, chatterList);
                }

                String[] dotStates = {"", ".", "..", "..."};
                String dots = dotStates[(int) ((System.currentTimeMillis() / 250) % dotStates.length)];
                chatters.append(dots);

                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(
                        GlStateManager.SourceFactor.SRC_ALPHA,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                        GlStateManager.SourceFactor.ONE,
                        GlStateManager.DestFactor.ZERO
                );
                Minecraft.getMinecraft().fontRenderer.drawString(chatters.toString(), xCoord, yCoord, mc.currentScreen instanceof GuiChat ? 0xFFFFFFFF : 0x7FFFFFFF);
                GlStateManager.scale(chat.getChatScale(), chat.getChatScale(), 1.0F);
                GlStateManager.popMatrix();
            }
        }
    }
}