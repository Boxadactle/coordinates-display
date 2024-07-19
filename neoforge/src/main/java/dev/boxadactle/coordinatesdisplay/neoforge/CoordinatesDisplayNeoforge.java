package dev.boxadactle.coordinatesdisplay.forge;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.config.screen.ConfigScreen;
import dev.boxadactle.coordinatesdisplay.hud.UnknownRendererException;
import dev.boxadactle.coordinatesdisplay.hud.UnknownVisibilityFilterException;
import dev.boxadactle.coordinatesdisplay.forge.init.Keybinds;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.network.chat.Component;
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

@SuppressWarnings("unused")
@Mod(CoordinatesDisplay.MOD_ID)
public class CoordinatesDisplayNeoforge {

    public static boolean deltaError = false;

    public CoordinatesDisplayNeoforge() {
        CoordinatesDisplay.init();

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
        public static void renderHud(RenderGuiEvent.Post event) {
            try {
                if (CoordinatesDisplay.HUD.shouldRender(CoordinatesDisplay.getConfig().visibilityFilter)) {
                    RenderSystem.enableBlend();

                    ModConfig config = CoordinatesDisplay.getConfig();

                    CoordinatesDisplay.HUD.render(
                            event.getGuiGraphics(),
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
                if (deltaError) {
                    throw new RuntimeException(e);
                }

                CoordinatesDisplay.LOGGER.error("Unknown error from config file");
                CoordinatesDisplay.LOGGER.printStackTrace(e);

                CoordinatesDisplay.LOGGER.player.warn(GuiUtils.getTranslatable("message.coordinatesdisplay.configError"));
                CoordinatesDisplay.CONFIG.resetConfig();

                deltaError = true;
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
