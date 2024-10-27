package dev.boxadactle.coordinatesdisplay;

import com.mojang.blaze3d.platform.InputConstants;
import dev.boxadactle.boxlib.scheduling.Scheduling;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.screen.CoordinatesScreen;
import dev.boxadactle.coordinatesdisplay.screen.config.PositionScreen;
import dev.boxadactle.coordinatesdisplay.position.Position;
import dev.boxadactle.coordinatesdisplay.registry.DisplayMode;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class Bindings {

    public static final KeyMapping hudEnabled = new KeyMapping("key.coordinatesdisplay.hudenabled", GLFW.GLFW_KEY_H, "category.coordinatesdisplay");

    public static final KeyMapping coordinatesGUIKeybind = new KeyMapping("key.coordinatesdisplay.coordinatesgui", GLFW.GLFW_KEY_C, "category.coordinatesdisplay");

    public static final KeyMapping copyLocation = new KeyMapping("key.coordinatesdisplay.copypos", -1, "category.coordinatesdisplay");
    public static final KeyMapping sendLocation = new KeyMapping("key.coordinatesdisplay.sendpos", -1, "category.coordinatesdisplay");
    public static final KeyMapping copyPosTp = new KeyMapping("key.coordinatesdisplay.copypostp", -1, "category.coordinatesdisplay");

    public static final KeyMapping changeHudPosition = new KeyMapping("key.coordinatesdisplay.changeHudPos", GLFW.GLFW_KEY_F9, "category.coordinatesdisplay");
    public static final KeyMapping cycleDisplayMode = new KeyMapping("key.coordinatesdisplay.cycleDisplayMode", GLFW.GLFW_KEY_M, "category.coordinatesdisplay");

    public static final KeyMapping toggle3DCompass = new KeyMapping("key.coordinatesdisplay.toggle3DCompass", GLFW.GLFW_KEY_F8, "category.coordinatesdisplay");

    public static void toggleHud() {
        CoordinatesDisplay.LOGGER.info("Toggling HUD visibility");
        CoordinatesDisplay.CONFIG.get().enabled = !CoordinatesDisplay.CONFIG.get().enabled;
        CoordinatesDisplay.CONFIG.save();
    }

    public static void coordinatesGui() {
        Scheduling.nextTick(() -> ClientUtils.setScreen(new CoordinatesScreen(Position.of(WorldUtils.getPlayer()))));
    }

    public static void copyLocation(Position pos) {
        ClientUtils.getKeyboard().setClipboard(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().copyPosMessage, pos));
        CoordinatesDisplay.LOGGER.player.info("Copied to clipboard!");
        CoordinatesDisplay.LOGGER.info("Copied location to clipboard");
    }

    public static void sendLocation(Position pos) {
        CoordinatesDisplay.LOGGER.player.publicChat(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().posChatMessage, pos));
        CoordinatesDisplay.LOGGER.info("Sent position as chat message");
    }

    public static void copyTeleportCommand(Position pos) {
        ClientUtils.getKeyboard().setClipboard(CoordinatesDisplay.getConfig().teleportMode.toCommand(pos));

        CoordinatesDisplay.LOGGER.player.info("Copied position as teleport command!");
    }

    public static void openHudPositionGui() {
        Scheduling.nextTick(() -> ClientUtils.setScreen(new PositionScreen(null)));
    }

    public static void cycleDisplayMode() {
        if (!InputConstants.isKeyDown(ClientUtils.getWindow(), 340)) CoordinatesDisplay.getConfig().renderMode = DisplayMode.nextMode(CoordinatesDisplay.getConfig().renderMode);
        else CoordinatesDisplay.getConfig().renderMode = DisplayMode.previousMode(CoordinatesDisplay.getConfig().renderMode);

        CoordinatesDisplay.CONFIG.save();
    }

    public static void toggle3DCompass() {
        CoordinatesDisplay.getConfig().render3dCompass = !CoordinatesDisplay.getConfig().render3dCompass;
        CoordinatesDisplay.CONFIG.save();
    }
    
    public static void checkBindings(Position pos) {
        if (hudEnabled.consumeClick()) toggleHud();

        if (coordinatesGUIKeybind.consumeClick()) coordinatesGui();

        if (copyLocation.consumeClick()) copyLocation(pos);

        if (sendLocation.consumeClick()) sendLocation(pos);

        if (copyPosTp.consumeClick()) copyTeleportCommand(pos);

        if (changeHudPosition.consumeClick()) openHudPositionGui();

        if (cycleDisplayMode.consumeClick()) cycleDisplayMode();

        if (toggle3DCompass.consumeClick()) toggle3DCompass();
    }
    
}
