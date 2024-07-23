package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BSubcommand;
import dev.boxadactle.boxlib.command.api.subcommand.BasicSubcommand;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.Bindings;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.position.Position;

public class PositionSubcommand {

    public static BSubcommand create() {
        return new BasicSubcommand("position", CoordinatesCommand::noArgs)
                .registerSubcommand(new BasicSubcommand("chat", PositionSubcommand::sendPosInChat))
                .registerSubcommand(new BasicSubcommand("copy", PositionSubcommand::copyPos))
                .registerSubcommand(new BasicSubcommand("copytp", PositionSubcommand::copyPosTp));
    }

    static int sendPosInChat(CommandContext<BCommandSourceStack> ignored) {
        Position pos = Position.of(WorldUtils.getPlayer());

        Bindings.sendLocation(pos);

        return 0;
    }

    static int copyPos(CommandContext<BCommandSourceStack> ignored) {
        Position pos = Position.of(WorldUtils.getPlayer());

        Bindings.copyLocation(pos);

        return 0;
    }

    static int copyPosTp(CommandContext<BCommandSourceStack> ignored) {
        try {
            Position pos = Position.of(WorldUtils.getPlayer());

            Bindings.copyTeleportCommand(pos);

            return 0;
        } catch (Exception ignored2) {
            CoordinatesDisplay.LOGGER.player.info(GuiUtils.getTranslatable("command.coordinatesdisplay.internalError"));
            return 1;
        }
    }

}
