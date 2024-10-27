package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BCommand;
import dev.boxadactle.boxlib.command.api.subcommand.BasicSubcommand;
import dev.boxadactle.boxlib.scheduling.Scheduling;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.Bindings;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.position.Position;
import dev.boxadactle.coordinatesdisplay.screen.CoordinatesScreen;
import dev.boxadactle.coordinatesdisplay.screen.config.PositionScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public class CoordinatesCommand {

    public static BCommand createCommand() {
        return BCommand.create("coordinates", CoordinatesCommand::openCoordinatesScreen)
                .registerSubcommand(new BasicSubcommand("toggle3dCompass", CoordinatesCommand::toggleCompass))
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

    static int toggleCompass(CommandContext<BCommandSourceStack> ignored) {
        Bindings.toggle3DCompass();

        String message = CoordinatesDisplay.CONFIG.get().render3dCompass ? GuiUtils.ON.getString() : GuiUtils.OFF.getString();
        CoordinatesDisplay.LOGGER.player.info(I18n.get("command.coordinatesdisplay.toggle3dCompass", message));

        return 0;
    }

}
