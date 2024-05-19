package dev.boxadactle.coordinatesdisplay.fabric.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

import java.util.List;

public class HelpCommand extends CoordinatesCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        builder.executes(this::sendHelpMessage);
    }

    private int sendHelpMessage(CommandContext<FabricClientCommandSource> context) {
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

}
