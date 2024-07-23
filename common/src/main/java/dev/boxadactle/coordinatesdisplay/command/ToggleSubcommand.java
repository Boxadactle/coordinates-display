package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BSubcommand;
import dev.boxadactle.boxlib.command.api.subcommand.BasicSubcommand;
import dev.boxadactle.boxlib.command.api.subcommand.BooleanSubcommand;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.resources.language.I18n;

public class ToggleSubcommand {

    public static BSubcommand create() {
        return new BasicSubcommand("toggle", ToggleSubcommand::toggle)
                .registerSubcommand(new BooleanSubcommand("enabled", ToggleSubcommand::toggleBoolean));
    }

    static int toggle(CommandContext<BCommandSourceStack> ignored) {
        CoordinatesDisplay.getConfig().enabled = !CoordinatesDisplay.getConfig().enabled;
        CoordinatesDisplay.CONFIG.save();

        CoordinatesDisplay.LOGGER.player.info(I18n.get(
                "button.coordinatesdisplay.enabled",
                CoordinatesDisplay.getConfig().enabled ?
                        GuiUtils.YES.getString() :
                        GuiUtils.NO.getString()
        ));

        return 0;
    }

    static int toggleBoolean(CommandContext<BCommandSourceStack> context, boolean value) {
        CoordinatesDisplay.getConfig().enabled = value;
        CoordinatesDisplay.CONFIG.save();

        return 0;
    }

}
