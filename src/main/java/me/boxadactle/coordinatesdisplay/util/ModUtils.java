package me.boxadactle.coordinatesdisplay.util;

import io.github.cottonmc.cotton.config.ConfigManager;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.*;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;

import java.util.Locale;

public class ModUtils {

    public static final int TRANSPARENT_GRAY = 0x5c5c5c60;
    public static final int WHITE = 16777215;
    public static final int GRAY = 	11184810;
    public static final int DARK_GRAY = 5592405;
    public static String TRUE;
    public static String FALSE;

    public static void initText() {
        TRUE  = "§a" + Text.translatable("coordinatesdisplay.true").getString();
        FALSE = "§c" + Text.translatable("coordinatesdisplay.false").getString();
    }

    // list of colors
    public static final String[] colors = {
            "white", "gray", "dark_gray", "black",
            "dark_red", "red",
            "gold", "yellow",
            "dark_green", "green",
            "aqua", "dark_aqua",
            "blue", "dark_blue",
            "light_purple", "dark_purple"
    };

    public static int convertPosition(int position) {
        return position / MinecraftClient.getInstance().options.getGuiScale().getValue();
    }
    
    public static Text makeDeathPositionText(int x, int y, int z) {
        Text pos = Text.translatable("message.coordinatesdisplay.location2", x, y, z);

        Text position = Texts.bracketed(pos).styled((style -> style
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("message.coordinatesdisplay.teleport")))
            .withColor(getColorDecimal(CoordinatesDisplay.CONFIG.deathPosColor))
            .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/tp @s %d %d %d", x, y, z)))
        ));

        return Text.translatable("message.coordinatesdisplay.deathpos", position).styled(style -> style.withColor(getColorDecimal(CoordinatesDisplay.CONFIG.definitionColor)));
    }

    public static int getColorIndex(String color) {
        int index = -1;

        for (int i = 0; i < colors.length; i++)
            if (colors[i].equalsIgnoreCase(color)) index = i;

        return index;
    }

    public static String getColor(String color) {
        return Text.translatable("coordinatesdisplay.color." + color).getString();
    }

    public static String getColorPrefix(String color) {
        String prefix;
        String c = color.toLowerCase(Locale.ROOT);
        String defaultPrefix = "§f";
        switch (c) {
            case "dark_red" -> prefix = "§4";
            case "red"-> prefix = "§c";
            case "gold" -> prefix = "§6";
            case "yellow" -> prefix = "§e";
            case "dark_green" -> prefix = "§2";
            case "green" -> prefix = "§a";
            case "aqua" -> prefix = "§b";
            case "dark_aqua" -> prefix = "§3";
            case "dark_blue" -> prefix = "§1";
            case "blue" -> prefix = "§9";
            case "light_purple" -> prefix = "§d";
            case "dark_purple" -> prefix = "§5";
            case "white" -> prefix = "§f";
            case "gray" -> prefix = "§7";
            case "dark_gray" -> prefix = "§8";
            case "black" -> prefix = "§0";
            default -> {
                prefix = defaultPrefix;
                CoordinatesDisplay.LOGGER.warn("Could not parse color " + color + " so defaulted to " + defaultPrefix);
            }
        }
        CoordinatesDisplay.LOGGER.info("Parsed color " + color + " as " + prefix);
        return prefix;
    }

    public static int getColorDecimal(String color) {
        int decimal;
        String c = color.toLowerCase(Locale.ROOT);
        int defaultInt = 16777215;
        switch (c) {
            case "dark_red" -> decimal = 11141120;
            case "red" -> decimal = 16733525;
            case "gold" -> decimal = 16755200;
            case "yellow" -> decimal = 16777045;
            case "dark_green" -> decimal = 43520;
            case "green" -> decimal = 5635925;
            case "aqua" -> decimal = 5636095;
            case "dark_aqua" -> decimal = 43690;
            case "dark_blue" -> decimal = 170;
            case "blue" -> decimal = 5592575;
            case "light_purple" -> decimal = 16733695;
            case "dark_purple" -> decimal = 11141290;
            case "white" -> decimal = 16777215;
            case "gray" -> decimal = 11184810;
            case "dark_gray" -> decimal = 5592405;
            case "black" -> decimal = 0;
            default -> {
                decimal = defaultInt;
                CoordinatesDisplay.LOGGER.warn("Could not parse color " + color + " so defaulted to " + defaultInt);
            }
        }
        return decimal;
    }

    public static void resetConfig() {
        CoordinatesDisplay.CONFIG.displayPosOnDeathScreen = DefaultModConfig.displayPosOnDeathScreen;
        CoordinatesDisplay.CONFIG.showDeathPosInChat = DefaultModConfig.showDeathPosInChat;

        CoordinatesDisplay.CONFIG.visible = DefaultModConfig.visible;
        CoordinatesDisplay.CONFIG.roundPosToTwoDecimals = DefaultModConfig.roundPosToTwoDecimals;

        CoordinatesDisplay.CONFIG.renderBiome = DefaultModConfig.renderBiome;
        CoordinatesDisplay.CONFIG.renderDirection = DefaultModConfig.renderDirection;
        CoordinatesDisplay.CONFIG.renderChunkData = DefaultModConfig.renderChunkData;
        CoordinatesDisplay.CONFIG.renderBackground = DefaultModConfig.renderBackground;

        CoordinatesDisplay.CONFIG.definitionColor = DefaultModConfig.definitionColor;
        CoordinatesDisplay.CONFIG.dataColor = DefaultModConfig.dataColor;
        CoordinatesDisplay.CONFIG.deathPosColor = DefaultModConfig.deathPosColor;

        CoordinatesDisplay.CONFIG.padding = DefaultModConfig.padding;
        CoordinatesDisplay.CONFIG.textPadding = DefaultModConfig.textPadding;

        CoordinatesDisplay.CONFIG.hudX = DefaultModConfig.hudX;
        CoordinatesDisplay.CONFIG.hudY = DefaultModConfig.hudY;

        ConfigManager.saveConfig(CoordinatesDisplay.CONFIG);
        CoordinatesDisplay.parseColorPrefixes();
        CoordinatesDisplay.OVERLAY.updateConfig(CoordinatesDisplay.CONFIG);
    }

    // method to turn an angle into a direction string
    public static String getDirectionFromYaw(float degrees) {
        String direction;
        String[] directions = {"south", "southwest", "west", "northwest", "north", "northeast", "east", "southeast", "south"};
        if (degrees > 0)
            direction = directions[Math.round(degrees / 45)];
        else {
            int index = Math.round(degrees / 45) * -1;
            direction = directions[8 - index];
        }
        return direction;
    }

    public static int getLongestLength(Text ...text) {
        int largest = 0;
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        for (Text value : text) {
            int t = textRenderer.getWidth(value);
            if (t > largest) largest = t;
        }
        return largest;
    }

    public static String getBiomeId(String id) {
        StringBuilder name = new StringBuilder();

        String withoutNamespace = id.split(":")[1];
        String spaces = withoutNamespace.replaceAll("_", " ");
        for (String word : spaces.split("\\s")) {
            String firstLetter = word.substring(0, 1);
            String theRest = word.substring(1);
            name.append(firstLetter.toUpperCase()).append(theRest).append(" ");
        }

        return name.toString().trim();
    }

    // copy + pasted from DebugHud.class
    public static String getBiomeString(RegistryEntry<Biome> biome) {
        return (String)biome.getKeyOrValue().map((biomeKey) -> {
            return biomeKey.getValue().toString();
        }, (biome_) -> {
            return "[unregistered " + biome_ + "]";
        });
    }

}
