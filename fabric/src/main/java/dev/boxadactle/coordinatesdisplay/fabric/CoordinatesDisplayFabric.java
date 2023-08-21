package dev.boxadactle.coordinatesdisplay.fabric;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.fabric.init.Commands;
import dev.boxadactle.coordinatesdisplay.init.Keybinds;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;

import static dev.boxadactle.coordinatesdisplay.init.Keybinds.*;

public class CoordinatesDisplayFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CoordinatesDisplay.init();

        Commands.register();

        ClientTickEvents.END_CLIENT_TICK.register(this::checkBindings);

        HudRenderCallback.EVENT.register(this::renderHud);

        KeyBindingHelper.registerKeyBinding(visibleKeybind);
        KeyBindingHelper.registerKeyBinding(coordinatesGUIKeybind);

        KeyBindingHelper.registerKeyBinding(copyLocation);
        KeyBindingHelper.registerKeyBinding(sendLocation);
        KeyBindingHelper.registerKeyBinding(copyPosTp);

        KeyBindingHelper.registerKeyBinding(changeHudPosition);
        KeyBindingHelper.registerKeyBinding(cycleDisplayMode);
    }

    private void checkBindings(Minecraft client) {
        Entity camera = WorldUtils.getCamera();
        if (camera != null) {
            Keybinds.checkBindings(Position.of(camera));
        }
    }

    private void renderHud(GuiGraphics guiGraphics, float tickDelta) {
        if (
                !ClientUtils.getOptions().hideGui
                        && CoordinatesDisplay.CONFIG.get().visible
                        && !ClientUtils.getOptions().renderDebug
                        && CoordinatesDisplay.shouldHudRender
        ) {
            try {
                RenderSystem.enableBlend();

                ModConfig config = CoordinatesDisplay.getConfig();

                CoordinatesDisplay.HUD.render(
                        guiGraphics,
                        Position.of(WorldUtils.getCamera()),
                        config.hudX,
                        config.hudY,
                        config.renderMode,
                        config.startCorner,
                        false,
                        config.hudScale
                );
            } catch (NullPointerException exception) {
                CoordinatesDisplay.LOGGER.printStackTrace(exception);
            }
        }
    }

}