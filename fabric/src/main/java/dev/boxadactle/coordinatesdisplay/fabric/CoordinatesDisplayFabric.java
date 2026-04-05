package dev.boxadactle.coordinatesdisplay.fabric;

import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.Bindings;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class CoordinatesDisplayFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CoordinatesDisplay.init();

        ClientTickEvents.END_CLIENT_TICK.register(this::checkBindings);

        KeyMappingHelper.registerKeyMapping(Bindings.hudEnabled);
        KeyMappingHelper.registerKeyMapping(Bindings.coordinatesGUIKeybind);
        KeyMappingHelper.registerKeyMapping(Bindings.markGuiKeybind);
        KeyMappingHelper.registerKeyMapping(Bindings.copyLocation);
        KeyMappingHelper.registerKeyMapping(Bindings.sendLocation);
        KeyMappingHelper.registerKeyMapping(Bindings.copyPosTp);
        KeyMappingHelper.registerKeyMapping(Bindings.changeHudPosition);
        KeyMappingHelper.registerKeyMapping(Bindings.cycleDisplayMode);
        KeyMappingHelper.registerKeyMapping(Bindings.toggle3DCompass);
    }

    private void checkBindings(Minecraft client) {
        Player player = WorldUtils.getPlayer();
        if (player != null) {
            Bindings.checkBindings(Position.of(player));
        }
    }

}