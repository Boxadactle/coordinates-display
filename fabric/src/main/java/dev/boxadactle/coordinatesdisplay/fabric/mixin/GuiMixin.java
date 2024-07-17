package dev.boxadactle.coordinatesdisplay.fabric.mixin;

import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.fabric.CoordinatesDisplayFabric;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void renderHud(float f, CallbackInfo ci) {
        try {
            if (CoordinatesDisplay.HUD.shouldRender(CoordinatesDisplay.getConfig().visibilityFilter)) {
                ModConfig config = CoordinatesDisplay.getConfig();

                CoordinatesDisplay.HUD.render(
                        Position.of(WorldUtils.getPlayer()),
                        config.hudX,
                        config.hudY,
                        config.renderMode,
                        config.startCorner,
                        false,
                        config.hudScale
                );
            }
        } catch (NullPointerException e) {
            if (CoordinatesDisplayFabric.deltaError) {
                throw new RuntimeException(e);
            }

            CoordinatesDisplay.LOGGER.error("Unknown error from config file");
            CoordinatesDisplay.LOGGER.printStackTrace(e);

            CoordinatesDisplay.LOGGER.player.warn(GuiUtils.getTranslatable("message.coordinatesdisplay.configError"));
            CoordinatesDisplay.CONFIG.resetConfig();

            CoordinatesDisplayFabric.deltaError = true;
        }
    }

}
