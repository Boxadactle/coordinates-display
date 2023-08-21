package dev.boxadactle.coordinatesdisplay.forge.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ConfigCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "config";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {

        // open config file
        builder.then(Commands.literal("file")
                .executes(this::openConfigFile)
        );

        builder.then(Commands.literal("reload")
                .executes(this::reloadConfig)
        );


        // empty args
        builder.executes(this::openConfigGui);
    }

    private int openConfigGui(CommandContext<CommandSourceStack> context) {

        CoordinatesDisplay.shouldConfigGuiOpen = true;
        CoordinatesDisplay.LOGGER.info("Opening Config GUI");

        return 0;

    }

    private int openConfigFile(CommandContext<CommandSourceStack> context) {
        if (ModUtil.openConfigFile()) {
            CoordinatesDisplay.LOGGER.player.info(super.translatable("command.coordinatesdisplay.config.open.success"));
            return 0;
        } else {
            CoordinatesDisplay.LOGGER.info(super.translatable("command.coordinatesdisplay.config.open.fail"));
            return 1;
        }
    }

    private int reloadConfig(CommandContext<CommandSourceStack> context) {
        CoordinatesDisplay.CONFIG.reload();
        CoordinatesDisplay.LOGGER.player.info(super.translatable("command.coordinatesdisplay.config.reload"));
        CoordinatesDisplay.LOGGER.info("Reloaded all config");

        return 0;
    }

}
