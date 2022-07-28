package me.boxadactle.coordinatesdisplay.init;

import io.github.cottonmc.cotton.config.ConfigManager;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.gui.CoordinatesScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class Keybinds {
    static KeyBinding visibleKeybind = new KeyBinding("key.coordinatesdisplay.visible", GLFW.GLFW_KEY_O, "category.coordinatesdisplay");
    static KeyBinding coordinatesGUIKeybind = new KeyBinding("key.coordinatesdisplay.coordinatesgui", GLFW.GLFW_KEY_C, "category.coordinatesdisplay");

    static KeyBinding openConfigFileKeybind = new KeyBinding("key.coordinatesdisplay.openfile", GLFW.GLFW_KEY_F6, "category.coordinatesdisplay");
    static KeyBinding reloadConfigKeybind = new KeyBinding("key.coordinatesdisplay.configreload", GLFW.GLFW_KEY_F7, "category.coordinatesdisplay");

    public static void register() {
        KeyBindingHelper.registerKeyBinding(visibleKeybind);
        KeyBindingHelper.registerKeyBinding(coordinatesGUIKeybind);
        KeyBindingHelper.registerKeyBinding(reloadConfigKeybind);
        KeyBindingHelper.registerKeyBinding(openConfigFileKeybind);
    }

    public static void checkBindings(int x, int y, int z) {

        if (visibleKeybind.wasPressed()) {
            CoordinatesDisplay.CONFIG.visible = !CoordinatesDisplay.CONFIG.visible;
            ConfigManager.saveConfig(CoordinatesDisplay.CONFIG);
            CoordinatesDisplay.LOGGER.info("Updated visible property in config file");
        }

        if (coordinatesGUIKeybind.wasPressed()) {
            MinecraftClient.getInstance().setScreen(new CoordinatesScreen(x, y, z));
        }

        if (openConfigFileKeybind.wasPressed()) {
            if (CoordinatesDisplay.openConfigFile()) {
                MinecraftClient.getInstance().player.sendMessage(Text.of(CoordinatesDisplay.CHAT_PREFIX + "Successfully opened config file"), false);
            } else {
                MinecraftClient.getInstance().player.sendMessage(Text.of(CoordinatesDisplay.CHAT_PREFIX + "Sorry but I could not open the file. It is at: " + CoordinatesDisplay.configDir.getAbsolutePath()), false);
            }

        }

        if (reloadConfigKeybind.wasPressed()) {
            CoordinatesDisplay.reloadConfig();
            MinecraftClient.getInstance().player.sendMessage(Text.of(CoordinatesDisplay.CHAT_PREFIX + "Config reloaded!"), false);
            CoordinatesDisplay.LOGGER.info("Reloaded all config");
        }
    }
}
