package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BSubcommand;
import dev.boxadactle.boxlib.command.api.subcommand.BasicSubcommand;
import dev.boxadactle.boxlib.command.api.subcommand.IntegerSubcommand;
import dev.boxadactle.boxlib.scheduling.Scheduling;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.screen.config.PositionScreen;
import net.minecraft.client.resources.language.I18n;

public class MoveHudSubcommand {

    public static BSubcommand create() {
        return new BasicSubcommand("movehud", MoveHudSubcommand::moveHud)
                .registerSubcommand(new BasicSubcommand("get", MoveHudSubcommand::get))
                .registerSubcommand(new IntegerSubcommand("xpos", MoveHudSubcommand::xOnly)
                        .registerSubcommand(new IntegerSubcommand("ypos", MoveHudSubcommand::hudPos))
                );
    }

    static int moveHud(CommandContext<BCommandSourceStack> ignored) {
        Scheduling.nextTick(() -> ClientUtils.setScreen(new PositionScreen(null)));

        return 0;
    }

    static int get(CommandContext<BCommandSourceStack> ignored) {
        CoordinatesDisplay.LOGGER.player.info(I18n.get(
                "command.coordinatesdisplay.movehud.get",
                CoordinatesDisplay.getConfig().hudX,
                CoordinatesDisplay.getConfig().hudY
        ));

        return 0;
    }

    static int xOnly(CommandContext<BCommandSourceStack> ignored, int i) {
        CoordinatesDisplay.LOGGER.player.error(I18n.get("command.coordinatesdisplay.movehud.fail"));

        return 1;
    }

    static int hudPos(CommandContext<BCommandSourceStack> context, int y) {
        CoordinatesDisplay.getConfig().hudX = IntegerArgumentType.getInteger(context, "xpos");
        CoordinatesDisplay.getConfig().hudY = y;

        return 0;
    }

}
