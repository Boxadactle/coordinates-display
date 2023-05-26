package me.boxadactle.coordinatesdisplay;

import me.boxadactle.coordinatesdisplay.init.Keybinds;
<<<<<<< Updated upstream
import me.boxadactle.coordinatesdisplay.util.HudOverlay;
import me.boxadactle.coordinatesdisplay.util.ModConfig;
import me.boxadactle.coordinatesdisplay.util.ModLogger;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
=======
import me.boxadactle.coordinatesdisplay.util.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
>>>>>>> Stashed changes
import net.fabricmc.api.ClientModInitializer;
public class CoordinatesDisplay implements ClientModInitializer {

    public static final String MOD_NAME = "CoordinatesDisplay";
    public static final String MOD_ID = "coordinatesdisplay";

    public static final ModLogger LOGGER = new ModLogger();

    public static boolean shouldRenderOnHud = true;

    public static ConfigHolder<ModConfig> CONFIG;

    public static HudOverlay OVERLAY;

    public static final String UPDATE_URL = "https://boxadactle.github.io/update-urls/coordinates-display/fabric.json";

    public static String UPDATE_MOD_URL;

    public static String MINECRAFT_VERSION;

    @Override
    public void onInitializeClient() {
        ModVersion version = ModVersion.getVersion();

        LOGGER.info("Initializing %s...", version.toString());

        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class);

<<<<<<< Updated upstream
        OVERLAY = new HudOverlay(CONFIG);
=======
        OVERLAY = new HudRenderer();
>>>>>>> Stashed changes

        Keybinds.register();

        LOGGER.info("Parsed all color prefixes");
    }

    public static void resetConfig() {
        CONFIG.setConfig(new ModConfig());
        CONFIG.save();
        LOGGER.info("Reloaded all config");
    }

}
