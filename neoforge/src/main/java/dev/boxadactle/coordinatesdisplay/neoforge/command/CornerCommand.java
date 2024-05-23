package dev.boxadactle.coordinatesdisplay.neoforge.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CornerCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "corner";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {
        ModConfig.StartCorner[] corners =  ModConfig.StartCorner.values();

        for (ModConfig.StartCorner corner : corners) {
            builder.then(Commands.literal(corner.name().toLowerCase())
                    .executes(c -> {
                        CoordinatesDisplay.getConfig().startCorner = corner;
                        CoordinatesDisplay.CONFIG.save();
                        return 0;
                    })
            );
        }
    }
}
