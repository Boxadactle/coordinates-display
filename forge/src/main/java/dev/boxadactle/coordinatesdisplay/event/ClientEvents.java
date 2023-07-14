package dev.boxadactle.coordinatesdisplay.event;

import dev.boxadactle.coordinatesdisplay.init.Commands;
import dev.boxadactle.coordinatesdisplay.init.Keybinds;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = "coordinatesdisplay", value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void keyInput(InputEvent.Key e) {
            Entity camera = Minecraft.getInstance().getCameraEntity();
            if (camera != null) {
                Position pos = Position.of(Minecraft.getInstance().getCameraEntity());
                Keybinds.checkBindings(pos);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = "coordinatesdisplay", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void keyRegister(RegisterKeyMappingsEvent e) {
            Keybinds.register(e);
        }

    }
}
