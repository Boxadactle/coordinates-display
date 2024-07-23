package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BSubcommand;
import dev.boxadactle.boxlib.command.api.subcommand.BasicSubcommand;
import dev.boxadactle.boxlib.scheduling.Scheduling;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.screen.ConfigScreen;

public class ConfigSubcommand {
    public static BSubcommand create() {
        return new BasicSubcommand("config", ConfigSubcommand::openConfigGui)
                .registerSubcommand(new BasicSubcommand("file", ConfigSubcommand::openConfigFile))
                .registerSubcommand(new BasicSubcommand("reload", ConfigSubcommand::reloadConfig));
    }

    static int openConfigGui(CommandContext<BCommandSourceStack> ignored) {
        Scheduling.nextTick(() -> ClientUtils.setScreen(new ConfigScreen(null)));
        CoordinatesDisplay.LOGGER.info("Opening Config GUI");

        return 0;
    }

    static int openConfigFile(CommandContext<BCommandSourceStack> ignored) {
        if (ModUtil.openConfigFile()) {
            CoordinatesDisplay.LOGGER.player.info(GuiUtils.getTranslatable("command.coordinatesdisplay.config.open.success"));
            return 0;
        } else {
            CoordinatesDisplay.LOGGER.player.error(GuiUtils.getTranslatable("command.coordinatesdisplay.config.open.fail"));
            return 1;
        }
    }

    static int reloadConfig(CommandContext<BCommandSourceStack> ignored) {
        CoordinatesDisplay.CONFIG.reload();
        CoordinatesDisplay.LOGGER.player.info(GuiUtils.getTranslatable("command.coordinatesdisplay.config.reload"));
        CoordinatesDisplay.LOGGER.info("Reloaded all config");

        return 0;
    }
}
