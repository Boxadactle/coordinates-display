package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class MoveHudCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "movehud";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {

        builder.executes(this::openHudPositionScreen);

    }

    private int openHudPositionScreen(CommandContext<FabricClientCommandSource> context) {

        CoordinatesDisplay.shouldHudPositionGuiOpen = true;

        return 0;

    }
}
