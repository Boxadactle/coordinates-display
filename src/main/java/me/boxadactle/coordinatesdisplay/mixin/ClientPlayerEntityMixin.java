package me.boxadactle.coordinatesdisplay.mixin;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Shadow @Final protected MinecraftClient client;

    @Inject(at = @At("RETURN"), method = "requestRespawn")
    private void requestRespawn(CallbackInfo ci) {
        if (CoordinatesDisplay.CONFIG.showDeathPosInChat) {
            MinecraftClient c = MinecraftClient.getInstance();

            int x = (int) Math.round(c.player.getX());
            int y = (int) Math.round(c.player.getY());
            int z = (int) Math.round(c.player.getZ());

            String pos = CoordinatesDisplay.DeathposColorPrefix + Text.translatable("message.coordinatesdisplay.location", x, y, z).getString();
            Text deathPos = Text.translatable("message.coordinatesdisplay.deathpos", pos);
            client.player.sendMessage(Text.of(CoordinatesDisplay.CHAT_PREFIX + deathPos.getString()));
        }
    }
}
