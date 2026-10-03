package hitscan.nostalgic.woodworks.proxy;

import hitscan.nostalgic.woodworks.Woodworks;
import hitscan.nostalgic.woodworks.message.MessageStatueUpdateSkin;
import hitscan.nostalgic.woodworks.registry.WoodworksTileEntities;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
    public void preInit() {
        WoodworksTileEntities.regsiterTileEntities();
    }

    public void init() {
        Woodworks.NETWORK.registerMessage(MessageStatueUpdateSkin.Handler.class, MessageStatueUpdateSkin.class, 0, Side.SERVER);
    }
}
