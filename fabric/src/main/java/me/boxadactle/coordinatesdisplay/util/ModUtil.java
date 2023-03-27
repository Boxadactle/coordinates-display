package me.boxadactle.coordinatesdisplay.util;

import com.mojang.datafixers.util.Pair;
import io.github.cottonmc.cotton.config.ConfigManager;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.SystemUtils;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class ModUtil {

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
        TRUE  = "§a" + Text.translatable("coordinatesdisplay.true").getString();
        FALSE = "§c" + Text.translatable("coordinatesdisplay.false").getString();
    }

    public static Text colorize(Text text, int color) {
        return text.copy().styled(style -> style.withColor(color));
    }

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

    public static int calculateHudWidth(int p, int tp, Text xtext, Text ytext, Text ztext, Text chunkx, Text chunkz, Text direction, Text biome, Text version) {
        int a = getLongestTextLength(xtext, ytext, ztext);
        int b = getLongestTextLength(chunkx, chunkz);
        int c = a + (CoordinatesDisplay.CONFIG.renderChunkData ? b + tp : 0);

        int d = 0;
        if (CoordinatesDisplay.CONFIG.renderDirection) {
            if (getLongestTextLength(direction) > d) d = getLongestTextLength(direction);
        }
        if (CoordinatesDisplay.CONFIG.renderBiome) {
            if (getLongestTextLength(biome) > d) d = getLongestTextLength(biome);
        }
        if (CoordinatesDisplay.CONFIG.renderMCVersion) {
            if (getLongestTextLength(version) > d) d = getLongestTextLength(version);
        }

        return p + Math.max(c, d) + p;
    }

    public static int calculateHudHeight(int th, int p, int tp, Text xtext, Text ytext, Text ztext, Text chunkx, Text chunkz, Text direction, Text biome, Text version) {
        int a = th * 3;

        int b = 0;
        if (CoordinatesDisplay.CONFIG.renderDirection) {
            b += th;
        }
        if (CoordinatesDisplay.CONFIG.renderBiome) {
            b += th;
        }
        if (CoordinatesDisplay.CONFIG.renderMCVersion) {
            b += th;
        }

        boolean c = (CoordinatesDisplay.CONFIG.renderDirection || CoordinatesDisplay.CONFIG.renderBiome || CoordinatesDisplay.CONFIG.renderMCVersion);

        return p + a + (c ? tp : 0) + b + p;
    }

    public static int calculateHudWidthMin(int p, int th, int dpadding, Text xtext, Text ytext, Text ztext, Text yaw, Text pitch, Text direction, Text biome) {
        int a = getLongestTextLength(xtext, ytext, ztext, biome);
        int b = MinecraftClient.getInstance().textRenderer.getWidth("NW");

        return p + a + dpadding + b + p;
    }

    public static int calculateHudHeightMin(int p, int th) {
        // this might become a real method later
        return p + (th * 4) + p;
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

        Text pos = Text.translatable("message.coordinatesdisplay.deathlocation", x, y, z, getPlayerCurrentDimension() != null ? getPlayerCurrentDimension() : "unregistered dimension");

        Text position = Texts.bracketed(pos).styled((style -> style
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("message.coordinatesdisplay.teleport")))
            .withColor(CoordinatesDisplay.CONFIG.deathPosColor)
            .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format(command, x, y, z)))
        ));

        return Text.translatable("message.coordinatesdisplay.deathpos", position).styled(style -> style.withColor(CoordinatesDisplay.CONFIG.definitionColor));
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
        ModConfig c = CoordinatesDisplay.CONFIG;

        c.visible = true;
        c.roundPosToTwoDecimals = true;

        c.minMode = false;
        c.hudX = 0;
        c.hudY = 0;

        c.renderBackground = true;
        c.renderChunkData = true;
        c.renderDirection = true;
        c.renderBiome = true;
        c.renderDirectionInt = true;
        c.renderMCVersion = true;

        c.definitionColor = 0x55FF55;
        c.dataColor = 0xFFFFFF;
        c.deathPosColor = 0x55FFFF;
        c.backgroundColor = 0x405c5c5c;

        c.displayPosOnDeathScreen = true;
        c.showDeathPosInChat = true;

        c.padding = 5;
        c.textPadding = 15;

        c.posChatMessage = "{x} {y} {z}";
        c.copyPosMessage = "{x}, {y}, {z}";

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

    public static int getLongestTextLength(Text ...text) {
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

            String stripNamespace = id.substring(id.indexOf(":") + 1);
            String spaces = stripNamespace.replaceAll("_", " ");
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