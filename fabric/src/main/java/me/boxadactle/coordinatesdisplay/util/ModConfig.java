package me.boxadactle.coordinatesdisplay.util;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "coordinates-display")
public class ModConfig implements ConfigData {

    public boolean visible = DefaultModConfig.visible;
    public boolean roundPosToTwoDecimals = DefaultModConfig.roundPosToTwoDecimals;
    public int hudX = DefaultModConfig.hudX;
    public int hudY = DefaultModConfig.hudY;

<<<<<<< Updated upstream
    public boolean renderBackground = DefaultModConfig.renderBackground;
    public boolean renderChunkData = DefaultModConfig.renderChunkData;
    public boolean renderDirection = DefaultModConfig.renderDirection;
    public boolean renderBiome = DefaultModConfig.renderBiome;
=======
    public boolean minMode = false;
    public int hudX = 0;
    public int hudY = 0;
    public float hudScale = 1.0f;
>>>>>>> Stashed changes

    public int definitionColor = DefaultModConfig.definitionColor;
    public int dataColor = DefaultModConfig.dataColor;
    public int deathPosColor = DefaultModConfig.deathPosColor;

    public boolean displayPosOnDeathScreen = DefaultModConfig.displayPosOnDeathScreen;
    public boolean showDeathPosInChat = DefaultModConfig.displayPosOnDeathScreen;

    public int padding = DefaultModConfig.padding;
    public int textPadding = DefaultModConfig.textPadding;

    public String posChatMessage = DefaultModConfig.posChatMessage;
    public String copyPosMessage = DefaultModConfig.copyPosMessage;
}
