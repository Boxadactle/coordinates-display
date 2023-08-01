package dev.boxadactle.coordinatesdisplay.fabric.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.init.Keybinds;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ConfigCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "config";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {

        // open config file
        builder.then(ClientCommandManager.literal("file")
                .executes(this::openConfigFile)
        );

        builder.then(ClientCommandManager.literal("reload")
                .executes(this::reloadConfig)
        );


        // empty args
        builder.executes(this::openConfigGui);
    }

    private int openConfigGui(CommandContext<FabricClientCommandSource> context) {

        CoordinatesDisplay.shouldConfigGuiOpen = true;
        CoordinatesDisplay.LOGGER.info("Opening Config GUI");

        return 0;

    }

    private int openConfigFile(CommandContext<FabricClientCommandSource> context) {
        if (ModUtil.openConfigFile()) {
            CoordinatesDisplay.LOGGER.player.info(super.translatable("command.coordinatesdisplay.config.open.success"));
            return 0;
        } else {
            CoordinatesDisplay.LOGGER.info(super.translatable("command.coordinatesdisplay.config.open.fail"));
            return 1;
        }
    }

    private int reloadConfig(CommandContext<FabricClientCommandSource> context) {
        Keybinds.reloadConfigKeybind.setDown(true);
        CoordinatesDisplay.LOGGER.player.info(super.translatable("command.coordinatesdisplay.config.reload"));
        CoordinatesDisplay.LOGGER.info("Reloaded all config");

        return 0;
    }

}
