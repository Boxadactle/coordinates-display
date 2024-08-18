package dev.boxadactle.coordinatesdisplay.fabric;

import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.fabric.init.Keybinds;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class CoordinatesDisplayFabric implements ClientModInitializer {

    public static boolean deltaError = false;

    @Override
    public void onInitializeClient() {
        CoordinatesDisplay.init();

        ClientTickEvents.END_CLIENT_TICK.register(this::checkBindings);

        Keybinds.register();
    }

    private void checkBindings(Minecraft client) {
        Player player = WorldUtils.getPlayer();
        if (player != null) {
            Keybinds.checkBindings(Position.of(player));
        }
    }

}