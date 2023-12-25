package dev.boxadactle.coordinatesdisplay.forge.init;

import com.mojang.blaze3d.platform.InputConstants;
import dev.boxadactle.boxlib.math.mathutils.Mappers;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static KeyMapping visibleKeybind = new KeyMapping("key.coordinatesdisplay.visible", GLFW.GLFW_KEY_O, "category.coordinatesdisplay");
    public static KeyMapping coordinatesGUIKeybind = new KeyMapping("key.coordinatesdisplay.coordinatesgui", GLFW.GLFW_KEY_C, "category.coordinatesdisplay");

    public static KeyMapping copyLocation = new KeyMapping("key.coordinatesdisplay.copypos", GLFW.GLFW_KEY_B, "category.coordinatesdisplay");
    public static KeyMapping sendLocation = new KeyMapping("key.coordinatesdisplay.sendpos", GLFW.GLFW_KEY_X, "category.coordinatesdisplay");
    public static KeyMapping copyPosTp = new KeyMapping("key.coordinatesdisplay.copypostp", GLFW.GLFW_KEY_N, "category.coordinatesdisplay");

    public static KeyMapping changeHudPosition = new KeyMapping("key.coordinatesdisplay.changeHudPos", GLFW.GLFW_KEY_F9, "category.coordinatesdisplay");
    public static KeyMapping cycleDisplayMode = new KeyMapping("key.coordinatesdisplay.cycleDisplayMode", GLFW.GLFW_KEY_M, "category.coordinatesdisplay");

    public static void register(RegisterKeyMappingsEvent e) {
        e.register(visibleKeybind);
        e.register(coordinatesGUIKeybind);

        e.register(copyLocation);
        e.register(sendLocation);
        e.register(copyPosTp);

        e.register(changeHudPosition);
        e.register(cycleDisplayMode);
    }

    public static void checkBindings(Position pos) {
        if (visibleKeybind.consumeClick()) CoordinatesDisplay.Bindings.visible();

        if (coordinatesGUIKeybind.consumeClick()) CoordinatesDisplay.Bindings.coordinatesGui();

        if (copyLocation.consumeClick()) CoordinatesDisplay.Bindings.copyLocation(pos);

        if (sendLocation.consumeClick()) CoordinatesDisplay.Bindings.sendLocation(pos);

        if (copyPosTp.consumeClick()) CoordinatesDisplay.Bindings.copyTeleportCommand(pos);

        if (changeHudPosition.consumeClick()) CoordinatesDisplay.Bindings.openHudPositionGui();

        if (cycleDisplayMode.consumeClick()) CoordinatesDisplay.Bindings.cycleDisplayMode();
    }

}
