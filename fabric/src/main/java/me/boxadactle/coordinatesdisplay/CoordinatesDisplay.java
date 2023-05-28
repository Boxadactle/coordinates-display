package me.boxadactle.coordinatesdisplay;

import me.boxadactle.coordinatesdisplay.init.Keybinds;
import me.boxadactle.coordinatesdisplay.util.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
public class CoordinatesDisplay implements ClientModInitializer {

    public static final String MOD_NAME = "CoordinatesDisplay";
    public static final String MOD_ID = "coordinatesdisplay";

    public static final ModLogger LOGGER = new ModLogger();

    public static boolean shouldRenderOnHud = true;

    public static ConfigHolder<ModConfig> CONFIG;

    public static HudRenderer OVERLAY;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing %s...", ModVersion.getString());

        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class);

        OVERLAY = new HudRenderer();

        Keybinds.register();

        LOGGER.info("Parsed all color prefixes");
    }

    public static void resetConfig() {
        CONFIG.setConfig(new ModConfig());
        CONFIG.save();
        LOGGER.info("Reloaded all config");
    }

}
