package dev.boxadactle.coordinatesdisplay.util;

import com.mojang.datafixers.util.Pair;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import dev.boxadactle.boxlib.math.Vec3;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.SystemUtils;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class ModUtil {

    public static final String CONFIG_WIKI = "https://boxadactle.github.io/wiki/coordinates-display/";
    public static final String CONFIG_WIKI_VISUAL = "https://boxadactle.github.io/wiki/coordinates-display/#visual";
    public static final String CONFIG_WIKI_RENDER = "https://boxadactle.github.io/wiki/coordinates-display/#rendering";
    public static final String CONFIG_WIKI_COLOR = "https://boxadactle.github.io/wiki/coordinates-display/#color";
    public static final String CONFIG_WIKI_DEATH = "https://boxadactle.github.io/wiki/coordinates-display/#deathpos";
    public static final String CONFIG_WIKI_TEXTS = "https://boxadactle.github.io/wiki/coordinates-display/#text";

    public static final int CONFIG_CONTENT_COLOR = 0x40363636;

    public static int aspectRatio(int int1, int int2, int int3) {
        int i1 = int3 / int1;
        return int2 * i1;
    }

    @Deprecated
    public static String getPlayerCurrentDimension() {
        RegistryKey<World> registry = MinecraftClient.getInstance().player.clientWorld.getRegistryKey();

        return (registry != null ? registry.getValue().toString() : null);
    }

    public static String parseText(String text, Position pos) {
        MinecraftClient c = MinecraftClient.getInstance();
        String newtext = text;

        String direction = getDirectionFromYaw(pos.getYaw(true));

        DecimalFormat d = new DecimalFormat(CoordinatesDisplay.CONFIG.get().shouldRoundWhenCopying ? "0" : "0.00");

        Pair[] supported = new Pair[]{
                new Pair<>("dimension", ClientUtils.parseIdentifier(WorldUtils.getCurrentDimension())),
                new Pair<>("x", d.format(pos.getPlayerVector().getX())),
                new Pair<>("y", d.format(pos.getPlayerVector().getY())),
                new Pair<>("z", d.format(pos.getPlayerVector().getZ())),
                new Pair<>("direction", direction),
                new Pair<>("name", c.player.getGameProfile().getName())
        };
        for (Pair<?, ?> pair : supported) {
            newtext = newtext.replaceAll("\\{" + pair.getFirst() + "}", (String) pair.getSecond());
        }

        return newtext;
    }

    public static String toTeleportCommand(Vec3<Double> pos, @Nullable String dimension) {
        int x = (int)Math.round(pos.getX());
        int y = (int)Math.round(pos.getY());
        int z = (int)Math.round(pos.getZ());

        ModConfig.TeleportMode tpmode = CoordinatesDisplay.getConfig().teleportMode;

        if (dimension != null && tpmode.equals(ModConfig.TeleportMode.EXECUTE)) {
            return String.format("/execute in %s run tp @s %d %d %d", dimension, x, y, z);
        } else if (tpmode.equals(ModConfig.TeleportMode.TP)) {
            return String.format("/tp @s %d %d %d", x, y, z);
        } else {
            return String.format("#goto %s %s %s", x, y, z);
        }
    }
    
    public static Text makeDeathPositionText(Position pos) {
        String command = toTeleportCommand(pos.getPlayerVector(), WorldUtils.getCurrentDimension());

        int x = (int)Math.round(pos.getPlayerVector().getX());
        int y = (int)Math.round(pos.getPlayerVector().getY());
        int z = (int)Math.round(pos.getPlayerVector().getZ());

        Text posText = Text.translatable("message.coordinatesdisplay.deathlocation", x, y, z, WorldUtils.getCurrentDimension() != null ? WorldUtils.getCurrentDimension() : "unregistered dimension");

        Text position = GuiUtils.brackets(posText).copy().styled((style -> style
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("message.coordinatesdisplay.teleport")))
            .withColor(CoordinatesDisplay.getConfig().deathPosColor)
            .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format(command, x, y, z)))
        ));

        return Text.translatable("message.coordinatesdisplay.deathpos", position).styled(style -> style.withColor(CoordinatesDisplay.CONFIG.get().definitionColor));
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

    // method to turn an angle into a direction string
    public static String getDirectionFromYaw(double degrees) {
        String direction;
        String[] directions = {"south", "southwest", "west", "northwest", "north", "northeast", "east", "southeast", "south"};
        if (degrees > 0)
            direction = directions[(int)Math.round(degrees / 45)];
        else {
            int index = (int)Math.round(degrees / 45) * -1;
            direction = directions[8 - index];
        }
        return direction;
    }

    // copy + pasted from DebugHud.class
    public static String getBiomeString(RegistryEntry<Biome> biome) {
        return biome != null ?
                biome.getKeyOrValue().map((biomeKey) -> biomeKey.getValue().toString(), (biome_) -> "[unregistered " + biome_ + "]") :
                "minecraft:plains";
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

    public static Vec3i doubleVecToIntVec(Vec3d vec) {
        return new Vec3i((int)Math.round(vec.x), (int)Math.round(vec.y), (int)Math.round(vec.z));
    }

    public static Vec3<Integer> doubleVecToIntVec(Vec3<Double> vec) {
        return new Vec3<>((int)Math.round(vec.getX()), (int)Math.round(vec.getY()), (int)Math.round(vec.getZ()));
    }

    public static Vec3i toMinecraftVector(Vec3<Integer> vec) {
        return new Vec3i(vec.getX(), vec.getY(), vec.getZ());
    }

}
