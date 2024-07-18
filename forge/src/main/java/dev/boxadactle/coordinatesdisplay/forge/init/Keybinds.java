package dev.boxadactle.coordinatesdisplay.forge.init;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import static dev.boxadactle.coordinatesdisplay.CoordinatesDisplay.Bindings.*;

public class Keybinds {

    public static void register() {
        ClientRegistry.registerKeyBinding(hudEnabled);
        ClientRegistry.registerKeyBinding(coordinatesGUIKeybind);

        ClientRegistry.registerKeyBinding(copyLocation);
        ClientRegistry.registerKeyBinding(sendLocation);
        ClientRegistry.registerKeyBinding(copyPosTp);

        ClientRegistry.registerKeyBinding(changeHudPosition);
        ClientRegistry.registerKeyBinding(cycleDisplayMode);
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
