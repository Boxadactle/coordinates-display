package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BSubcommand;
import dev.boxadactle.boxlib.command.api.subcommand.BasicSubcommand;
import dev.boxadactle.boxlib.command.api.subcommand.IntegerSubcommand;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.scheduling.Scheduling;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.marking.MarkGui;
import dev.boxadactle.coordinatesdisplay.marking.MarkSerializer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.resources.language.I18n;

public class MarkSubcommand {

    public static BSubcommand create() {
        return new BasicSubcommand("mark", MarkSubcommand::opengui)
                .registerSubcommand(
                        new BasicSubcommand("set", CoordinatesCommand::noArgs)
                                .registerSubcommand(new IntegerSubcommand("x", MarkSubcommand::oneNumber)
                                        .registerSubcommand(new IntegerSubcommand("y", MarkSubcommand::oneNumber)
                                                .registerSubcommand(new IntegerSubcommand("z", MarkSubcommand::coordinates))
                                        )
                                )
                )
                .registerSubcommand(new BasicSubcommand("here", MarkSubcommand::here))
                .registerSubcommand(new BasicSubcommand("clear", MarkSubcommand::clearMark))
                .registerSubcommand(new BasicSubcommand("share", MarkSubcommand::share));
    }

    private static int opengui(CommandContext<BCommandSourceStack> bCommandSourceStackCommandContext) {
        Scheduling.nextTick(() -> ClientUtils.setScreen(new MarkGui()));
        return 0;
    }

    private static int share(CommandContext<BCommandSourceStack> bCommandSourceStackCommandContext) {
        if (CoordinatesDisplay.MARK_POS == null) {
            CoordinatesDisplay.LOGGER.player.error(I18n.get("command.coordinatesdisplay.mark.notset"));
            return 1;
        }
        CoordinatesDisplay.LOGGER.player.publicChat(MarkSerializer.serialize(CoordinatesDisplay.MARK_POS));
        return 0;
    }

    private static int here(CommandContext<BCommandSourceStack> bCommandSourceStackCommandContext) {
        Position p = Position.of(WorldUtils.getPlayer());

        CoordinatesDisplay.MARK_POS = p.position.getBlockPos();
        CoordinatesDisplay.LOGGER.player.info(I18n.get("command.coordinatesdisplay.mark", p.position.getBlockPos().x, p.position.getBlockPos().y, p.position.getBlockPos().z));

        return 0;
    }

    private static int coordinates(CommandContext<BCommandSourceStack> stack, int z) {
        int x = stack.getArgument("x", Integer.class);
        int y = stack.getArgument("y", Integer.class);

        CoordinatesDisplay.MARK_POS = new Vec3<>(x, y, z);
        CoordinatesDisplay.LOGGER.player.info(I18n.get("command.coordinatesdisplay.mark", x, y, z));

        return 0;
    }

    private static int oneNumber(CommandContext<BCommandSourceStack> bCommandSourceStackCommandContext, int aDouble) {
        return CoordinatesCommand.noArgs(null);
    }


    private static int clearMark(CommandContext<BCommandSourceStack> bCommandSourceStackCommandContext) {
        CoordinatesDisplay.MARK_POS = null;
        CoordinatesDisplay.LOGGER.player.info(I18n.get("command.coordinatesdisplay.mark.clear"));

        return 0;
    }

}
