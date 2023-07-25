package dev.boxadactle.coordinatesdisplay.mixin;

import dev.boxadactle.coordinatesdisplay.util.position.Position;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;

    // need to render the overlay in the render method
    @Inject(at = @At("RETURN"), method = "render")
    private void render(DrawContext drawContext, float tickDelta, CallbackInfo ci) {
        if (
                !this.client.options.hudHidden &&
                CoordinatesDisplay.CONFIG.get().visible &&
                !this.client.options.debugEnabled &&
                CoordinatesDisplay.shouldHudRender
        ) {
            try {
                CoordinatesDisplay.OVERLAY.render(
                        drawContext,
                        CoordinatesDisplay.CONFIG.get().hudX,
                        CoordinatesDisplay.CONFIG.get().hudY,
                        Position.of(WorldUtils.getCamera()),
                        CoordinatesDisplay.CONFIG.get().renderMode,
                        false,
                        CoordinatesDisplay.CONFIG.get().hudScale
                );
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
