package me.boxadactle.coordinatesdisplay;

import me.boxadactle.coordinatesdisplay.gui.ConfigScreen;
import me.boxadactle.coordinatesdisplay.init.Keybinds;
import me.boxadactle.coordinatesdisplay.util.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import java.io.File;

@Mod("coordinatesdisplay")
public class CoordinatesDisplay {

    public static final ModLogger LOGGER = new ModLogger();

    public static final String MOD_NAME = "CoordinatesDisplay";
    public static final String MOD_ID = "coordinatesdisplay";

    public static boolean shouldRenderOnHud = true;

    public static final File configDir = new File(Minecraft.getInstance().gameDirectory.getAbsolutePath() + "\\config");

    public static ConfigHolder<ModConfig> CONFIG;

    public static HudOverlay OVERLAY;

    public static final String UPDATE_URL = "https://boxadactle.github.io/update-urls/coordinates-display/forge.json";

    public static String UPDATE_MOD_URL;

    public static String MINECRAFT_VERSION;

    public static boolean hasPlayerSeenUpdateMessage = false;

    public CoordinatesDisplay() {
        ModVersion version = ModVersion.getVersion();

        LOGGER.info("Loading " + version.toString());

        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class);

        // what a pain
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () ->
                new ConfigGuiHandler.ConfigGuiFactory((minecraft, screen) -> new ConfigScreen(screen)));

        LOGGER.info("Registering key binds");
        Keybinds.register();

        LOGGER.info("Creating instance of HUD overlay");
        OVERLAY = new HudOverlay();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player != null) {
            LocalPlayer p = Minecraft.getInstance().player;
            Keybinds.checkBindings(p.getBlockX(), p.getBlockY(), p.getBlockZ());
        }
    }
}
