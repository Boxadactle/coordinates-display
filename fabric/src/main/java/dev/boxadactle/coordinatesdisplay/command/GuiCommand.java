package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class GuiCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {

        builder.executes(this::openCoordinatesScreen);

    }

    private int openCoordinatesScreen(CommandContext<FabricClientCommandSource> context) {

        CoordinatesDisplay.shouldCoordinatesGuiOpen = true;

        return 0;

    }
}
