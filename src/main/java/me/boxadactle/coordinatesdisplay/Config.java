package me.boxadactle.coordinatesdisplay;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

@ConfigFile(name = "./" + CoordinatesDisplay.MOD_NAME + "/config")
public class Config {

    @Comment("should anything be rendered")
    public boolean visible = true;

    @Comment("should the gray background be rendered")
    public boolean renderBackground = true;

    @Comment("should the chunk data be rendered")
    public boolean renderChunkData = true;

    @Comment("should the cardinal direction be rendered")
    public boolean renderDirection = true;

    @Comment("should the biome you're in be rendered")
    public boolean renderBiome = true;

    @Comment("the color of the definitions on the hud")
    public String definitionColor = "green";

    @Comment("the color of the data on the hud")
    public String dataColor = "white";

    @Comment("should numbers be rounded with two decimals")
    public boolean roundPosToTwoDecimals = true;

    @Comment("how much padding on the box")
    public int padding = 5;

    @Comment("how much padding should be on the text")
    public int textPadding = 10;
}
