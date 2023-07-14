package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.commands.CommandSourceStack;

public class MoveHudCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "movehud";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {

        builder.executes(this::openHudPositionScreen);

    }

    private int openHudPositionScreen(CommandContext<CommandSourceStack> context) {

        CoordinatesDisplay.shouldHudPositionGuiOpen = true;

        return 0;

    }
}
