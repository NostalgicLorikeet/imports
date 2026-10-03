package hitscan.nostalgic.woodworks.client.render;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import hitscan.nostalgic.woodworks.Woodworks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class WoodworksSkinManager {
    public static final HashMap<UUID, ResourceLocation> UUID_TO_SKIN = new HashMap<>();
    public static final HashMap<UUID, Boolean> SKIN_USE_SLIM = new HashMap<>();

    public static final HashMap<UUID, GameProfile> UUID_TO_GAME_PROFILE = new HashMap<>();

    public static ResourceLocation getSkinLocation(UUID uuid) {
        if (!UUID_TO_SKIN.containsKey(uuid)) {
            UUID_TO_SKIN.put(uuid, DefaultPlayerSkin.getDefaultSkin(uuid));
            SKIN_USE_SLIM.put(uuid, (uuid.hashCode() & 1) == 1);

            Minecraft.getMinecraft().addScheduledTask(() -> {
                try {
                    GameProfile profile;

                    if (UUID_TO_GAME_PROFILE.containsKey(uuid)) {
                        profile = UUID_TO_GAME_PROFILE.get(uuid);
                    } else {
                        profile = new GameProfile(uuid, null);
                        UUID_TO_GAME_PROFILE.put(uuid, profile);
                    }

                    if (profile.getName() == null) {
                        Minecraft.getMinecraft().getSessionService().fillProfileProperties(profile, true);
                    }

                    Woodworks.LOGGER.info("Trying to download skin from ID {}", uuid);

                    Minecraft.getMinecraft().getSkinManager().loadProfileTextures(profile, (typeIn, location, profileTexture) -> {
                        if (typeIn == MinecraftProfileTexture.Type.SKIN) {
                            UUID_TO_SKIN.put(uuid, location);
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

        return UUID_TO_SKIN.get(uuid);
    }
}