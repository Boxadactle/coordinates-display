package dev.boxadactle.coordinatesdisplay.mixin;

import dev.boxadactle.coordinatesdisplay.util.position.Position;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Shadow @Final protected MinecraftClient client;

    @Inject(at = @At("RETURN"), method = "tick")
    private void tick(CallbackInfo ci) {
        CoordinatesDisplay.tick();
    }

    @Inject(at = @At("RETURN"), method = "requestRespawn")
    private void requestRespawn(CallbackInfo ci) {
        if (CoordinatesDisplay.CONFIG.get().showDeathPosInChat) {
            MinecraftClient c = MinecraftClient.getInstance();

            Position pos = Position.of(WorldUtils.getCamera());

            CoordinatesDisplay.LOGGER.player.chat(ModUtil.makeDeathPositionText(pos));
        }
    }
}
