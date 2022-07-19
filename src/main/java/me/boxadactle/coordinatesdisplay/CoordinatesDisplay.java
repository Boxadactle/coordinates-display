package me.boxadactle.coordinatesdisplay;

import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.logging.ModLogger;
import me.boxadactle.coordinatesdisplay.init.KeybindsInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Locale;

public class CoordinatesDisplay implements ClientModInitializer {

    public static final String MOD_NAME = "CoordinatesDisplay";
    public static final String MOD_ID = "coordinatesdisplay";

    public static final String CHAT_PREFIX = "§3[§bCoordinatesDisplay§3] §a";

    public static ModLogger LOGGER = new ModLogger(MOD_ID, MOD_NAME);

    public static final File configDir = new File(FabricLoader.getInstance().getConfigDir() + "/" + MOD_NAME);

    public static Config CONFIG;

    public static String DefinitionColorPrefix;

    public static String DataColorPrefix;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing coordinates mod...");

        CONFIG = new Config();
        CONFIG = ConfigManager.loadConfig(Config.class);
        LOGGER.info("Loaded all config");

        KeybindsInit.register();

        parseColorPrefixes();
        LOGGER.info("Parsed all color prefixes");
    }

    public static void reloadConfig() {
        CONFIG = new Config();
        CONFIG = ConfigManager.loadConfig(Config.class);
        parseColorPrefixes();
        LOGGER.info("Reloaded all config");
    }

    public static void parseColorPrefixes() {
        DefinitionColorPrefix = getColorPrefix(CONFIG.definitionColor);
        DataColorPrefix = getColorPrefix(CONFIG.dataColor);
    }

    private static String getColorPrefix(String color) {
        String prefix;
        String c = color.toLowerCase(Locale.ROOT);
        String defaultPrefix = "§f";
        switch(c) {
            case "dark_red":
                prefix = "§4";
                break;
            case "red":
                prefix = "§c";
                break;
            case "gold":
                prefix = "§6";
                break;
            case "yellow":
                prefix = "§e";
                break;
            case "dark_green":
                prefix = "§2";
                break;
            case "green":
                prefix = "§a";
                break;
            case "aqua":
                prefix = "§b";
                break;
            case "dark_aqua":
                prefix = "§3";
                break;
            case "dark_blue":
                prefix = "§1";
                break;
            case "blue":
                prefix = "§9";
                break;
            case "light_purple":
                prefix = "§d";
                break;
            case "dark_purple":
                prefix = "§5";
                break;
            case "white":
                prefix = "§f";
                break;
            case "gray":
                prefix = "§7";
                break;
            case "dark_gray":
                prefix = "§8";
                break;
            case "black":
                prefix = "§0";
                break;
            default:
                prefix = defaultPrefix;
                CoordinatesDisplay.LOGGER.warn("Could not parse color " + color + " so defaulted to " + defaultPrefix);
        }
        LOGGER.info("Parsed color " + color + " as " + prefix);
        return prefix;
    }
}
