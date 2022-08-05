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

    public String definitionColor = DefaultModConfig.definitionColor;
    public String dataColor = DefaultModConfig.dataColor;
    public String deathPosColor = DefaultModConfig.deathPosColor;

    public boolean displayPosOnDeathScreen = DefaultModConfig.displayPosOnDeathScreen;
    public boolean showDeathPosInChat = DefaultModConfig.displayPosOnDeathScreen;

    public int padding = DefaultModConfig.padding;
    public int textPadding = DefaultModConfig.textPadding;
}
