package hitscan.nostalgic.woodworks.message;

import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageStatueUpdateSkin implements IMessage {
    int statue;
    String name;

    public MessageStatueUpdateSkin() {}

    public MessageStatueUpdateSkin(int statue, String name) {
        this.statue = statue;
        this.name = name;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.statue = buf.readInt();
        this.name = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(statue);
        ByteBufUtils.writeUTF8String(buf, name);
    }

    public static class Handler implements IMessageHandler<MessageStatueUpdateSkin, IMessage> {
        @Override
        public IMessage onMessage(MessageStatueUpdateSkin message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                Entity entity = ctx.getServerHandler().player.world.getEntityByID(message.statue);
                if (entity instanceof EntityStrawStatue) {
                    EntityStrawStatue entityStrawStatue = (EntityStrawStatue) entity;
                    if (!message.name.isEmpty()) {
                        entityStrawStatue.setPlayerName(message.name);
                    } else {
                        entityStrawStatue.resetPlayerInfo();
                    }
                }
            });
            return null;
        }
    }
}
