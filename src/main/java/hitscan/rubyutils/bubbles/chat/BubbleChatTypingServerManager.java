package hitscan.rubyutils.bubbles.chat;

import hitscan.rubyutils.RubyUtils;
import hitscan.rubyutils.message.MessagePlayerIsTyping;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

public class BubbleChatTypingServerManager {
    private static final HashMap<EntityPlayer, long[]> TYPING_PLAYERS = new HashMap<>();
    private static final long TYPING_TRACK_TIME = 4000;
    private static World world;

    public static void setWorld(World worldIn) {
        world = worldIn;
    }

    public static void setPlayerTyping(EntityPlayer player) {
        long[] receiveAndSend = new long[2];
        //last received packet
        receiveAndSend[0] = System.currentTimeMillis();
        //last sent packet
        receiveAndSend[1] = TYPING_PLAYERS.containsKey(player) ? TYPING_PLAYERS.get(player)[1] : -1;
        TYPING_PLAYERS.put(player, receiveAndSend);
    }

    public static void removePlayerTyping(EntityPlayer player) {
        TYPING_PLAYERS.remove(player);
        BubbleChatTypingServerManager.notify((EntityPlayerMP) player, false);
    }

    public static void run() {
        for (EntityPlayer player : new ArrayList<>(TYPING_PLAYERS.keySet())) {
            if (player instanceof EntityPlayerMP) {
                if (System.currentTimeMillis() - TYPING_PLAYERS.get(player)[0] > TYPING_TRACK_TIME + 1000) {
                    removePlayerTyping(player);
                } else {
                    if (System.currentTimeMillis() - TYPING_PLAYERS.get(player)[1] > TYPING_TRACK_TIME) {
                        notify((EntityPlayerMP) player, true);
                        TYPING_PLAYERS.get(player)[1] = System.currentTimeMillis();
                    }
                }
            } else {
                TYPING_PLAYERS.remove(player);
            }
        }
    }

    public static void clearTypingList() {
        TYPING_PLAYERS.clear();
    }

    public static void notify(EntityPlayerMP player, boolean typing) {
        RubyUtils.NETWORK.sendToAllTracking(new MessagePlayerIsTyping(player.getUniqueID(), typing), player);
        RubyUtils.NETWORK.sendTo(new MessagePlayerIsTyping(player.getUniqueID(), typing), player);
    }
}