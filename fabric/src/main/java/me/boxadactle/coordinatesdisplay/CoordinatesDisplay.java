package me.boxadactle.coordinatesdisplay;

import io.github.cottonmc.cotton.config.ConfigManager;
import me.boxadactle.coordinatesdisplay.init.Keybinds;
import me.boxadactle.coordinatesdisplay.util.HudOverlay;
import me.boxadactle.coordinatesdisplay.util.ModConfig;
import me.boxadactle.coordinatesdisplay.util.ModLogger;
import net.fabricmc.api.ClientModInitializer;

public class CoordinatesDisplay implements ClientModInitializer {

    public static final String MOD_NAME = "CoordinatesDisplay";
    public static final String MOD_ID = "coordinatesdisplay";

    public static final ModLogger LOGGER = new ModLogger();

    public static boolean shouldRenderOnHud = true;

    public static ModConfig CONFIG;

    public static HudOverlay OVERLAY;

    public static final String UPDATE_URL = "https://boxadactle.github.io/update-urls/coordinates-display/fabric.json";

    public static String UPDATE_MOD_URL;

    public static String MINECRAFT_VERSION;

    public static boolean hasPlayerSeenUpdateMessage = false;

    @Override
    public void onInitializeClient() {
        ModVersion version = ModVersion.getVersion();

        LOGGER.info("Initializing %s...", version.toString());

        CONFIG = new ModConfig();
        CONFIG = ConfigManager.loadConfig(ModConfig.class);
        LOGGER.info("Loaded all config");

        OVERLAY = new HudOverlay(CONFIG);

        Keybinds.register();

        LOGGER.info("Parsed all color prefixes");
    }

    public static void reloadConfig() {
        CONFIG = new ModConfig();
        CONFIG = ConfigManager.loadConfig(ModConfig.class);
        LOGGER.info("Reloaded all config");
    }

}
