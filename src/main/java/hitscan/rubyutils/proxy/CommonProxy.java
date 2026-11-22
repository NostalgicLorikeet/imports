package hitscan.rubyutils.proxy;

import hitscan.rubyutils.RubyUtils;
import hitscan.rubyutils.message.MessagePlayerIAmTyping;
import hitscan.rubyutils.message.MessagePlayerIsTyping;
import hitscan.rubyutils.message.MessageStatueUpdateSkin;
import hitscan.rubyutils.init.RubyUtilsTileEntities;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
    public void preInit() {
        RubyUtilsTileEntities.registerTileEntities();
    }

    public void init() {
        RubyUtils.NETWORK.registerMessage(MessageStatueUpdateSkin.Handler.class, MessageStatueUpdateSkin.class, 0, Side.SERVER);
        RubyUtils.NETWORK.registerMessage(MessagePlayerIAmTyping.Handler.class, MessagePlayerIAmTyping.class, 1, Side.SERVER);
        RubyUtils.NETWORK.registerMessage(MessagePlayerIsTyping.Handler.class, MessagePlayerIsTyping.class, 2, Side.CLIENT);
    }
}
