package me.boxadactle.coordinatesdisplay.util;

import com.mojang.datafixers.util.Pair;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;

public class ModUtils {

    public static final int TRANSPARENT_GRAY = 0x5c5c5c60;
    public static final int WHITE = 16777215;
    public static final int GRAY = 	11184810;
    public static final int DARK_GRAY = 5592405;
    public static Component TRUE;
    public static Component FALSE;

    public static final String CONFIG_WIKI = "https://boxadactle.github.io/wiki/coordinates-display/";
    public static final String CONFIG_WIKI_VISUAL = "https://boxadactle.github.io/wiki/coordinates-display/#visual";
    public static final String CONFIG_WIKI_RENDER = "https://boxadactle.github.io/wiki/coordinates-display/#rendering";
    public static final String CONFIG_WIKI_COLOR = "https://boxadactle.github.io/wiki/coordinates-display/#color";
    public static final String CONFIG_WIKI_DEATH = "https://boxadactle.github.io/wiki/coordinates-display/#deathpos";
    public static final String CONFIG_WIKI_TEXTS = "https://boxadactle.github.io/wiki/coordinates-display/#text";

    public static boolean isMousePressed;

    public static void initText() {
        TRUE = Component.translatable("coordinatesdisplay.true").withStyle(style -> style.withColor(0x55FF55));
        FALSE = Component.translatable("coordinatesdisplay.false").withStyle(style -> style.withColor(0xFF5555));
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

    public static float randomYaw() {
        return Mth.wrapDegrees((float) Math.random() * 360);
    }

    public static String getPlayerCurrentDimension() {

        return Minecraft.getInstance().level.dimension().location().toString();
    }

    public static String parseText(String text) {
        Minecraft c = Minecraft.getInstance();
        String newTextComponent = text;

        DecimalFormat decimalFormat = new DecimalFormat(CoordinatesDisplay.CONFIG.get().decimalRounding ? "0.00" : "0");

        String x = decimalFormat.format(c.player.getX());
        String y = decimalFormat.format(c.player.getY());
        String z = decimalFormat.format(c.player.getZ());

        String direction = getDirectionFromYaw(Mth.wrapDegrees(c.cameraEntity.getXRot()));

        Pair[] supported = new Pair[]{
                new Pair<>("dimension", parseIdentifier(getPlayerCurrentDimension())),
                new Pair<>("x", x),
                new Pair<>("y", y),
                new Pair<>("z", z),
                new Pair<>("direction", direction),
                new Pair<>("name", c.player.getDisplayName().getString())
        };
        for (Pair<?, ?> pair : supported) {
            newTextComponent = newTextComponent.replaceAll("\\{" + pair.getFirst() + "}", (String) pair.getSecond());
        }

        return newTextComponent;
    }

    public static Object selectRandom(Object ...args) {
        return args[(int) Math.round(Math.random() * (args.length - 1))];
    }

    public static String asTpCommand(double x, double y, double z, @Nullable String dimension) {
        DecimalFormat d = new DecimalFormat("0.00");
        String xs = d.format(x);
        String ys = d.format(y);
        String zs = d.format(z);
        if (dimension != null) {
            return String.format("/execute in %s run tp @s %s %s %s", dimension, xs, ys, zs);
        } else {
            return String.format("/tp @s %s %s %s", xs, ys, zs);
        }
    }

    public static Component makeDeathPositionTextComponent(int x, int y, int z) {

        String command = asTpCommand(x, y, z, getPlayerCurrentDimension());

        Component position = Component.translatable("message.coordinatesdisplay.deathlocation", x, y, z, getPlayerCurrentDimension()).withStyle((style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("message.coordinatesdisplay.teleport")))
                .withColor((CoordinatesDisplay.CONFIG.getConfig().deathPosColor))
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format(command, x, y, z)))
        ));

        return Component.translatable("message.coordinatesdisplay.deathpos", position).withStyle(style -> style.withColor((CoordinatesDisplay.CONFIG.getConfig().definitionColor)));
    }

    public static int getColorIndex(String color) {
        int index = -1;

        for (int i = 0; i < colors.length; i++)
            if (colors[i].equalsIgnoreCase(color)) index = i;

        return index;
    }

    public static String getColor(String color) {
        return Component.translatable("coordinatesdisplay.color." + color).getString();
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

<<<<<<< Updated upstream:forge/src/main/java/me/boxadactle/coordinatesdisplay/util/ModUtils.java
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

=======
    public static int calculateHudWidthMin(int p, int th, int dpadding, Component xtext, Component ytext, Component ztext, Component yaw, Component pitch, Component direction, Component biome) {
        int a = getLongestTextLength(xtext, ytext, ztext, biome);
        int b = Minecraft.getInstance().font.width("NW");

        return p + a + dpadding + b + p;
    }

    public static int calculateHudHeightMin(int p, int th) {
        // this might become a real method later
        return p + (th * 4) + p;
    }


>>>>>>> Stashed changes:forge/src/main/java/me/boxadactle/coordinatesdisplay/util/ModUtil.java
    public static boolean openConfigFile() {
        CoordinatesDisplay.LOGGER.info("Trying to open file in native file explorer...");
        File f = CoordinatesDisplay.configDir;
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
        CoordinatesDisplay.CONFIG.setConfig(new ModConfig());
        CoordinatesDisplay.CONFIG.save();
    }

    public static void reloadConfig() {
        CoordinatesDisplay.CONFIG.load();
        CoordinatesDisplay.LOGGER.info("Reloaded all config");
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

    public static int getLongestLength(Component ...text) {
        int largest = 0;
        for (Component value : text) {
            int t = Minecraft.getInstance().font.width(value.getString());
            if (t > largest) largest = t;
        }
        return largest;
    }

    public static @NotNull String parseIdentifier(@Nullable String id) {
        if (id != null) {
            StringBuilder name = new StringBuilder();

            String withoutNamespace = "plains";

            try {
                withoutNamespace = id.split(":")[1];
            } catch (IndexOutOfBoundsException e) {
                CoordinatesDisplay.LOGGER.error("Invalid biome");
                e.printStackTrace();
            }

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

    public static Component colorize(Component c, int color) {
        return c.copy().withStyle(style -> style.withColor(color));
    }

    // copy + pasted from DebugHud.class
    public static String printBiome(Holder<Biome> p_205375_) {
        return p_205375_.unwrap().map((p_205377_) -> {
            return p_205377_.location().toString();
        }, (p_205367_) -> {
            return "[unregistered " + p_205367_ + "]";
        });
    }

    public static boolean isMousePressed() {
        return isMousePressed;
    }

    public static int[] getDistance(int x, int y, int pointX, int pointY) {
        int distanceX = Math.abs(x - pointX);
        int distanceY = Math.abs(y - pointY);

        return new int[]{distanceX, distanceY};
    }

    public static boolean isMouseHovering(int mouseX, int mouseY, int boxX, int boxY, int boxWidth, int boxHeight) {
        return mouseX >= boxX && mouseX <= boxX + boxWidth &&
                mouseY >= boxY && mouseY <= boxY + boxHeight;
    }

    public static int clampToZero(int number) {
        return Math.max(number, 0);
    }

    public static int calculatePointDistance(int x, int y, int x1, int y1) {
        int deltaX = x1 - x;
        int deltaY = y1 - y;

        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        return (int) distance;
    }

    public static float calculateMouseScale(int x, int y, int w, int h, int mouseX, int mouseY) {
        int value1 = calculatePointDistance(x, y, x + w, y + h);
        int value2 = calculatePointDistance(x, y, mouseX, mouseY);
        float scaleFactor = (float) value2 / value1;

        scaleFactor = Math.max(0.5f, Math.min(2.0f, scaleFactor));

        scaleFactor = Math.round(scaleFactor * 100.0f) / 100.0f;

        return scaleFactor;
    }


}