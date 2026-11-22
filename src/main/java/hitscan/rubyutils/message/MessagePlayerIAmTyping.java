package hitscan.rubyutils.message;

import hitscan.rubyutils.bubbles.chat.BubbleChatTypingServerManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class MessagePlayerIAmTyping implements IMessage {
    String uuid;
    boolean typing;

    public MessagePlayerIAmTyping() {}

    public MessagePlayerIAmTyping(UUID uuid, boolean typing) {
        this.uuid = uuid.toString();
        this.typing = typing;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.typing = buf.readBoolean();
        this.uuid = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(typing);
        ByteBufUtils.writeUTF8String(buf, uuid);
    }

    public static class Handler implements IMessageHandler<MessagePlayerIAmTyping, IMessage> {
        @Override
        public IMessage onMessage(MessagePlayerIAmTyping message, MessageContext ctx) {
            EntityPlayer typingEntity = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(UUID.fromString(message.uuid));
            if (message.typing) {
                BubbleChatTypingServerManager.setPlayerTyping(typingEntity);
            } else {
                BubbleChatTypingServerManager.removePlayerTyping(typingEntity);
            }
            return null;
        }
    }
}
