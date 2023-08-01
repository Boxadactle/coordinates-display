package dev.boxadactle.coordinatesdisplay.forge.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.ModConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModeCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "mode";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {

        ModConfig.RenderMode[] modes =  ModConfig.RenderMode.values();

        for (ModConfig.RenderMode mode : modes) {
            builder.then(Commands.literal(mode.name().toLowerCase())
                    .executes(c -> {
                        CoordinatesDisplay.getConfig().renderMode = mode;
                        CoordinatesDisplay.CONFIG.save();
                        return 0;
                    })
            );
        }

    }
}
