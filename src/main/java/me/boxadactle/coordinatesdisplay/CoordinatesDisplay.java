package me.boxadactle.coordinatesdisplay;

import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.logging.ModLogger;
import me.boxadactle.coordinatesdisplay.init.Keybinds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.ScreenHandler;
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
    public static final String MOD_VERSION = "1.2.0";

    public static ModLogger LOGGER = new ModLogger(MOD_ID, MOD_NAME);

    public static final File configDir = new File(FabricLoader.getInstance().getConfigDir() + "/" + MOD_NAME);

    public static Config CONFIG;

    public static String DefinitionColorPrefix;

    public static String DataColorPrefix;

    public static String DeathposColorPrefix;

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

        Keybinds.register();

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
        DeathposColorPrefix = getColorPrefix(CONFIG.deathPosColor);
    }

    public static String getColorPrefix(String color) {
        String prefix;
        String c = color.toLowerCase(Locale.ROOT);
        String defaultPrefix = "§f";
        switch (c) {
            case "dark_red" -> prefix = "§4";
            case "red", "rainbow" -> prefix = "§c";
            case "gold" -> prefix = "§6";
            case "yellow" -> prefix = "§e";
            case "dark_green" -> prefix = "§2";
            case "green" -> prefix = "§a";
            case "aqua" -> prefix = "§b";
            case "dark_aqua" -> prefix = "§3";
            case "dark_blue" -> prefix = "§1";
            case "blue" -> prefix = "§9";
            case "light_purple" -> prefix = "§d";
            case "dark_purple" -> prefix = "§5";
            case "white" -> prefix = "§f";
            case "gray" -> prefix = "§7";
            case "dark_gray" -> prefix = "§8";
            case "black" -> prefix = "§0";
            default -> {
                prefix = defaultPrefix;
                CoordinatesDisplay.LOGGER.warn("Could not parse color " + color + " so defaulted to " + defaultPrefix);
            }
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
                if (MinecraftClient.getInstance().player != null) {
                    MinecraftClient.getInstance().player.sendMessage(Text.of(CHAT_PREFIX + "Sorry but I could not open the file. It is saved in the \"config\" folder in your .minecraft directory."), false);
                }
                LOGGER.warn("Incompatible with desktop class");
                worked = false;
            }
        }

        return worked;
    }

    public static void resetConfig() {
        CONFIG.displayPosOnDeathScreen = ConfigDefault.displayPosOnDeathScreen;
        CONFIG.showDeathPosInChat = ConfigDefault.showDeathPosInChat;

        CONFIG.visible = ConfigDefault.visible;
        CONFIG.roundPosToTwoDecimals = ConfigDefault.roundPosToTwoDecimals;

        CONFIG.renderBiome = ConfigDefault.renderBiome;
        CONFIG.renderDirection = ConfigDefault.renderDirection;
        CONFIG.renderChunkData = ConfigDefault.renderChunkData;
        CONFIG.renderBackground = ConfigDefault.renderBackground;

        CONFIG.definitionColor = ConfigDefault.definitionColor;
        CONFIG.dataColor = ConfigDefault.dataColor;
        CONFIG.deathPosColor = ConfigDefault.deathPosColor;

        CONFIG.padding = ConfigDefault.padding;
        CONFIG.textPadding = ConfigDefault.textPadding;

        ConfigManager.saveConfig(CONFIG);
        parseColorPrefixes();
    }
}
