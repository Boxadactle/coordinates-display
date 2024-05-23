package dev.boxadactle.coordinatesdisplay.neoforge.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.hud.CoordinatesHuds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModeCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "mode";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {

        String[] modes = CoordinatesHuds.registeredOverlays.keySet().toArray(new String[0]);

        for (String mode : modes) {
            builder.then(Commands.literal(mode.toLowerCase())
                    .executes(c -> {
                        CoordinatesDisplay.getConfig().renderMode = mode;
                        CoordinatesDisplay.CONFIG.save();
                        return 0;
                    })
            );
        }

    }
}
