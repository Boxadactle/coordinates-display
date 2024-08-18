package dev.boxadactle.coordinatesdisplay;

import com.mojang.datafixers.util.Pair;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.position.Position;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.SystemUtils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class ModUtil {


    public static String parseText(String text, Position pos) {
        Minecraft c = ClientUtils.getClient();
        String newTextComponent = text;

        DecimalFormat decimalFormat = new DecimalFormat(CoordinatesDisplay.CONFIG.get().includeDecimalsWhenCopying ? "0.00" : "0");

        Vec3<Double> player = pos.position.getPlayerPos();

        String x = decimalFormat.format(player.getX());
        String y = decimalFormat.format(player.getY());
        String z = decimalFormat.format(player.getZ());

        String direction = getDirectionFromYaw(Mth.wrapDegrees(c.cameraEntity.getXRot()));

        Pair<String, ?>[] supported = new Pair[]{
                new Pair<>("dimension", pos.world.getDimension(true)),
                new Pair<>("x", x),
                new Pair<>("y", y),
                new Pair<>("z", z),
                new Pair<>("direction", direction),
                new Pair<>("name", Objects.requireNonNull(c.player.getDisplayName()).getString())
        };
        for (Pair<?, ?> pair : supported) {
            newTextComponent = newTextComponent.replaceAll("\\{" + pair.getFirst() + "}", (String) pair.getSecond());
        }

        return newTextComponent;
    }

    public static String toExecuteCommand(Position pos) {
        int x = (int)Math.round(pos.position.getPlayerPos().getX());
        int y = (int)Math.round(pos.position.getPlayerPos().getY());
        int z = (int)Math.round(pos.position.getPlayerPos().getZ());

        String dimension = pos.world.getDimension(false);
        return String.format("/execute in %s run tp @s %d %d %d", dimension, x, y, z);
    }

    public static String toTeleportCommand(Position pos) {
        int x = (int)Math.round(pos.position.getPlayerPos().getX());
        int y = (int)Math.round(pos.position.getPlayerPos().getY());
        int z = (int)Math.round(pos.position.getPlayerPos().getZ());

        return String.format("/tp @s %d %d %d", x, y, z);
    }

    public static String toBaritoneCommand(Position pos) {
        int x = (int)Math.round(pos.position.getPlayerPos().getX());
        int y = (int)Math.round(pos.position.getPlayerPos().getY());
        int z = (int)Math.round(pos.position.getPlayerPos().getZ());

        return String.format("#goto %s %s %s", x, y, z);
    }

    public static Component makeDeathPositionComponent(Position pos) {

        Vec3<Double> player = pos.position.getPlayerPos();

        String command = CoordinatesDisplay.getConfig().teleportMode.toCommand(pos);

        int x = (int)Math.round(player.getX());
        int y = (int)Math.round(player.getY());
        int z = (int)Math.round(player.getZ());

        Component position = new TranslatableComponent("message.coordinatesdisplay.deathlocation", x, y, z, pos.world.getDimension(false)).withStyle((style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("message.coordinatesdisplay.teleport")))
                .withColor(TextColor.fromRgb(CoordinatesDisplay.CONFIG.get().deathPosColor))
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format(command, x, y, z)))
        ));

        return GuiUtils.colorize(new TranslatableComponent("message.coordinatesdisplay.deathpos", position), CoordinatesDisplay.CONFIG.get().definitionColor);
    }

    @ExpectPlatform
    public static String getBlockName(Block block) {
        throw new AssertionError("Expected platform-specific block name function.");
    }


    public static boolean openConfigFile() {
        CoordinatesDisplay.LOGGER.info("Trying to open file in native file explorer...");
        File f = new File(ClientUtils.getClient().gameDirectory.getAbsolutePath() + "\\config");;
        boolean worked;
        if (SystemUtils.OS_NAME.toLowerCase().contains("windows")) {
            try {
                Runtime.getRuntime().exec(new String[]{"explorer.exe", f.getAbsolutePath()});
                worked = true;
            } catch (IOException e) {
                CoordinatesDisplay.LOGGER.error("Got an error: ");
                CoordinatesDisplay.LOGGER.printStackTrace(e);
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

    public static Component getBiomeComponent(Holder<Biome> biome, boolean colored, int defaultColor) {
        if (biome == null) {
            return GuiUtils.colorize(new TranslatableComponent("hud.coordinatesdisplay.biome.unknown"), defaultColor);
        }

        Registry<Biome> registry = WorldUtils.getWorld() != null ? WorldUtils.getWorld().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY) : BuiltinRegistries.BIOME;

        Optional<ResourceKey<Biome>> resource = registry.getResourceKey(biome.value());

        if (resource.isEmpty()) {
            throw new RuntimeException("Biome key is empty for biome: " + biome);
        }

        ResourceLocation key = resource.get().location();

        return GuiUtils.colorize(
                new TranslatableComponent("biome." + key.getNamespace() + "." + key.getPath()),
                colored ?
                        CoordinatesDisplay.WorldColors.getBiomeColor(biome):
                        defaultColor
        );
    }

    public static boolean isMouseHovering(int mouseX, int mouseY, int boxX, int boxY, int boxWidth, int boxHeight) {
        return mouseX >= boxX && mouseX <= boxX + boxWidth &&
                mouseY >= boxY && mouseY <= boxY + boxHeight;
    }

    public static String getNamespace(String id) {
        return id.split(":")[0];
    }

    public static int calculatePointDistance(int x, int y, int x1, int y1) {
        int deltaX = x1 - x;
        int deltaY = y1 - y;

        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        return (int) Math.abs(distance);
    }

    public static float calculateMouseScale(int x, int y, int w, int h, int mouseX, int mouseY) {
        int value1 = calculatePointDistance(x, y, x + w, y + h);
        int value2 = calculatePointDistance(x, y, mouseX, mouseY);
        float scaleFactor = (float) value2 / value1;

        scaleFactor = Math.max(0.5f, Math.min(2.0f, scaleFactor));

        scaleFactor = Math.round(scaleFactor * 100.0f) / 100.0f;

        return scaleFactor;
    }

    public static <T> boolean or(T val, T ...compare) {
        boolean toReturn = false;

        for (T t : compare) {
            if (val.equals(t)) {
                toReturn = true;
                break;
            }
        }

        return toReturn;
    }

    public static <T> boolean not(T val, T ...compare) {
        boolean toReturn = true;

        for (T t : compare) {
            if (val.equals(t)) {
                toReturn = false;
                break;
            }
        }

        return toReturn;
    }

    public static Vec3i doubleVecToIntVec(net.minecraft.world.phys.Vec3 vec) {
        return new Vec3i((int)Math.round(vec.x), (int)Math.round(vec.y), (int)Math.round(vec.z));
    }

    public static Vec3<Double> fromMinecraftVector(net.minecraft.world.phys.Vec3 vec3d) {
        return new Vec3<>(vec3d.x, vec3d.y, vec3d.z);
    }

    public static BlockPos toBlockPos(Vec3<Integer> vec) {
        return new BlockPos(vec.getX(), vec.getY(), vec.getZ());
    }

}