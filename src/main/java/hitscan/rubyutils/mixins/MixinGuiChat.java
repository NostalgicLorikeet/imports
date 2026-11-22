package hitscan.rubyutils.mixins;

import hitscan.rubyutils.RubyUtils;
import hitscan.rubyutils.bubbles.chat.BubbleChatTypingClientManager;
import hitscan.rubyutils.message.MessagePlayerIAmTyping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(GuiChat.class)
public class MixinGuiChat {
    String textFieldArchive = "";

    @Shadow
    protected GuiTextField inputField;

    @Inject(method = "keyTyped", at = @At("TAIL"))
    protected void onKeyTyped(char typedChar, int keyCode, CallbackInfo ci) throws IOException {
        if (this.inputField != null) {
            if (!this.inputField.getText().startsWith("/") && !this.inputField.getText().isEmpty() && !this.inputField.getText().equals(textFieldArchive) && false) {
                if (System.currentTimeMillis() - BubbleChatTypingClientManager.lastTrackingPacket > BubbleChatTypingClientManager.TIME_BETWEEN_PACKETS) {
                    RubyUtils.NETWORK.sendToServer(new MessagePlayerIAmTyping(Minecraft.getMinecraft().player.getUniqueID(), true));
                    BubbleChatTypingClientManager.lastTrackingPacket = System.currentTimeMillis();
                    BubbleChatTypingClientManager.setPlayerTyping(Minecraft.getMinecraft().player.getUniqueID().toString());
                    textFieldArchive = this.inputField.getText();
                }
            }
        }
    }
}
