package dev.boxadactle.coordinatesdisplay.fabric;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.fabric.init.Commands;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.fabric.init.Keybinds;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class CoordinatesDisplayFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CoordinatesDisplay.init();

        Commands.register();

        ClientTickEvents.END_CLIENT_TICK.register(this::checkBindings);

        HudRenderCallback.EVENT.register(this::renderHud);

        Keybinds.register();
    }

    private void checkBindings(Minecraft client) {
        Player player = WorldUtils.getPlayer();
        if (player != null) {
            Keybinds.checkBindings(Position.of(player));
        }
    }

    private void renderHud(GuiGraphics guiGraphics, float tickDelta) {
        if (
                !ClientUtils.getOptions().hideGui
                        && CoordinatesDisplay.CONFIG.get().visible
                        && !ClientUtils.getClient().options.renderDebug
                        && CoordinatesDisplay.shouldHudRender
        ) {
            try {
                RenderSystem.enableBlend();

                ModConfig config = CoordinatesDisplay.getConfig();

                CoordinatesDisplay.HUD.render(
                        guiGraphics,
                        Position.of(WorldUtils.getPlayer()),
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