package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.boxadactle.boxlib.command.BCommandManager;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BClientSubcommand;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.hud.CoordinatesHuds;

public class ModeSubcommand implements BClientSubcommand {
    @Override
    public ArgumentBuilder<BCommandSourceStack, ?> getSubcommand() {
        return BCommandManager.literal("mode");
    }

    @Override
    public void build(ArgumentBuilder<BCommandSourceStack, ?> builder) {
        String[] modes = CoordinatesHuds.registeredOverlays.keySet().toArray(new String[0]);

        for (String mode : modes) {
            builder.then(BCommandManager.literal(mode.toLowerCase())
                    .executes(c -> {
                        CoordinatesDisplay.getConfig().renderMode = mode;
                        CoordinatesDisplay.CONFIG.save();
                        return 0;
                    })
            );
        }
    }
}
