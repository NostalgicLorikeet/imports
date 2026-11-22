package hitscan.rubyutils.events.chat;

import hitscan.rubyutils.Tags;
import hitscan.rubyutils.bubbles.chat.BubbleChatTypingServerManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class EventTypingRunOnTickServer {
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            BubbleChatTypingServerManager.run();
        }
    }
}