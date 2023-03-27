package me.boxadactle.coordinatesdisplay.util;

import io.github.cottonmc.cotton.config.annotations.ConfigFile;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;

@ConfigFile(name = "./" + CoordinatesDisplay.MOD_ID, extension = ".json")
public class ModConfig {

    public boolean visible = true;
    public boolean roundPosToTwoDecimals = true;

    public boolean minMode = false;
    public int hudX = 0;
    public int hudY = 0;

    public boolean renderBackground = true;
    public boolean renderChunkData = true;
    public boolean renderDirection = true;
    public boolean renderBiome = true;
    public boolean renderDirectionInt = true;
    public boolean renderMCVersion = true;

    public int definitionColor = 0x55FF55;
    public int dataColor = 0xFFFFFF;
    public int deathPosColor = 0x55FFFF;
    public int backgroundColor = 0x405c5c5c;

    public boolean displayPosOnDeathScreen = true;
    public boolean showDeathPosInChat = true;

    public int padding = 5;
    public int textPadding = 15;

    public String posChatMessage = "{x} {y} {z}";
    public String copyPosMessage = "{x}, {y}, {z}";
}
