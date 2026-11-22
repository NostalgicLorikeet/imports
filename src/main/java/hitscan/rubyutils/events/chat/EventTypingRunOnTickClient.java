package hitscan.rubyutils.events.chat;

import hitscan.rubyutils.RubyUtils;
import hitscan.rubyutils.Tags;
import hitscan.rubyutils.bubbles.chat.BubbleChatTypingClientManager;
import hitscan.rubyutils.message.MessagePlayerIAmTyping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class EventTypingRunOnTickClient {
    private static boolean wasChatOpen = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            BubbleChatTypingClientManager.run();
            boolean isChatOpen = Minecraft.getMinecraft().currentScreen instanceof GuiChat;

            if (wasChatOpen && !isChatOpen) {
                if (BubbleChatTypingClientManager.isPlayerTyping(Minecraft.getMinecraft().player)) {
                    RubyUtils.NETWORK.sendToServer(new MessagePlayerIAmTyping(Minecraft.getMinecraft().player.getUniqueID(), false));
                    BubbleChatTypingClientManager.removePlayerTyping(Minecraft.getMinecraft().player.getUniqueID().toString());
                }
            }

            wasChatOpen = isChatOpen;
        }
    }
}
