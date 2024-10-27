package dev.boxadactle.coordinatesdisplay;

import dev.boxadactle.boxlib.config.BConfig;
import dev.boxadactle.boxlib.config.BConfigFile;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.registry.*;

import java.lang.reflect.Field;

@BConfigFile("coordinatesdisplay")
public class ModConfig implements BConfig {

    public boolean enabled = true;

    public int decimalPlaces = 0;
    public DisplayMode renderMode = DisplayMode.DEFAULT;
    public StartCorner startCorner = StartCorner.TOP_LEFT;
    public VisibilityFilter visibilityFilter = VisibilityFilter.ALWAYS;
    public int hudX = 0;
    public int hudY = 0;
    public float hudScale = 1.0f;
    public boolean hudTextShadow = true;

    public boolean renderBackground = true;
    public boolean renderXYZ = true;
    public boolean renderChunkData = true;
    public boolean renderDirection = true;
    public boolean renderDirectionInt = true;
    public boolean renderBiome = true;
    public boolean biomeColors = true;
    public boolean dimensionColors = true;
    public boolean renderMCVersion = true;
    public boolean renderDimension = true;

    public int definitionColor = GuiUtils.GREEN;
    public int dataColor = GuiUtils.WHITE;
    public int deathPosColor = GuiUtils.AQUA;
    public int backgroundColor = 0x405c5c5c;

    public boolean displayPosOnDeathScreen = true;
    public boolean showDeathPosInChat = true;

    public int padding = 5;
    public int textPadding = 5;

    public String posChatMessage = "{x} {y} {z}";
    public String copyPosMessage = "{x}, {y}, {z}";
    public boolean includeDecimalsWhenCopying = true;
    public TeleportMode teleportMode = TeleportMode.EXECUTE;

    public boolean render3dCompass = false;

    public static void checkValidity(ModConfig config) throws NullPointerException {
        Class<?> clazz = config.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            try {
                if (field.get(config) == null) {
                    throw new NullPointerException("Field " + field.getName() + " is null");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}