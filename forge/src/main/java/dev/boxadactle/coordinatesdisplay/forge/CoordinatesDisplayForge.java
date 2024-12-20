package dev.boxadactle.coordinatesdisplay.forge;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.Bindings;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModConfig;
import dev.boxadactle.coordinatesdisplay.screen.ConfigScreen;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod(CoordinatesDisplay.MOD_ID)
public class CoordinatesDisplayForge {

    public static boolean deltaError = false;

    public CoordinatesDisplayForge() {
        CoordinatesDisplay.init();

        ModList.get().getModContainerById(CoordinatesDisplay.MOD_ID).ifPresent(c -> c.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen))
        ));
    }

    @Mod.EventBusSubscriber(modid = CoordinatesDisplay.MOD_ID, value = Dist.CLIENT)
    public static class ClientNeoforgeEvents {

        @SubscribeEvent
        public static void keyInput(InputEvent.Key e) {
            Player player = WorldUtils.getPlayer();
            if (player != null) {
                 Bindings.checkBindings(Position.of(player));
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

    @Mod.EventBusSubscriber(modid = CoordinatesDisplay.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void clientSetup(RegisterKeyMappingsEvent e) {
            e.register(Bindings.hudEnabled);
            e.register(Bindings.coordinatesGUIKeybind);
            e.register(Bindings.copyLocation);
            e.register(Bindings.sendLocation);
            e.register(Bindings.copyPosTp);
            e.register(Bindings.changeHudPosition);
            e.register(Bindings.cycleDisplayMode);
        }
    }

}
