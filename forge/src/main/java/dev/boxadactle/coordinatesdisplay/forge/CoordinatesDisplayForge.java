package dev.boxadactle.coordinatesdisplay.forge;

import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.Bindings;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.position.Position;
import dev.boxadactle.coordinatesdisplay.screen.ConfigScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod(CoordinatesDisplay.MOD_ID)
public class CoordinatesDisplayForge {

    public static boolean deltaError = false;

    public CoordinatesDisplayForge() {
        CoordinatesDisplay.init();

        ModList.get().getModContainerById(CoordinatesDisplay.MOD_ID).ifPresent(modContainer -> modContainer.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory(((minecraft, screen) -> new ConfigScreen(screen)))
        ));
    }

    @Mod.EventBusSubscriber(modid = CoordinatesDisplay.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void keyInput(InputEvent.Key e) {
            Player player = WorldUtils.getPlayer();
            if (player != null) {
                Bindings.checkBindings(Position.of(player));
            }
        }

        @SubscribeEvent
        public static void renderHud(CustomizeGuiOverlayEvent.Chat e) {
            CoordinatesDisplay.renderHud(e.getGuiGraphics());
        }

    }

    @Mod.EventBusSubscriber(modid = CoordinatesDisplay.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent e) {
            e.register(Bindings.hudEnabled);
            e.register(Bindings.coordinatesGUIKeybind);
            e.register(Bindings.copyLocation);
            e.register(Bindings.sendLocation);
            e.register(Bindings.copyPosTp);
            e.register(Bindings.changeHudPosition);
            e.register(Bindings.cycleDisplayMode);
            e.register(Bindings.toggle3DCompass);
        }
    }

}