package dev.boxadactle.coordinatesdisplay.init;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class Keybinds {
    public static KeyBinding visibleKeybind = new KeyBinding("key.coordinatesdisplay.visible", GLFW.GLFW_KEY_O, "category.coordinatesdisplay");
    public static KeyBinding coordinatesGUIKeybind = new KeyBinding("key.coordinatesdisplay.coordinatesgui", GLFW.GLFW_KEY_C, "category.coordinatesdisplay");

    public static KeyBinding openConfigFileKeybind = new KeyBinding("key.coordinatesdisplay.openfile", GLFW.GLFW_KEY_F6, "category.coordinatesdisplay");
    public static KeyBinding reloadConfigKeybind = new KeyBinding("key.coordinatesdisplay.configreload", GLFW.GLFW_KEY_F7, "category.coordinatesdisplay");

    public static KeyBinding copyLocation = new KeyBinding("key.coordinatesdisplay.copypos", GLFW.GLFW_KEY_B, "category.coordinatesdisplay");
    public static KeyBinding sendLocation = new KeyBinding("key.coordinatesdisplay.sendpos", GLFW.GLFW_KEY_X, "category.coordinatesdisplay");
    public static KeyBinding copyPosTp = new KeyBinding("key.coordinatesdisplay.copypostp", GLFW.GLFW_KEY_N, "category.coordinatesdisplay");
    public static KeyBinding changeHudPos = new KeyBinding("key.coordinatesdisplay.changeHudPos", GLFW.GLFW_KEY_F9, "category.coordinatesdisplay");

    public static void register() {
        KeyBindingHelper.registerKeyBinding(visibleKeybind);
        KeyBindingHelper.registerKeyBinding(coordinatesGUIKeybind);
        KeyBindingHelper.registerKeyBinding(reloadConfigKeybind);
        KeyBindingHelper.registerKeyBinding(openConfigFileKeybind);
        KeyBindingHelper.registerKeyBinding(copyLocation);
        KeyBindingHelper.registerKeyBinding(sendLocation);
        KeyBindingHelper.registerKeyBinding(copyPosTp);
        KeyBindingHelper.registerKeyBinding(changeHudPos);
    }

    public static void checkBindings() {

        if (visibleKeybind.wasPressed()) {
            CoordinatesDisplay.CONFIG.get().visible = !CoordinatesDisplay.CONFIG.get().visible;
            CoordinatesDisplay.CONFIG.save();
            CoordinatesDisplay.LOGGER.info("Updated visible property in config file");
        }

        if (coordinatesGUIKeybind.wasPressed()) {
            CoordinatesDisplay.shouldCoordinatesGuiOpen = true;
        }

        if (openConfigFileKeybind.wasPressed()) {
            if (ModUtil.openConfigFile()) {
                CoordinatesDisplay.LOGGER.info("Opened file in native explorer!");
            } else {
                CoordinatesDisplay.LOGGER.chatError("Sorry I could not open the file. It is saved at: " + FabricLoader.getInstance().getConfigDir().toFile().getAbsolutePath());
            }
        }

        if (reloadConfigKeybind.wasPressed()) {
            CoordinatesDisplay.CONFIG.resetConfig();
            CoordinatesDisplay.LOGGER.player.info("Config reloaded!");
            CoordinatesDisplay.LOGGER.info("Reloaded all config");
        }

        if (copyLocation.wasPressed()) {
            Position pos = Position.of(WorldUtils.getCamera());
            MinecraftClient.getInstance().keyboard.setClipboard(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().copyPosMessage, pos));
            CoordinatesDisplay.LOGGER.player.info("Copied to clipboard!");
            CoordinatesDisplay.LOGGER.info("Copied location to clipboard!");
        }

        if (sendLocation.wasPressed()) {
            Position pos = Position.of(WorldUtils.getCamera());
            CoordinatesDisplay.LOGGER.player.publicChat(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().posChatMessage, pos));
            CoordinatesDisplay.LOGGER.info("Sent position as chat message");
        }

        if (copyPosTp.wasPressed()) {
            Position pos = Position.of(WorldUtils.getCamera());

            MinecraftClient.getInstance().keyboard.setClipboard(ModUtil.toTeleportCommand(pos.position.getPlayerPos(), pos.world.getDimension(false)));

            CoordinatesDisplay.LOGGER.player.info("Copied position as command");
        }

        if (changeHudPos.wasPressed()) {

            CoordinatesDisplay.shouldHudPositionGuiOpen = true;

        }
    }
}
