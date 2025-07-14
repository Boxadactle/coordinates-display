package dev.boxadactle.coordinatesdisplay.mixin;

import com.google.gson.stream.MalformedJsonException;
import com.mojang.authlib.GameProfile;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.marking.MarkSerializer;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.PlayerChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatListener.class)
public class ChatListenerMixin {

//    @Inject(
//            method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    public void checkMessages(Component chatComponent, MessageSignature headerSignature, GuiMessageTag tag, CallbackInfo ci) {
//        try {
//            Vec3<Integer> mark = MarkSerializer.deserialize(chatComponent.getString());
//
//            ci.cancel();
//            CoordinatesDisplay.LOGGER.player.chat(ModUtil.makeMarkComponent(mark, ));
//        } catch (MalformedJsonException e) {
//            // this message is not a shared mark
//        }
//    }

    @Inject(
            method = "handlePlayerChatMessage",
            at = @At("HEAD"),
            cancellable = true
    )
    public void checkMessages(PlayerChatMessage chatMessage, GameProfile gameProfile, ChatType.Bound boundChatType, CallbackInfo ci) {
        try {
            Vec3<Integer> mark = MarkSerializer.deserialize(chatMessage.decoratedContent().getString());

            ci.cancel();

            CoordinatesDisplay.LOGGER.player.chat(ModUtil.makeMarkComponent(mark, gameProfile.getName()));
        } catch (MalformedJsonException e) {
            // this message is not a shared mark
        }
    }

}
