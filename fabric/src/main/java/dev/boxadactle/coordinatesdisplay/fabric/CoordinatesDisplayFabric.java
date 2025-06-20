package dev.boxadactle.coordinatesdisplay.fabric;

import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.Bindings;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class CoordinatesDisplayFabric implements ClientModInitializer {

    public static boolean deltaError = false;

    @Override
    public void onInitializeClient() {
        CoordinatesDisplay.init();

        ClientTickEvents.END_CLIENT_TICK.register(this::checkBindings);
        HudElementRegistry.addLast(ResourceLocation.fromNamespaceAndPath(CoordinatesDisplay.MOD_ID, "hud"), (g, d) -> {
                if (CoordinatesDisplay.shouldHudRender) {
                    CoordinatesDisplay.renderHud(g);
                }
        });
    }

    private void checkBindings(Minecraft client) {
        Player player = WorldUtils.getPlayer();
        if (player != null) {
            Bindings.checkBindings(Position.of(player));
        }
    }

}