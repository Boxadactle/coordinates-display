package me.boxadactle.coordinatesdisplay;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

@ConfigFile(name = "./" + CoordinatesDisplay.MOD_NAME + "/config")
public class Config {

    @Comment("should anything be rendered")
    public boolean visible = ConfigDefault.visible;

    @Comment("should numbers be rounded with two decimals")
    public boolean roundPosToTwoDecimals = ConfigDefault.roundPosToTwoDecimals;

    @Comment("should the gray background be rendered")
    public boolean renderBackground = ConfigDefault.renderBackground;

    @Comment("should the chunk data be rendered")
    public boolean renderChunkData = ConfigDefault.renderChunkData;

    @Comment("should the cardinal direction be rendered")
    public boolean renderDirection = ConfigDefault.renderDirection;

    @Comment("should the biome you're in be rendered")
    public boolean renderBiome = ConfigDefault.renderBiome;

    @Comment("the color of the definitions on the hud (see https://www.digminecraft.com/lists/color_list_pc.php)")
    public String definitionColor = ConfigDefault.definitionColor;

    @Comment("the color of the data on the hud (see https://www.digminecraft.com/lists/color_list_pc.php)")
    public String dataColor = ConfigDefault.dataColor;

    @Comment("should death location be displayed on the death screen")
    public boolean displayPosOnDeathScreen = true;

    @Comment("should death location be sent as chat message")
    public boolean showDeathPosInChat = true;

    @Comment("Color of the death location chat message")
    public String deathPosColor = "aqua";

    @Comment("how much padding on the box")
    public int padding = ConfigDefault.padding;

    @Comment("how much padding should be on the text")
    public int textPadding = ConfigDefault.textPadding;
}
