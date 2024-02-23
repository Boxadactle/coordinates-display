package dev.boxadactle.coordinatesdisplay.forge;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.forge.init.Commands;
import dev.boxadactle.coordinatesdisplay.forge.init.Keybinds;
import dev.boxadactle.coordinatesdisplay.config.screen.ConfigScreen;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import static net.minecraftforge.client.ConfigScreenHandler.*;

@Mod(CoordinatesDisplay.MOD_ID)
public class CoordinatesDisplayForge {

    public CoordinatesDisplayForge() {
        CoordinatesDisplay.init();

        Commands.register();

        // what a pain
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenFactory.class, () ->
                new ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen)));
    }

    @Mod.EventBusSubscriber(modid = CoordinatesDisplay.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void keyInput(InputEvent.Key e) {
            Player player = WorldUtils.getPlayer();
            if (player != null) {
                Keybinds.checkBindings(Position.of(player));
            }
        }

        @SubscribeEvent(priority = EventPriority.LOW)
        public static void renderHud(RenderGuiEvent.Pre e) {
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

    @Mod.EventBusSubscriber(modid = CoordinatesDisplay.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent e) {
            Keybinds.register(e);
        }
    }
}