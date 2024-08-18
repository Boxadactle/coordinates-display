package dev.boxadactle.coordinatesdisplay.hud;

/**
 * Thrown when attempting to render an overlay that has not been registered.
 *
 * @since 6.0.0
 * @see HudRenderer
 * @see Hud
 */
public class UnknownRendererException extends RuntimeException {

    public UnknownRendererException(String renderMode) {
        super("Could not find overlay with id " + renderMode + "!");
    }

}
