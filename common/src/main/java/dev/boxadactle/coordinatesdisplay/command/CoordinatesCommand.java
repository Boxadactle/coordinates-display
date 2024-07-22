package dev.boxadactle.coordinatesdisplay.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BClientCommand;
import dev.boxadactle.boxlib.scheduling.Scheduling;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.screen.CoordinatesScreen;
import dev.boxadactle.coordinatesdisplay.screen.config.PositionScreen;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.network.chat.Component;

import java.util.List;

public class CoordinatesCommand {

    public static BClientCommand create() {
        return BClientCommand.create("coordinates", CoordinatesCommand::openCoordinatesScreen)
                .registerSubcommand(new ConfigSubcommand())
                .registerSubcommand(new CornerSubcommand())
                .registerSubcommand("help", CoordinatesCommand::showHelpMessage)
                .registerSubcommand(new ModeSubcommand())
                .registerSubcommand(new VisibilitySubcommand())
                .registerSubcommand("movehud", CoordinatesCommand::moveHud)
                .registerSubcommand(new PositionSubcommand())
                .registerSubcommand("toggle", CoordinatesCommand::toggle);
    }

    private static int openCoordinatesScreen(CommandContext<BCommandSourceStack> ignored) {
        Scheduling.nextTick(() -> ClientUtils.setScreen(new CoordinatesScreen(Position.of(WorldUtils.getPlayer()))));

        return 0;
    }

    private static int toggle(CommandContext<BCommandSourceStack> ignored) {
        CoordinatesDisplay.getConfig().enabled = !CoordinatesDisplay.getConfig().enabled;
        CoordinatesDisplay.CONFIG.save();

        return 0;
    }

    private static int showHelpMessage(CommandContext<BCommandSourceStack> ignored) {
        List<Component> components = ImmutableList.of(
                GuiUtils.colorize(Component.translatable("command.coordinatesdisplay.helpmenu"), GuiUtils.AQUA),
                Component.translatable("command.coordinatesdisplay.config"),
                Component.translatable("command.coordinatesdisplay.gui"),
                Component.translatable("command.coordinatesdisplay.help"),
                Component.translatable("command.coordinatesdisplay.mode"),
                Component.translatable("command.coordinatesdisplay.movehud"),
                Component.translatable("command.coordinatesdisplay.position"),
                Component.translatable("command.coordinatesdisplay.visibility")
        );

        components.forEach(c -> {
            CoordinatesDisplay.LOGGER.player.chat(c);
        });

        return 0;
    }

    private static int moveHud(CommandContext<BCommandSourceStack> ignored) {
        Scheduling.nextTick(() -> ClientUtils.setScreen(new PositionScreen(null)));

        return 0;
    }

}