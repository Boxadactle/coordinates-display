package me.boxadactle.coordinatesdisplay.util;

import com.mojang.datafixers.util.Pair;
import io.github.cottonmc.cotton.config.ConfigManager;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.SystemUtils;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;

public class ModUtils {

    public static final int TRANSPARENT_GRAY = 0x5c5c5c60;
    public static final int WHITE = 16777215;
    public static final int GRAY = 	11184810;
    public static final int DARK_GRAY = 5592405;
    public static String TRUE;
    public static String FALSE;

    public static final String CONFIG_WIKI = "https://github.com/Boxadactle/coordinates-display/wiki/Configuration";
    public static final String CONFIG_WIKI_VISUAL = "https://github.com/Boxadactle/coordinates-display/wiki/Configuration#visual-settings";
    public static final String CONFIG_WIKI_RENDER = "https://github.com/Boxadactle/coordinates-display/wiki/Configuration#render-settings";
    public static final String CONFIG_WIKI_COLOR = "https://github.com/Boxadactle/coordinates-display/wiki/Configuration#color-configuration";
    public static final String CONFIG_WIKI_DEATH = "https://github.com/Boxadactle/coordinates-display/wiki/Configuration#death-position-configuration";
    public static final String CONFIG_WIKI_TEXTS = "https://github.com/Boxadactle/coordinates-display/wiki/Configuration#texts-configuration";

    public static void initText() {
        TRUE  = "§a" + new TranslatableText("coordinatesdisplay.true").getString();
        FALSE = "§c" + new TranslatableText("coordinatesdisplay.false").getString();
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

    public static int aspectRatio(int int1, int int2, int int3) {
        int i1 = int3 / int1;
        return int2 * i1;
    }

    public static String getPlayerCurrentDimension() {
        RegistryKey<World> registry = MinecraftClient.getInstance().player.world.getRegistryKey();

        return (registry != null ? registry.getValue().toString() : null);
    }

    public static String parseText(String text) {
        MinecraftClient c = MinecraftClient.getInstance();
        String newtext = text;

        DecimalFormat decimalFormat = new DecimalFormat(CoordinatesDisplay.CONFIG.roundPosToTwoDecimals ? "0.00" : "0");

        String x = decimalFormat.format(c.player.getX());
        String y = decimalFormat.format(c.player.getY());
        String z = decimalFormat.format(c.player.getZ());

        String direction = getDirectionFromYaw(MathHelper.wrapDegrees(c.cameraEntity.getYaw()));

        Pair[] supported = new Pair[]{
                new Pair<>("dimension", parseIdentifier(getPlayerCurrentDimension())),
                new Pair<>("x", x),
                new Pair<>("y", y),
                new Pair<>("z", z),
                new Pair<>("direction", direction),
                new Pair<>("name", c.player.getDisplayName().getString())
        };
        for (Pair<?, ?> pair : supported) {
            newtext = newtext.replaceAll("\\{" + pair.getFirst() + "}", (String) pair.getSecond());
        }

        return newtext;
    }

    public static Object selectRandom(Object ...args) {
        return args[(int) Math.round(Math.random() * (args.length - 1))];
    }

    public static String asTpCommand(int x, int y, int z, @Nullable String dimension) {
        if (dimension != null) {
            return String.format("/execute in %s run tp @s %d %d %d", dimension, x, y, z);
        } else {
            return String.format("/tp @s %d %d %d", x, y, z);
        }
    }
    
    public static Text makeDeathPositionText(int x, int y, int z) {
        MinecraftClient client = MinecraftClient.getInstance();

        String command = asTpCommand(x, y, z, getPlayerCurrentDimension());

        Text pos = new TranslatableText("message.coordinatesdisplay.deathlocation", x, y, z, getPlayerCurrentDimension() != null ? getPlayerCurrentDimension() : "unregistered dimension");

        Text position = Texts.bracketed(pos).styled((style -> style
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("message.coordinatesdisplay.teleport")))
            .withColor(CoordinatesDisplay.CONFIG.deathPosColor)
            .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format(command, x, y, z)))
        ));

        return new TranslatableText("message.coordinatesdisplay.deathpos", position).styled(style -> style.withColor(CoordinatesDisplay.CONFIG.definitionColor));
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

    public static boolean openConfigFile() {
        CoordinatesDisplay.LOGGER.info("Trying to open file in native file explorer...");
        File f = FabricLoader.getInstance().getConfigDir().toFile();
        boolean worked;
        if (SystemUtils.OS_NAME.toLowerCase().contains("windows")) {
            try {
                Runtime.getRuntime().exec(new String[]{"explorer.exe", f.getAbsolutePath()});
                worked = true;
            } catch (IOException e) {
                CoordinatesDisplay.LOGGER.error("Got an error: ");
                e.printStackTrace();
                worked = false;
            }
        } else {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browseFileDirectory(f);
                CoordinatesDisplay.LOGGER.info("Opened directory");
                worked = true;
            } else {
                CoordinatesDisplay.LOGGER.warn("Incompatible with desktop class");
                worked = false;
            }
        }

        return worked;
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

        CoordinatesDisplay.CONFIG.posChatMessage = DefaultModConfig.posChatMessage;
        CoordinatesDisplay.CONFIG.copyPosMessage = DefaultModConfig.copyPosMessage;

        ConfigManager.saveConfig(CoordinatesDisplay.CONFIG);
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

    public static String parseIdentifier(@Nullable String id) {
        if (id != null) {
            StringBuilder name = new StringBuilder();

            String withoutNamespace = id.split(":")[1];
            String spaces = withoutNamespace.replaceAll("_", " ");
            for (String word : spaces.split("\\s")) {
                String firstLetter = word.substring(0, 1);
                String theRest = word.substring(1);
                name.append(firstLetter.toUpperCase()).append(theRest).append(" ");
            }

            return name.toString().trim();
        } else {
            return "Plains";
        }
    }

    // copy + pasted from DebugHud.class
    public static String getBiomeString(RegistryEntry<Biome> biome) {
        return biome.getKeyOrValue().map((biomeKey) -> biomeKey.getValue().toString(), (biome_) -> "[unregistered " + biome_ + "]");
    }

}
