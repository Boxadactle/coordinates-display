package me.boxadactle.coordinatesdisplay;

import me.boxadactle.coordinatesdisplay.gui.ConfigScreen;
import me.boxadactle.coordinatesdisplay.util.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
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

    public static HudRenderer OVERLAY;

    public static ModVersion MOD_VERSION;

    public CoordinatesDisplay() {
        MOD_VERSION = new ModVersion();

        LOGGER.info("Loading " + MOD_VERSION);

        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class);

        // what a pain forge why
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen)));

        LOGGER.info("Creating instance of HUD overlay");
        OVERLAY = new HudRenderer();

        MinecraftForge.EVENT_BUS.register(this);
    }
}
