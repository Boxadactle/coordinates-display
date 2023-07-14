package dev.boxadactle.coordinatesdisplay.mixin;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.util.ModConfig;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeGui.class)
public class ForgeGuiMixin {

    @Inject(at = @At("RETURN"), method = "render")
    private void render(GuiGraphics guiGraphics, float tickDelta, CallbackInfo ci) {
        if (!ClientUtils.getOptions().hideGui && CoordinatesDisplay.CONFIG.get().visible && !ClientUtils.getOptions().renderDebug) {
            try {
                ModConfig config = CoordinatesDisplay.getConfig();

                CoordinatesDisplay.OVERLAY.render(
                        guiGraphics,
                        Position.of(WorldUtils.getCamera()),
                        config.hudX,
                        config.hudY,
                        config.renderMode,
                        false,
                        config.hudScale
                );
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

}
