package dev.boxadactle.coordinatesdisplay.hud;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Metadata for an Hud renderer.
 * <p></p>
 * This metadata is required to register an Hud renderer.
 *
 * @see dev.boxadactle.coordinatesdisplay.hud.Hud
 * @see HudRenderer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HudDisplayMode {
    /**
     * The id for the renderer.
     * <p></p>
     * Decides the hud renderer's id in the config, when accessing the renderer,
     * and translation key for the translation, definition, and value methods in the HudRenderer interface.
     * <p>Global translation key for renderer will be "hud.coordinatesdisplay.ID." unless one is specified in the translationKey value.</p>
     *
     * @return the key of the renderer
     */
    String value();

    /**
     * The translation key for the renderer.
     * <p></p>
     * Decides the translation key for the translation, definition, and value methods in the HudRenderer interface.
     * Do not override if you want to use the default translation key.
     *
     * @return the translation key of the renderer
     */
    String translationKey() default "";

    /**
     * Whether to ignore position modifier translations
     * <p></p>
     * This is useful for when you want to use a specific location for a renderer
     *
     * @return boolean to enable/disable position modifier translations
     */
    boolean ignoreTranslations() default false;

    /**
     * The class for the position modifier.
     * <p></p>
     * This is useful for when you want to use a specific location for a renderer
     * You must change ignoreTranslations to true in order to use this feature
     *
     * @return the class for the position modifier
     */
    Class<? extends HudPositionModifier.BasicPositionModifier> positionModifier() default HudPositionModifier.Basic.class;

    /**
     * Whether to allow the hud to be moved by the user
     * <p></p>
     * This is useful for when you want a set location for a renderer specified in the code
     *
     * @return boolean to enable/disable hud movements from the user
     */
    boolean allowMove() default true;


    /**
     * Whether the hud has a background
     * <p></p>
     * Specify false to disable the background button from the config screens
     *
     * @return boolean to enable/disable the background config button
     */
    boolean hasBackground() default true;

    /**
     * Whether the hud displays the x, y, and z coordinates
     * <p></p>
     * Specify false to disable the XYZ button from the config screens
     *
     * @return boolean to enable/disable the x, y, and z config buttons
     */
    boolean hasXYZ() default true;

    /**
     * Whether the hud displays the chunk data
     * <p></p>
     * Specify false to disable the chunk data button from the config screens
     *
     * @return boolean to enable/disable the chunk data config button
     */
    boolean hasChunkData() default true;

    /**
     * Whether the hud displays the direction
     * <p></p>
     * Specify false to disable the direction button from the config screens
     *
     * @return boolean to enable/disable the direction config button
     */
    boolean hasDirection() default true;

    /**
     * Whether the hud displays the yaw/pitch integer
     * <p></p>
     * Specify false to disable the direction int button from the config screens
     *
     * @return boolean to enable/disable the direction int config button
     */
    boolean hasDirectionInt() default true;

    /**
     * Whether the hud displays the biome
     * <p></p>
     * Specify false to disable the biome button from the config screens
     *
     * @return boolean to enable/disable the biome config button
     */
    boolean hasBiome() default true;

    /**
     * Whether the hud displays the current minecraft version
     * <p></p>
     * Specify false to disable the minecraft version button from the config screens
     *
     * @return boolean to enable/disable the mc version config button
     */
    boolean hasMCVersion() default true;

    /**
     * Whether the hud displays the current dimension
     * <p></p>
     * Specify false to disable the dimension button from the config screens
     *
     * @return boolean to enable/disable the dimension config button
     */
    boolean hasDimension() default true;
}
