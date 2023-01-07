package me.boxadactle.coordinatesdisplay.util;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModLogger {

    private final Logger logger;

    private final String prefix = "[" + CoordinatesDisplay.MOD_NAME + "]: ";

    private final Minecraft client;

    public PlayerChat player;

    public ModLogger() {
        logger = LogManager.getFormatterLogger(CoordinatesDisplay.MOD_NAME);

        client = Minecraft.getInstance();

        player = new PlayerChat(client);
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

    public class PlayerChat {

        private Minecraft client;

        private Component prefix = ModUtil.colorize(ComponentUtils.wrapInSquareBrackets(
                ModUtil.colorize(Component.literal("Coordinates Display"), 5636095)
        ), 43690).copy().append(" ");

        public PlayerChat(Minecraft minecraft) {
            this.client = minecraft;
        }

        public void error(String msg, Object... data) {
            if (this.client.player != null) {
                this.client.player.sendSystemMessage(
                        prefix.copy().append(ModUtil.colorize(Component.literal(String.format(msg, data)), 0x2f2d2d))
                );
            }
        }

        public void warn(String msg, Object... data) {
            if (this.client.player != null) {
                this.client.player.sendSystemMessage(
                        prefix.copy().append(ModUtil.colorize(Component.literal(String.format(msg, data)), 0xff9966))
                );
            }
        }

        public void info(String msg, Object... data) {
            if (this.client.player != null) {
                this.client.player.sendSystemMessage(
                        prefix.copy().append(ModUtil.colorize(Component.literal(String.format(msg, data)), 5635925))
                );
            }
        }

        public void chat(Component msg) {
            if (this.client.player != null) {
                this.client.player.sendSystemMessage(msg);
            }
        }

        public void publicChat(String msg) {
            if (this.client.player != null) {
                Minecraft.getInstance().player.connection.sendChat(msg);
            }
        }
    }
}