package dev.boxadactle.coordinatesdisplay.fabric.init;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import static dev.boxadactle.coordinatesdisplay.CoordinatesDisplay.Bindings.*;

public class Keybinds {

    public static void register() {
        KeyBindingHelper.registerKeyBinding(hudEnabled);

        KeyBindingHelper.registerKeyBinding(coordinatesGUIKeybind);

        KeyBindingHelper.registerKeyBinding(copyLocation);
        KeyBindingHelper.registerKeyBinding(sendLocation);
        KeyBindingHelper.registerKeyBinding(copyPosTp);

        KeyBindingHelper.registerKeyBinding(changeHudPosition);
        KeyBindingHelper.registerKeyBinding(cycleDisplayMode);
    }

    public static void checkBindings(Position pos) {
        if (hudEnabled.consumeClick()) CoordinatesDisplay.Bindings.toggleHud();

        if (coordinatesGUIKeybind.consumeClick()) CoordinatesDisplay.Bindings.coordinatesGui();

        if (copyLocation.consumeClick()) CoordinatesDisplay.Bindings.copyLocation(pos);

        if (sendLocation.consumeClick()) CoordinatesDisplay.Bindings.sendLocation(pos);

        if (copyPosTp.consumeClick()) CoordinatesDisplay.Bindings.copyTeleportCommand(pos);

        if (changeHudPosition.consumeClick()) CoordinatesDisplay.Bindings.openHudPositionGui();

        if (cycleDisplayMode.consumeClick()) CoordinatesDisplay.Bindings.cycleDisplayMode();
    }

}
