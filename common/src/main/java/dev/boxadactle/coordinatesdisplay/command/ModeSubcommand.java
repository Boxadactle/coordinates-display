package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.boxadactle.boxlib.command.BCommandManager;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BClientSubcommand;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.registry.DisplayMode;

public class ModeSubcommand implements BClientSubcommand {
    @Override
    public ArgumentBuilder<BCommandSourceStack, ?> getSubcommand() {
        return BCommandManager.literal("mode");
    }

    @Override
    public void build(ArgumentBuilder<BCommandSourceStack, ?> builder) {
        DisplayMode[] modes = DisplayMode.values();

        for (DisplayMode mode : modes) {
            builder.then(BCommandManager.literal(mode.getName().toLowerCase())
                    .executes(c -> {
                        CoordinatesDisplay.getConfig().renderMode = mode;
                        CoordinatesDisplay.CONFIG.save();
                        return 0;
                    })
            );
        }
    }
}
