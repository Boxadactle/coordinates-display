package dev.boxadactle.coordinatesdisplay.init;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class Keybinds {
    public static KeyMapping visibleKeybind = new KeyMapping("key.coordinatesdisplay.visible", GLFW.GLFW_KEY_O, "category.coordinatesdisplay");
    public static KeyMapping coordinatesGUIKeybind = new KeyMapping("key.coordinatesdisplay.coordinatesgui", GLFW.GLFW_KEY_C, "category.coordinatesdisplay");

    public static KeyMapping openConfigFileKeybind = new KeyMapping("key.coordinatesdisplay.openfile", GLFW.GLFW_KEY_F6, "category.coordinatesdisplay");
    public static KeyMapping reloadConfigKeybind = new KeyMapping("key.coordinatesdisplay.configreload", GLFW.GLFW_KEY_F7, "category.coordinatesdisplay");

    public static KeyMapping copyLocation = new KeyMapping("key.coordinatesdisplay.copypos", GLFW.GLFW_KEY_B, "category.coordinatesdisplay");
    public static KeyMapping sendLocation = new KeyMapping("key.coordinatesdisplay.sendpos", GLFW.GLFW_KEY_X, "category.coordinatesdisplay");
    public static KeyMapping copyPosTp = new KeyMapping("key.coordinatesdisplay.copypostp", GLFW.GLFW_KEY_N, "category.coordinatesdisplay");

    public static KeyMapping changeHudPosition = new KeyMapping("key.coordinatesdisplay.changeHudPos", GLFW.GLFW_KEY_F9, "category.coordinatesdisplay");


    public static void checkBindings(Position pos) {

        if (visibleKeybind.consumeClick()) {
            CoordinatesDisplay.CONFIG.get().visible = !CoordinatesDisplay.CONFIG.get().visible;
            CoordinatesDisplay.CONFIG.save();
            CoordinatesDisplay.LOGGER.info("Updated visible property in config file");
        }

        if (coordinatesGUIKeybind.consumeClick()) {
            CoordinatesDisplay.shouldCoordinatesGuiOpen = true;
        }

        if (openConfigFileKeybind.consumeClick()) {
            if (ModUtil.openConfigFile()) {
                CoordinatesDisplay.LOGGER.info("Opened file in native explorer!");
            } else {
                CoordinatesDisplay.LOGGER.player.warn("File could not be opened");
            }
        }

        if (reloadConfigKeybind.consumeClick()) {
            CoordinatesDisplay.CONFIG.reload();
            CoordinatesDisplay.LOGGER.player.info("Config reloaded!");
        }

        if (copyLocation.consumeClick()) {
            Minecraft.getInstance().keyboardHandler.setClipboard(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().copyPosMessage, pos));
            CoordinatesDisplay.LOGGER.player.info("Copied to clipboard!");
            CoordinatesDisplay.LOGGER.info("Copied location to clipboard");
        }

        if (sendLocation.consumeClick()) {
            CoordinatesDisplay.LOGGER.player.info(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().posChatMessage, pos));
            CoordinatesDisplay.LOGGER.info("Sent position as chat message");
        }

        if (copyPosTp.consumeClick()) {
            Minecraft.getInstance().keyboardHandler.setClipboard(ModUtil.toTeleportCommand(pos.position.getPlayerPos(), WorldUtils.getCurrentDimension()));

            CoordinatesDisplay.LOGGER.player.info("Copied position as teleport command!");
        }

        if (changeHudPosition.consumeClick()) {
            CoordinatesDisplay.shouldHudPositionGuiOpen = true;
        }
    }

}