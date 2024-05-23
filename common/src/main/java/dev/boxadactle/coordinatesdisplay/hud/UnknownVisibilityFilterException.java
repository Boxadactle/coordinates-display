package dev.boxadactle.coordinatesdisplay.hud;

/**
 * Thrown when attempting to use a visibility filter that has not been registered.
 *
 * @since 10.0.0
 * @see HudVisibilityFilter
 * @see Hud
 */
public class UnknownVisibilityFilterException extends RuntimeException {

    public UnknownVisibilityFilterException(String filter) {
        super("Could not find overlay with id " + filter + "!");
    }

}
