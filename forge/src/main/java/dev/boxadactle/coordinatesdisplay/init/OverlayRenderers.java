package dev.boxadactle.coordinatesdisplay.init;

import dev.boxadactle.coordinatesdisplay.util.hud.AllRenderer;
import dev.boxadactle.coordinatesdisplay.util.hud.LineRenderer;
import dev.boxadactle.coordinatesdisplay.util.hud.MinRenderer;
import dev.boxadactle.coordinatesdisplay.util.HudRenderer;
import dev.boxadactle.coordinatesdisplay.util.ModConfig;

public class OverlayRenderers {

    public static void registerOverlayRenderers(HudRenderer renderer) {
        renderer.register(ModConfig.RenderMode.DEFAULT, new AllRenderer());
        renderer.register(ModConfig.RenderMode.MINIMUM, new MinRenderer());
        renderer.register(ModConfig.RenderMode.LINE, new LineRenderer());
    }

}
