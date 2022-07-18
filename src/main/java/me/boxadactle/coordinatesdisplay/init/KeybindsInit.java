package me.boxadactle.coordinatesdisplay.init;

import io.github.cottonmc.cotton.config.ConfigManager;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Environment(EnvType.CLIENT)
public class KeybindsInit {
    static KeyBinding visibleKeybind = new KeyBinding("key.coordinatesmod.visible", GLFW.GLFW_KEY_O, "category.coordinatesmod");
    static KeyBinding backgroundKeybind = new KeyBinding("key.coordinatesmod.background", GLFW.GLFW_KEY_B, "category.coordinatesmod");
    static KeyBinding chunkdataKeybind = new KeyBinding("key.coordinatesmod.chunkdata", GLFW.GLFW_KEY_C, "category.coordinatesmod");
    static KeyBinding directionKeybind = new KeyBinding("key.coordinatesmod.direction", GLFW.GLFW_KEY_Y, "category.coordinatesmod");
    static KeyBinding biomeKeybind = new KeyBinding("key.coordinatesmod.biome", GLFW.GLFW_KEY_M, "category.coordinatesmod");
    static KeyBinding decimalKeybind = new KeyBinding("key.coordinatesmod.decimals", GLFW.GLFW_KEY_PERIOD, "category.coordinatesmod");

    static KeyBinding openConfigFileKeybind = new KeyBinding("key.coordinatesmod.openfile", GLFW.GLFW_KEY_F6, "category.coordinatesmod");
    static KeyBinding reloadConfigKeybind = new KeyBinding("key.coordinatesmod.configreload", GLFW.GLFW_KEY_F7, "category.coordinatesmod");

    public static void register() {
        KeyBindingHelper.registerKeyBinding(visibleKeybind);
        KeyBindingHelper.registerKeyBinding(backgroundKeybind);
        KeyBindingHelper.registerKeyBinding(chunkdataKeybind);
        KeyBindingHelper.registerKeyBinding(directionKeybind);
        KeyBindingHelper.registerKeyBinding(biomeKeybind);
        KeyBindingHelper.registerKeyBinding(decimalKeybind);
        KeyBindingHelper.registerKeyBinding(reloadConfigKeybind);
        KeyBindingHelper.registerKeyBinding(openConfigFileKeybind);
    }

    public static void checkBindings() {

        if (visibleKeybind.wasPressed()) {
            CoordinatesDisplay.CONFIG.visible = !CoordinatesDisplay.CONFIG.visible;
            ConfigManager.saveConfig(CoordinatesDisplay.CONFIG);
            CoordinatesDisplay.LOGGER.info("Updated visible property in config file");
        }
        if (backgroundKeybind.wasPressed()) {
            CoordinatesDisplay.CONFIG.renderBackground = !CoordinatesDisplay.CONFIG.renderBackground;
            ConfigManager.saveConfig(CoordinatesDisplay.CONFIG);
            CoordinatesDisplay.LOGGER.info("Updated renderBackground property in config file");
        }
        if (chunkdataKeybind.wasPressed()) {
            CoordinatesDisplay.CONFIG.renderChunkData = !CoordinatesDisplay.CONFIG.renderChunkData;
            ConfigManager.saveConfig(CoordinatesDisplay.CONFIG);
            CoordinatesDisplay.LOGGER.info("Updated renderChunkData property in config file");
        }
        if (directionKeybind.wasPressed()) {
            CoordinatesDisplay.CONFIG.renderDirection = !CoordinatesDisplay.CONFIG.renderDirection;
            ConfigManager.saveConfig(CoordinatesDisplay.CONFIG);
            CoordinatesDisplay.LOGGER.info("Updated renderDirection property in config file");
        }
        if (biomeKeybind.wasPressed()) {
            CoordinatesDisplay.CONFIG.renderBiome = !CoordinatesDisplay.CONFIG.renderBiome;
            ConfigManager.saveConfig(CoordinatesDisplay.CONFIG);
            CoordinatesDisplay.LOGGER.info("Updated renderBiome property in config file");
        }
        if (decimalKeybind.wasPressed()) {
            CoordinatesDisplay.CONFIG.roundPosToTwoDecimals = !CoordinatesDisplay.CONFIG.roundPosToTwoDecimals;
            ConfigManager.saveConfig(CoordinatesDisplay.CONFIG);
            CoordinatesDisplay.LOGGER.info("Updated roundPosToTwoDecimals property in config file");
        }

        if (openConfigFileKeybind.wasPressed()) {
            CoordinatesDisplay.LOGGER.info("Trying to open file in native file explorer...");
            File f = CoordinatesDisplay.configDir;
            if (SystemUtils.OS_NAME.toLowerCase().contains("windows")) {
                try {
                    Runtime.getRuntime().exec(new String[]{"explorer.exe", f.getAbsolutePath()});
                } catch (IOException e) {
                    MinecraftClient.getInstance().player.sendMessage(Text.of(CoordinatesDisplay.CHAT_PREFIX + "Sorry but I could not open the file. It is saved in the \"config\" folder in your .minecraft directory."), false);
                    CoordinatesDisplay.LOGGER.error("Got an error: ");
                    e.printStackTrace();
                }
            } else {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browseFileDirectory(f);
                    CoordinatesDisplay.LOGGER.info("Opened directory");
                } else {
                    MinecraftClient.getInstance().player.sendMessage(Text.of(CoordinatesDisplay.CHAT_PREFIX + "Sorry but I could not open the file. It is saved in the \"config\" folder in your .minecraft directory."), false);
                    CoordinatesDisplay.LOGGER.warn("Incompatible with desktop class");
                }
            }
        }

        if (reloadConfigKeybind.wasPressed()) {
            CoordinatesDisplay.reloadConfig();
            MinecraftClient.getInstance().player.sendMessage(Text.of(CoordinatesDisplay.CHAT_PREFIX + "Config reloaded!"), false);
            CoordinatesDisplay.LOGGER.info("Reloaded all config");
        }
    }
}
