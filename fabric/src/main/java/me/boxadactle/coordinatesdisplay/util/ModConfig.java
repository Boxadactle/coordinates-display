package me.boxadactle.coordinatesdisplay.util;

import io.github.cottonmc.cotton.config.annotations.ConfigFile;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;

@ConfigFile(name = "./" + CoordinatesDisplay.MOD_ID, extension = ".json")
public class ModConfig {

    public boolean visible = DefaultModConfig.visible;
    public boolean roundPosToTwoDecimals = DefaultModConfig.roundPosToTwoDecimals;
    public int hudX = DefaultModConfig.hudX;
    public int hudY = DefaultModConfig.hudY;

    public boolean renderBackground = DefaultModConfig.renderBackground;
    public boolean renderChunkData = DefaultModConfig.renderChunkData;
    public boolean renderDirection = DefaultModConfig.renderDirection;
    public boolean renderBiome = DefaultModConfig.renderBiome;

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
