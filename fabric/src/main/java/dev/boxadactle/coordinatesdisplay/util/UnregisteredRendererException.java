package dev.boxadactle.coordinatesdisplay.util;

public class UnregisteredRendererException extends RuntimeException {

    public UnregisteredRendererException(ModConfig.RenderMode renderMode) {
        super("No HUD Renderer has been registered to display mode \"" + renderMode.name() + "\"");
    }

}