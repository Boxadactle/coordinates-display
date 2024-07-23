package dev.boxadactle.coordinatesdisplay.fabric;

import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.Bindings;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModConfig;
import dev.boxadactle.coordinatesdisplay.hud.Hud;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

public class CoordinatesDisplayFabric implements ClientModInitializer {

    public static boolean deltaError = false;

    @Override
    public void onInitializeClient() {
        CoordinatesDisplay.init();

        ClientTickEvents.END_CLIENT_TICK.register(this::checkBindings);

        HudRenderCallback.EVENT.register(this::renderHud);

        KeyBindingHelper.registerKeyBinding(Bindings.hudEnabled);
        KeyBindingHelper.registerKeyBinding(Bindings.coordinatesGUIKeybind);
        KeyBindingHelper.registerKeyBinding(Bindings.copyLocation);
        KeyBindingHelper.registerKeyBinding(Bindings.sendLocation);
        KeyBindingHelper.registerKeyBinding(Bindings.copyPosTp);
        KeyBindingHelper.registerKeyBinding(Bindings.changeHudPosition);
        KeyBindingHelper.registerKeyBinding(Bindings.cycleDisplayMode);
    }

    private void checkBindings(Minecraft client) {
        Player player = WorldUtils.getPlayer();
        if (player != null) {
            Bindings.checkBindings(Position.of(player));
        }
    }

    private void renderHud(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        try {
            if (CoordinatesDisplay.HUD.shouldRender(CoordinatesDisplay.getConfig().visibilityFilter)) {
                ModConfig config = CoordinatesDisplay.getConfig();

                CoordinatesDisplay.HUD.render(
                        guiGraphics,
                        Hud.RenderType.HUD,
                        Position.of(WorldUtils.getPlayer()),
                        config.hudX,
                        config.hudY,
                        config.renderMode,
                        config.startCorner,
                        config.hudScale
                );
            }
        } catch (NullPointerException e) {
            if (CoordinatesDisplayFabric.deltaError) {
                throw new RuntimeException(e);
            }

            CoordinatesDisplay.LOGGER.error("Unknown error from config file");
            CoordinatesDisplay.LOGGER.printStackTrace(e);

            CoordinatesDisplay.LOGGER.player.warn(GuiUtils.getTranslatable("message.coordinatesdisplay.configError"));
            CoordinatesDisplay.CONFIG.resetConfig();

            CoordinatesDisplayFabric.deltaError = true;
        }
    }

}