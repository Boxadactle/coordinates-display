package dev.boxadactle.coordinatesdisplay.fabric.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.hud.CoordinatesHuds;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class VisibilityFilterCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "visibility";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        String[] filters = CoordinatesHuds.registeredVisibilityFilters.keySet().toArray(new String[0]);

        for (String filter : filters) {
            builder.then(ClientCommandManager.literal(filter.toLowerCase())
                    .executes(c -> {
                        CoordinatesDisplay.getConfig().visibilityFilter = filter;
                        CoordinatesDisplay.CONFIG.save();
                        return 0;
                    })
            );
        }
    }
}
