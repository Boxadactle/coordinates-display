package dev.boxadactle.coordinatesdisplay.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.fabric.init.Keybinds;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class CoordinatesDisplayFabric implements ClientModInitializer {

    public static boolean deltaError = false;

    @Override
    public void onInitializeClient() {
        CoordinatesDisplay.init();

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

    private void renderHud(PoseStack stack, float f) {
        try {
            if (CoordinatesDisplay.HUD.shouldRender(CoordinatesDisplay.getConfig().visibilityFilter)) {
                ModConfig config = CoordinatesDisplay.getConfig();

                CoordinatesDisplay.HUD.render(
                        stack,
                        Position.of(WorldUtils.getPlayer()),
                        config.hudX,
                        config.hudY,
                        config.renderMode,
                        config.startCorner,
                        false,
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