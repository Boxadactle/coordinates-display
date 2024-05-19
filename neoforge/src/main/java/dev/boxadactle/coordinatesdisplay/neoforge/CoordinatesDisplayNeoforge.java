package dev.boxadactle.coordinatesdisplay.neoforge;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.config.screen.ConfigScreen;
import dev.boxadactle.coordinatesdisplay.neoforge.init.Commands;
import dev.boxadactle.coordinatesdisplay.neoforge.init.Keybinds;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(CoordinatesDisplay.MOD_ID)
public class CoordinatesDisplayNeoforge {

    public CoordinatesDisplayNeoforge() {
        CoordinatesDisplay.init();

        Commands.register();

        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () ->
                (minecraft, screen) -> new ConfigScreen(screen)
        );
    }

    @EventBusSubscriber(modid = CoordinatesDisplay.MOD_ID, value = Dist.CLIENT)
    public static class ClientNeoforgeEvents {

        @SubscribeEvent
        public static void keyInput(InputEvent.Key e) {
            Player player = WorldUtils.getPlayer();
            if (player != null) {
                 Keybinds.checkBindings(Position.of(player));
            }
        }

        @SubscribeEvent(priority = EventPriority.LOW)
        public static void renderHud(RenderGuiEvent.Post e) {
            if (
                    !ClientUtils.getOptions().hideGui
                            && CoordinatesDisplay.CONFIG.get().visible
                            && !ClientUtils.getClient().getDebugOverlay().showDebugScreen()
                            && CoordinatesDisplay.shouldHudRender
            ) {
                try {
                    RenderSystem.enableBlend();

                    ModConfig config = CoordinatesDisplay.getConfig();

                    CoordinatesDisplay.HUD.render(
                            e.getGuiGraphics(),
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

    @EventBusSubscriber(modid = CoordinatesDisplay.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent e) {
             Keybinds.register(e);
        }
    }

}
