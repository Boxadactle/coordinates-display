package me.boxadactle.coordinatesdisplay;

import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.logging.ModLogger;
import me.boxadactle.coordinatesdisplay.init.KeybindsInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.apache.commons.lang3.SystemUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
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

    public static final String[] colors = {
            "white", "gray", "dark_gray", "black",
            "dark_red", "red",
            "gold", "yellow",
            "dark_green", "green",
            "aqua", "dark_aqua",
            "blue", "dark_blue",
            "light_purple", "dark_purple"
    };

    public static int getColorIndex(String color) {
        int index = -1;

        for (int i = 0; i < colors.length; i++)
            if (colors[i].equalsIgnoreCase(color)) index = i;

        return index;
    }

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

    public static String getColorPrefix(String color) {
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

    public static boolean openConfigFile() {
        LOGGER.info("Trying to open file in native file explorer...");
        File f = configDir;
        boolean worked;
        if (SystemUtils.OS_NAME.toLowerCase().contains("windows")) {
            try {
                Runtime.getRuntime().exec(new String[]{"explorer.exe", f.getAbsolutePath()});
                worked = true;
            } catch (IOException e) {
                LOGGER.error("Got an error: ");
                e.printStackTrace();
                worked = false;
            }
        } else {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browseFileDirectory(f);
                LOGGER.info("Opened directory");
                worked = true;
            } else {
                MinecraftClient.getInstance().player.sendMessage(Text.of(CHAT_PREFIX + "Sorry but I could not open the file. It is saved in the \"config\" folder in your .minecraft directory."), false);
                LOGGER.warn("Incompatible with desktop class");
                worked = false;
            }
        }

        return worked;
    }

    public static void resetConfig() {
        CONFIG.visible = ConfigDefault.visible;
        CONFIG.roundPosToTwoDecimals = ConfigDefault.roundPosToTwoDecimals;

        CONFIG.renderBiome = ConfigDefault.renderBiome;
        CONFIG.renderDirection = ConfigDefault.renderDirection;
        CONFIG.renderChunkData = ConfigDefault.renderChunkData;
        CONFIG.renderBackground = ConfigDefault.renderBackground;

        CONFIG.definitionColor = ConfigDefault.definitionColor;
        CONFIG.dataColor = ConfigDefault.dataColor;

        CONFIG.padding = ConfigDefault.padding;
        CONFIG.textPadding = ConfigDefault.textPadding;

        ConfigManager.saveConfig(CONFIG);
        parseColorPrefixes();
    }
}
