package hitscan.rubyutils.message;

import hitscan.rubyutils.bubbles.chat.BubbleChatTypingClientManager;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class MessagePlayerIsTyping implements IMessage {
    String uuid;
    boolean typing;

    public MessagePlayerIsTyping() {}

    public MessagePlayerIsTyping(UUID uuidIn, boolean typing) {
        uuid = uuidIn.toString();
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

    public static class Handler implements IMessageHandler<MessagePlayerIsTyping, IMessage> {
        @Override
        public IMessage onMessage(MessagePlayerIsTyping message, MessageContext ctx) {
            if (message.typing) {
                BubbleChatTypingClientManager.setPlayerTyping(message.uuid);
            } else {
                BubbleChatTypingClientManager.removePlayerTyping(message.uuid);
            }
            return null;
        }
    }
}
