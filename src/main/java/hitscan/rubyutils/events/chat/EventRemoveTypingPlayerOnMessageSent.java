package hitscan.rubyutils.events.chat;

import hitscan.rubyutils.Tags;
import hitscan.rubyutils.bubbles.chat.BubbleChatTypingServerManager;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class EventRemoveTypingPlayerOnMessageSent {
    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        BubbleChatTypingServerManager.removePlayerTyping(event.getPlayer());
    }
}