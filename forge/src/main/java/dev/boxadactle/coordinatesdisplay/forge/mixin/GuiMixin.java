package dev.boxadactle.coordinatesdisplay.forge.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModConfig;
import dev.boxadactle.coordinatesdisplay.forge.CoordinatesDisplayForge;
import dev.boxadactle.coordinatesdisplay.hud.Hud;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.LayeredDraw;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "<init>", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void init(Minecraft arg, CallbackInfo ci, LayeredDraw drawer) {
        drawer.add((graphics, f) -> {
            try {
                if (CoordinatesDisplay.HUD.shouldRender(CoordinatesDisplay.getConfig().visibilityFilter)) {
                    RenderSystem.enableBlend();

                    ModConfig config = CoordinatesDisplay.getConfig();

                    CoordinatesDisplay.HUD.render(
                            graphics,
                            Hud.RenderType.HUD,
                            Position.of(WorldUtils.getPlayer()),
                            config.hudX,
                            config.hudY,
                            config.renderMode,
                            config.startCorner,
                            config.hudScale
                    );
                }
            } catch (NullPointerException e) {
                if (CoordinatesDisplayForge.deltaError) {
                    throw new RuntimeException(e);
                }

                CoordinatesDisplay.LOGGER.error("Unknown error from config file");
                CoordinatesDisplay.LOGGER.printStackTrace(e);

                CoordinatesDisplay.LOGGER.player.warn(GuiUtils.getTranslatable("message.coordinatesdisplay.configError"));
                CoordinatesDisplay.CONFIG.resetConfig();

                CoordinatesDisplayForge.deltaError = true;
            }
        });
    }

}
