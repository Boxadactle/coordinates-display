package me.boxadactle.coordinatesdisplay.util;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "./" + CoordinatesDisplay.MOD_ID)
public class ModConfig implements ConfigData {

    public boolean visible = true;
    public boolean decimalRounding = true;
    public boolean minMode = false;
    public int hudX = 0;
    public int hudY = 0;
    public float hudScale = 1.0f;
    public boolean hudTextShadow = true;


    public boolean renderBackground = true;
    public boolean renderChunkData = true;
    public boolean renderDirection = true;
    public boolean renderDirectionInt = true;
    public boolean renderBiome = true;
    public boolean renderMCVersion = true;

    public int definitionColor = 0x55FF55;
    public int dataColor = 0xFFFFFF;
    public int deathPosColor = 0x55FFFF;
    public int backgroundColor = 0x405c5c5c;

    public boolean displayPosOnDeathScreen = true;
    public boolean showDeathPosInChat = true;

    public int padding = 5;
    public int textPadding = 10;

    public String posChatMessage = "{x} {y} {z}";
    public String copyPosMessage = "{x}, {y}, {z}";
}