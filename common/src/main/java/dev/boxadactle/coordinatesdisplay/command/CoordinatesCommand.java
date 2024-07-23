package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BCommand;
import dev.boxadactle.boxlib.command.api.subcommand.BasicSubcommand;
import dev.boxadactle.boxlib.scheduling.Scheduling;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.position.Position;
import dev.boxadactle.coordinatesdisplay.screen.CoordinatesScreen;
import dev.boxadactle.coordinatesdisplay.screen.config.PositionScreen;
import net.minecraft.client.resources.language.I18n;

public class CoordinatesCommand {

    public static BCommand createCommand() {
        return BCommand.create("coordinates", CoordinatesCommand::openCoordinatesScreen)
                .registerSubcommand(ToggleSubcommand.create())
                .registerSubcommand(MoveHudSubcommand.create())
                .registerSubcommand(ConfigSubcommand.create())
                .registerSubcommand(CornerSubcommand.create())
                .registerSubcommand(ModeSubcommand.create())
                .registerSubcommand(VisibilitySubcommand.create())
                .registerSubcommand(PositionSubcommand.create())
                .registerSubcommand(TeleportModeSubcommand.create());
    }

    static int openCoordinatesScreen(CommandContext<BCommandSourceStack> ignored) {
        Scheduling.nextTick(() -> ClientUtils.setScreen(new CoordinatesScreen(Position.of(WorldUtils.getPlayer()))));

        return 0;
    }

    static int noArgs(CommandContext<BCommandSourceStack> ignored) {
        CoordinatesDisplay.LOGGER.player.error(I18n.get("command.coordinatesdisplay.emptyArgs"));

        return 1;
    }

}
