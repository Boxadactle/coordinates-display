package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.command.BCommandManager;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BClientSubcommand;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;

public class ConfigSubcommand implements BClientSubcommand {
    @Override
    public ArgumentBuilder<BCommandSourceStack, ?> getSubcommand() {
        return BCommandManager.literal("config")
                .executes(this::openConfigGui);
    }

    @Override
    public void build(ArgumentBuilder<BCommandSourceStack, ?> builder) {
        builder.then(BCommandManager.literal("file")
                .executes(this::openConfigFile)
        );

        builder.then(BCommandManager.literal("reload")
                .executes(this::reloadConfig)
        );

        builder.then(BCommandManager.literal("gui")
                .executes(this::openConfigGui)
        );
    }

    private int openConfigGui(CommandContext<BCommandSourceStack> ignored) {
        CoordinatesDisplay.shouldConfigGuiOpen = true;
        CoordinatesDisplay.LOGGER.info("Opening Config GUI");

        return 0;
    }

    private int openConfigFile(CommandContext<BCommandSourceStack> ignored) {
        if (ModUtil.openConfigFile()) {
            CoordinatesDisplay.LOGGER.player.info(GuiUtils.getTranslatable("command.coordinatesdisplay.config.open.success"));
            return 0;
        } else {
            CoordinatesDisplay.LOGGER.info(GuiUtils.getTranslatable("command.coordinatesdisplay.config.open.fail"));
            return 1;
        }
    }

    private int reloadConfig(CommandContext<BCommandSourceStack> ignored) {
        CoordinatesDisplay.CONFIG.reload();
        CoordinatesDisplay.LOGGER.player.info(GuiUtils.getTranslatable("command.coordinatesdisplay.config.reload"));
        CoordinatesDisplay.LOGGER.info("Reloaded all config");

        return 0;
    }
}
