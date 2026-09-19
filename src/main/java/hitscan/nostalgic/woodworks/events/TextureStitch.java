package hitscan.nostalgic.woodworks.events;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureStitch {
    @SubscribeEvent
    public static void textureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {
            for (int i = 0; i < 256; i++) {
                event.getMap().registerSprite(new ResourceLocation("woodworks","font/ascii_glyph_"+i));
            }
        }
    }
}
