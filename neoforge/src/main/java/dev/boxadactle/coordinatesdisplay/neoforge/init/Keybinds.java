package dev.boxadactle.coordinatesdisplay.forge.init;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import static dev.boxadactle.coordinatesdisplay.CoordinatesDisplay.Bindings.*;

public class Keybinds {

    public static void register(RegisterKeyMappingsEvent e) {
        e.register(hudEnabled);
        e.register(coordinatesGUIKeybind);

        e.register(copyLocation);
        e.register(sendLocation);
        e.register(copyPosTp);

        e.register(changeHudPosition);
        e.register(cycleDisplayMode);
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
