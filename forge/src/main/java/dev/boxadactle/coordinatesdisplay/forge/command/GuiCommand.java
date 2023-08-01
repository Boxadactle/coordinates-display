package dev.boxadactle.coordinatesdisplay.forge.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.commands.CommandSourceStack;

public class GuiCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {

        builder.executes(this::openCoordinatesScreen);

    }

    private int openCoordinatesScreen(CommandContext<CommandSourceStack> context) {

        CoordinatesDisplay.shouldCoordinatesGuiOpen = true;

        return 0;

    }
}
