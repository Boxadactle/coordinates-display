package me.boxadactle.coordinatesdisplay.init;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.gui.CoordinatesScreen;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class Keybinds {
    static KeyMapping visibleKeybind = new KeyMapping("key.coordinatesdisplay.visible", GLFW.GLFW_KEY_O, "category.coordinatesdisplay");
    static KeyMapping coordinatesGUIKeybind = new KeyMapping("key.coordinatesdisplay.coordinatesgui", GLFW.GLFW_KEY_C, "category.coordinatesdisplay");

    static KeyMapping openConfigFileKeybind = new KeyMapping("key.coordinatesdisplay.openfile", GLFW.GLFW_KEY_F6, "category.coordinatesdisplay");
    static KeyMapping reloadConfigKeybind = new KeyMapping("key.coordinatesdisplay.configreload", GLFW.GLFW_KEY_F7, "category.coordinatesdisplay");

    static KeyMapping copyLocation = new KeyMapping("key.coordinatesdisplay.copypos", GLFW.GLFW_KEY_B, "category.coordinatesdisplay");
    static KeyMapping sendLocation = new KeyMapping("key.coordinatesdisplay.sendpos", GLFW.GLFW_KEY_X, "category.coordinatesdisplay");
    static KeyMapping copyPosTp = new KeyMapping("key.coordinatesdisplay.copypostp", GLFW.GLFW_KEY_N, "category.coordinatesdisplay");

    public static void register() {
        ClientRegistry.registerKeyBinding(visibleKeybind);
        ClientRegistry.registerKeyBinding(coordinatesGUIKeybind);

        ClientRegistry.registerKeyBinding(openConfigFileKeybind);
        ClientRegistry.registerKeyBinding(reloadConfigKeybind);

        ClientRegistry.registerKeyBinding(copyLocation);
        ClientRegistry.registerKeyBinding(sendLocation);
        ClientRegistry.registerKeyBinding(copyPosTp);
    }

    public static void checkBindings(int x, int y, int z) {

        if (visibleKeybind.isDown() && visibleKeybind.consumeClick()) {
            CoordinatesDisplay.CONFIG.get().visible = !CoordinatesDisplay.CONFIG.get().visible;
            CoordinatesDisplay.CONFIG.save();
            CoordinatesDisplay.LOGGER.info("Updated visible property in config file");
        }

        if (coordinatesGUIKeybind.isDown() && coordinatesGUIKeybind.consumeClick()) {
            Minecraft.getInstance().setScreen(new CoordinatesScreen(x, y, z));
        }

        if (openConfigFileKeybind.isDown() && openConfigFileKeybind.consumeClick()) {
            if (ModUtils.openConfigFile()) {
                CoordinatesDisplay.LOGGER.info("Opened file in native explorer!");
            } else {
                CoordinatesDisplay.LOGGER.chatError("Sorry I could not open the file. It is saved at: " + CoordinatesDisplay.configDir.getAbsolutePath());
            }
        }

        if (reloadConfigKeybind.isDown() && reloadConfigKeybind.consumeClick()) {
            ModUtils.reloadConfig();
            CoordinatesDisplay.LOGGER.chatInfo("Config reloaded!");
        }

        if (copyLocation.isDown() && copyLocation.consumeClick()) {
            Minecraft.getInstance().keyboardHandler.setClipboard(ModUtils.parseText(CoordinatesDisplay.CONFIG.get().copyPosMessage));
            CoordinatesDisplay.LOGGER.chatInfo("Copied to clipboard!");
            CoordinatesDisplay.LOGGER.chatInfo("Copied location to clipboard!");
        }

        if (sendLocation.isDown() && sendLocation.consumeClick()) {
            CoordinatesDisplay.LOGGER.chatInfo(ModUtils.parseText(CoordinatesDisplay.CONFIG.get().posChatMessage));
            CoordinatesDisplay.LOGGER.info("Sent position as chat message");
        }

        if (copyPosTp.isDown() && copyPosTp.consumeClick()) {
            Minecraft.getInstance().keyboardHandler.setClipboard(ModUtils.asTpCommand(x, y, z, ModUtils.getPlayerCurrentDimension()));

            CoordinatesDisplay.LOGGER.chatInfo("Copied position as command");
        }
    }

}
