package me.boxadactle.coordinatesdisplay.util;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "./" + CoordinatesDisplay.MOD_ID)
public class ModConfig implements ConfigData {

    public boolean visible = true;
    public boolean decimalRounding = true;
    public int hudX = 0;
    public int hudY = 0;

    public boolean renderBackground = true;
    public boolean renderChunkData = true;
    public boolean renderDirection = true;
    public boolean renderBiome = true;

    public String definitionColor = "green";
    public String dataColor = "white";
    public String deathPosColor = "aqua";

    public boolean displayPosOnDeathScreen = true;
    public boolean showDeathPosInChat = true;

    public int padding = 5;
    public int textPadding = 10;

    public String posChatMessage = "{x} {y} {z}";
    public String copyPosMessage = "{x}, {y}, {z}";
}