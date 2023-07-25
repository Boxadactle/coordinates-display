package dev.boxadactle.coordinatesdisplay.init;

import dev.boxadactle.coordinatesdisplay.util.HudRenderer;
import dev.boxadactle.coordinatesdisplay.util.ModConfig;
import dev.boxadactle.coordinatesdisplay.util.hud.MaxRenderer;
import dev.boxadactle.coordinatesdisplay.util.hud.MinRenderer;
import dev.boxadactle.coordinatesdisplay.util.hud.DefaultRenderer;
import dev.boxadactle.coordinatesdisplay.util.hud.LineRenderer;

public class OverlayRenderers {

    public static void registerOverlayRenderers(HudRenderer renderer) {
        renderer.register(ModConfig.RenderMode.DEFAULT, DefaultRenderer.class);
        renderer.register(ModConfig.RenderMode.MINIMUM, MinRenderer.class);
        renderer.register(ModConfig.RenderMode.MAXIMUM, MaxRenderer.class);
        renderer.register(ModConfig.RenderMode.LINE, LineRenderer.class);
    }

}
