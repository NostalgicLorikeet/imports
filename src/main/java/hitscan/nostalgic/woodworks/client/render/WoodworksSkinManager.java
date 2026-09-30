package hitscan.nostalgic.woodworks.client.render;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.UUID;

public class WoodworksSkinManager {
    public static final HashMap<UUID, ResourceLocation> UUID_TO_SKIN_LIST = new HashMap<>();
    public static final HashMap<UUID, Boolean> SKIN_USE_SLIM = new HashMap<>();

    public static ResourceLocation getSkinLocation(UUID uuid) {
        if (!UUID_TO_SKIN_LIST.containsKey(uuid)) {
            UUID_TO_SKIN_LIST.put(uuid, DefaultPlayerSkin.getDefaultSkin(uuid));
            SKIN_USE_SLIM.put(uuid, (uuid.hashCode() & 1) == 1);

            Minecraft.getMinecraft().addScheduledTask(() -> {
                try {
                    GameProfile profile = new GameProfile(uuid, null);
                    Minecraft.getMinecraft().getSessionService().fillProfileProperties(profile, true);

                    System.out.println("Trying to download skin from ID "+ uuid);

                    Minecraft.getMinecraft().getSkinManager().loadProfileTextures(profile, (typeIn, location, profileTexture) -> {
                        if (typeIn == MinecraftProfileTexture.Type.SKIN) {
                            UUID_TO_SKIN_LIST.put(uuid, location);
                            if (profileTexture.getMetadata("model") != null) {
                                SKIN_USE_SLIM.put(uuid, profileTexture.getMetadata("model").equals("slim"));
                            } else {
                                SKIN_USE_SLIM.put(uuid, false);
                            }
                        }
                    }, true);
                } catch (Exception ignored) {}
            });
        }

        return UUID_TO_SKIN_LIST.get(uuid);
    }
}
