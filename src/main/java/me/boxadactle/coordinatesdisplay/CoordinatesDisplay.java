package me.boxadactle.coordinatesdisplay;

import io.github.cottonmc.cotton.config.ConfigManager;
import me.boxadactle.coordinatesdisplay.init.Keybinds;
import me.boxadactle.coordinatesdisplay.util.HudOverlay;
import me.boxadactle.coordinatesdisplay.util.ModConfig;
import me.boxadactle.coordinatesdisplay.util.ModLogger;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class CoordinatesDisplay implements ClientModInitializer {

    public static final String MOD_NAME = "CoordinatesDisplay";
    public static final String MOD_ID = "coordinatesdisplay";
    public static final String MOD_VERSION = "1.3.0";

    public static final ModLogger LOGGER = new ModLogger();

    public static final File configfile = new File(FabricLoader.getInstance().getConfigDir() + "/" + MOD_ID + ".json");

    public static boolean shouldRenderOnHud = true;

    public static ModConfig CONFIG;

    public static String DataColorPrefix;

    public static String DeathposColorPrefix;

    public static HudOverlay OVERLAY;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing %s v%s...", MOD_ID, MOD_VERSION);

        CONFIG = new ModConfig();
        CONFIG = ConfigManager.loadConfig(ModConfig.class);
        LOGGER.info("Loaded all config");

        OVERLAY = new HudOverlay(CONFIG);

        Keybinds.register();

        parseColorPrefixes();
        LOGGER.info("Parsed all color prefixes");
    }

    public static void reloadConfig() {
        CONFIG = new ModConfig();
        CONFIG = ConfigManager.loadConfig(ModConfig.class);
        parseColorPrefixes();
        LOGGER.info("Reloaded all config");
    }

    public static void parseColorPrefixes() {
        DataColorPrefix = ModUtils.getColorPrefix(CONFIG.dataColor);
        DeathposColorPrefix = ModUtils.getColorPrefix(CONFIG.deathPosColor);
    }

}
