package me.boxadactle.coordinatesdisplay.util;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModLogger {

    private final Logger logger;

    private final String prefix = "[" + CoordinatesDisplay.MOD_NAME + "]: ";

    private final String chatPrefix = "§3[§bCoordinatesDisplay§3] ";

    private final MinecraftClient client;

    public ModLogger() {
        logger = LogManager.getFormatterLogger(CoordinatesDisplay.MOD_NAME);

        client = MinecraftClient.getInstance();
    }

    public void error(String msg, Object... data) {
        logger.log(Level.ERROR, prefix + msg, data);
    }

    public void warn(String msg, Object... data) {
        logger.log(Level.WARN, prefix + msg, data);
    }

    public void info(String msg, Object... data) {
        logger.log(Level.INFO, prefix + msg, data);
    }

    public void printStackTrace(Exception e) {
        StackTraceElement[] stacktrace = e.getStackTrace();
        logger.error(prefix + e.getMessage());
        for (StackTraceElement stackTraceElement : stacktrace) {
            logger.error(prefix + "\t" + stackTraceElement.toString());
        }
    }

    public void chatError(String msg, Object... data) {
        if (this.client.player != null) {
            this.client.player.sendMessage(Text.literal(chatPrefix + "§4" + String.format(msg, data)), false);
        }
    }

    public void chatWarn(String msg, Object... data) {
        if (this.client.player != null) {
            this.client.player.sendMessage(Text.literal(chatPrefix + "§3" + String.format(msg, data)), false);
        }
    }

    public void chatInfo(String msg, Object... data) {
        if (this.client.player != null) {
            this.client.player.sendMessage(Text.literal(chatPrefix + "§a" + String.format(msg, data)), false);
        }
    }

    public void sendChatMessage(Text msg) {
        if (this.client.player != null) {
            this.client.player.sendMessage(msg, false);
        }
    }

    public String getChatPrefix() {
        return chatPrefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
