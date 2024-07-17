package dev.boxadactle.coordinatesdisplay.config;

import dev.boxadactle.boxlib.config.BConfig;
import dev.boxadactle.boxlib.config.BConfigFile;
import dev.boxadactle.coordinatesdisplay.registry.DisplayMode;
import dev.boxadactle.coordinatesdisplay.registry.StartCorner;
import dev.boxadactle.coordinatesdisplay.registry.TeleportMode;
import dev.boxadactle.coordinatesdisplay.registry.VisibilityFilter;
import net.minecraft.ChatFormatting;

@BConfigFile("coordinates-display")
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

    public ChatFormatting definitionColor = ChatFormatting.GREEN;
    public ChatFormatting dataColor = ChatFormatting.WHITE;
    public ChatFormatting deathPosColor = ChatFormatting.AQUA;
    public int backgroundColor = 0x405c5c5c;

    public boolean displayPosOnDeathScreen = true;
    public boolean showDeathPosInChat = true;

    public int padding = 5;
    public int textPadding = 5;

    public String posChatMessage = "{x} {y} {z}";
    public String copyPosMessage = "{x}, {y}, {z}";
    public boolean includeDecimalsWhenCopying = true;
    public TeleportMode teleportMode = TeleportMode.EXECUTE;

}