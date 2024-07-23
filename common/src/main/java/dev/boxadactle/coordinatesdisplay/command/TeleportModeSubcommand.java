package dev.boxadactle.coordinatesdisplay.command;

import dev.boxadactle.boxlib.command.api.BSubcommand;
import dev.boxadactle.boxlib.command.api.subcommand.BasicSubcommand;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.registry.TeleportMode;
import net.minecraft.client.resources.language.I18n;

public class TeleportModeSubcommand {

    public static BSubcommand create() {
        TeleportMode[] modes = TeleportMode.values();

        BSubcommand subcommand = new BasicSubcommand("teleport_mode", CoordinatesCommand::noArgs);

        for (TeleportMode mode : modes) {
            subcommand.registerSubcommand(new BasicSubcommand(mode.name().toLowerCase(), (context) -> {
                CoordinatesDisplay.getConfig().teleportMode = mode;
                CoordinatesDisplay.CONFIG.save();

                CoordinatesDisplay.LOGGER.player.info(I18n.get("button.coordinatesdisplay.tpmode", I18n.get("button.coordinatesdisplay.tpmode." + mode.name().toLowerCase())));

                return 0;
            }));
        }

        return subcommand;
    }

}
