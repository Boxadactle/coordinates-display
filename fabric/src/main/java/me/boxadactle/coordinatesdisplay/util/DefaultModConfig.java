package me.boxadactle.coordinatesdisplay.util;

/*
 * this is all the default config
 * it is never changed throughout the entire mod
 * this is used for resetting all the config
*/

public class DefaultModConfig {

    public static final boolean visible = true;
    public static final boolean roundPosToTwoDecimals = true;

    public static final boolean renderBackground = true;
    public static final boolean renderChunkData = true;
    public static final boolean renderDirection = true;
    public static final boolean renderBiome = true;

    public static final int definitionColor = 0x55FF55;
    public static final int dataColor = 0xFFFFFF;
    public static final int deathPosColor = 0x55FFFF;

    public static final boolean displayPosOnDeathScreen = true;
    public static final boolean showDeathPosInChat = true;

    public static final int padding = 5;
    public static final int textPadding = 10;

    public static final int hudX = 0;
    public static final int hudY = 0;

    public static final String posChatMessage = "{x} {y} {z}";
    public static final String copyPosMessage = "{x}, {y}, {z}";
}
