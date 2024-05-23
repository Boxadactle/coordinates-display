package dev.boxadactle.coordinatesdisplay.neoforge.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.hud.CoordinatesHuds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class VisibilityFilterCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "visibility";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {
        String[] filters = CoordinatesHuds.registeredVisibilityFilters.keySet().toArray(new String[0]);

        for (String filter : filters) {
            builder.then(Commands.literal(filter.toLowerCase())
                    .executes(c -> {
                        CoordinatesDisplay.getConfig().visibilityFilter = filter;
                        CoordinatesDisplay.CONFIG.save();
                        return 0;
                    })
            );
        }
    }
}
