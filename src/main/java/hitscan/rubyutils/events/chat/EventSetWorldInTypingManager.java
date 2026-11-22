package hitscan.rubyutils.events.chat;

import hitscan.rubyutils.Tags;
import hitscan.rubyutils.bubbles.chat.BubbleChatTypingClientManager;
import hitscan.rubyutils.bubbles.chat.BubbleChatTypingServerManager;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class EventSetWorldInTypingManager {
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (!event.getWorld().isRemote) {
            BubbleChatTypingServerManager.setWorld(event.getWorld());
            BubbleChatTypingServerManager.clearTypingList();
        } else {
            BubbleChatTypingClientManager.setWorld(event.getWorld());
            BubbleChatTypingClientManager.clearTypingList();
        }
    }

    @SubscribeEvent
    public static void onWordldLoad(PlayerInteractEvent.RightClickBlock event) {

    }
}