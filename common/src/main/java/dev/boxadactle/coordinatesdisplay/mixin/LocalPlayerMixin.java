package dev.boxadactle.coordinatesdisplay.mixin;

import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(at = @At("RETURN"), method = "respawn")
    private void respawn(CallbackInfo ci) {
        if (CoordinatesDisplay.CONFIG.get().showDeathPosInChat) {
            Position pos = Position.of(WorldUtils.getCamera());

            CoordinatesDisplay.LOGGER.player.chat(ModUtil.makeDeathPositionComponent(pos));
        }
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void tick(CallbackInfo ci) {
        CoordinatesDisplay.tick();
    }
}