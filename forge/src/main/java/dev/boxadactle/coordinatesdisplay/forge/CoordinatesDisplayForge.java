package dev.boxadactle.coordinatesdisplay.forge;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.config.screen.ConfigScreen;
import dev.boxadactle.coordinatesdisplay.forge.init.Keybinds;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.ConfigGuiHandler;

@SuppressWarnings("unused")
@Mod(CoordinatesDisplay.MOD_ID)
public class CoordinatesDisplayForge {

    public static boolean deltaError = false;

    public CoordinatesDisplayForge() {
        CoordinatesDisplay.init();

        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () ->
                new ConfigGuiHandler.ConfigGuiFactory((minecraft, screen) -> new ConfigScreen(screen))
        );
    }

    @Mod.EventBusSubscriber(modid = CoordinatesDisplay.MOD_ID, value = Dist.CLIENT)
    public static class ClientNeoforgeEvents {

        @SubscribeEvent
        public static void keyInput(InputEvent.KeyInputEvent e) {
            Player player = WorldUtils.getPlayer();
            if (player != null) {
                 Keybinds.checkBindings(Position.of(player));
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void renderHud(RenderGameOverlayEvent.Pre event) {
            try {
                if (CoordinatesDisplay.HUD.shouldRender(CoordinatesDisplay.getConfig().visibilityFilter)) {
                    RenderSystem.enableBlend();

                    ModConfig config = CoordinatesDisplay.getConfig();

                    CoordinatesDisplay.HUD.render(
                            event.getMatrixStack(),
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

    @Mod.EventBusSubscriber(modid = CoordinatesDisplay.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            Keybinds.register();
        }
    }

}
