package hitscan.rubyutils.bubbles.chat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.*;

public class BubbleChatTypingClientManager {
    public static long lastTrackingPacket = 0;
    public static final long TIME_BETWEEN_PACKETS = 4000;
    private static final long TIME_TO_PRUNE = 10000;
    private static final HashMap<String, Long> TYPING_PLAYERS = new HashMap<>();
    private static final List<EntityPlayer> TYPING_PLAYERS_AS_ENTITIES = new ArrayList<>();
    private static World world;

    public static void setWorld(World worldIn) {
        world = worldIn;
    }

    public static List<EntityPlayer> getTyperList() {
        return new ArrayList<>(TYPING_PLAYERS_AS_ENTITIES);
    }

    public static boolean isPlayerTyping(EntityPlayer player) {
        return TYPING_PLAYERS_AS_ENTITIES.contains(player);
    }

    public static void setPlayerTyping(String player) {
        TYPING_PLAYERS.put(player, System.currentTimeMillis());
    }

    public static void removePlayerTyping(String player) {
        TYPING_PLAYERS.remove(player);
    }

    public static void run() {
        if (world != null) {
            TYPING_PLAYERS_AS_ENTITIES.clear();
            Iterator<Map.Entry<String, Long>> iterator = TYPING_PLAYERS.entrySet().iterator();
            while (iterator.hasNext()) {
                String player = iterator.next().getKey();
                if (System.currentTimeMillis() - TYPING_PLAYERS.get(player) > TIME_TO_PRUNE) {
                    iterator.remove();
                } else {
                    TYPING_PLAYERS_AS_ENTITIES.add(world.getPlayerEntityByUUID(UUID.fromString(player)));
                }
            }
        }
    }

    public static void clearTypingList() {
        TYPING_PLAYERS.clear();
    }
}
