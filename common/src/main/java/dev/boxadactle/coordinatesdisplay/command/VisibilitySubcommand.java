package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.boxadactle.boxlib.command.BCommandManager;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BClientSubcommand;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.registry.VisibilityFilter;

public class VisibilitySubcommand implements BClientSubcommand {
    @Override
    public ArgumentBuilder<BCommandSourceStack, ?> getSubcommand() {
        return BCommandManager.literal("visibility");
    }

    @Override
    public void build(ArgumentBuilder<BCommandSourceStack, ?> builder) {
        VisibilityFilter[] modes = VisibilityFilter.values();

        for (VisibilityFilter mode : modes) {
            builder.then(BCommandManager.literal(mode.getId())
                    .executes(c -> {
                        CoordinatesDisplay.getConfig().visibilityFilter = mode;
                        CoordinatesDisplay.CONFIG.save();
                        return 0;
                    })
            );
        }
    }
}