package me.boxadactle.coordinatesdisplay.init;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.gui.CoordinatesScreen;
<<<<<<< Updated upstream
import me.boxadactle.coordinatesdisplay.util.ModUtils;
=======
import me.boxadactle.coordinatesdisplay.gui.config.ChangePositionScreen;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
>>>>>>> Stashed changes
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
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

    static KeyMapping changeHudPosition = new KeyMapping("key.coordinatesdisplay.changeHudPos", GLFW.GLFW_KEY_F9, "category.coordinatesdisplay");

    public static void register(RegisterKeyMappingsEvent e) {
        e.register(visibleKeybind);
        e.register(coordinatesGUIKeybind);

        e.register(openConfigFileKeybind);
        e.register(reloadConfigKeybind);

        e.register(copyLocation);
        e.register(sendLocation);
        e.register(copyPosTp);

        e.register(changeHudPosition);
    }

    public static void checkBindings(double x, double y, double z) {

        if (visibleKeybind.consumeClick()) {
            CoordinatesDisplay.CONFIG.get().visible = !CoordinatesDisplay.CONFIG.get().visible;
            CoordinatesDisplay.CONFIG.save();
            CoordinatesDisplay.LOGGER.info("Updated visible property in config file");
        }

        if (coordinatesGUIKeybind.consumeClick()) {
            Minecraft.getInstance().setScreen(new CoordinatesScreen((int) x, (int) y, (int) z));
        }

        if (openConfigFileKeybind.consumeClick()) {
            if (ModUtils.openConfigFile()) {
                CoordinatesDisplay.LOGGER.info("Opened file in native explorer!");
            } else {
                CoordinatesDisplay.LOGGER.player.warn("Sorry I could not open the file. It is saved at: " + CoordinatesDisplay.configDir.getAbsolutePath());
            }
        }

        if (reloadConfigKeybind.consumeClick()) {
            ModUtils.reloadConfig();
            CoordinatesDisplay.LOGGER.player.info("Config reloaded!");
        }

        if (copyLocation.consumeClick()) {
            Minecraft.getInstance().keyboardHandler.setClipboard(ModUtils.parseText(CoordinatesDisplay.CONFIG.get().copyPosMessage));
            CoordinatesDisplay.LOGGER.player.info("Copied to clipboard!");
            CoordinatesDisplay.LOGGER.info("Copied location to clipboard");
        }

        if (sendLocation.consumeClick()) {
            CoordinatesDisplay.LOGGER.player.info(ModUtils.parseText(CoordinatesDisplay.CONFIG.get().posChatMessage));
            CoordinatesDisplay.LOGGER.info("Sent position as chat message");
        }

        if (copyPosTp.consumeClick()) {
            Minecraft.getInstance().keyboardHandler.setClipboard(ModUtils.asTpCommand(x, y, z, ModUtils.getPlayerCurrentDimension()));

            CoordinatesDisplay.LOGGER.player.info("Copied position as teleport command!");
        }

        if (changeHudPosition.consumeClick()) {
            Minecraft.getInstance().setScreen(new ChangePositionScreen(null));
        }
    }

}
