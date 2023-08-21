package dev.boxadactle.coordinatesdisplay.fabric.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class CornerCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "corner";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        ModConfig.StartCorner[] corners =  ModConfig.StartCorner.values();

        for (ModConfig.StartCorner corner : corners) {
            builder.then(ClientCommandManager.literal(corner.name().toLowerCase())
                    .executes(c -> {
                        CoordinatesDisplay.getConfig().startCorner = corner;
                        CoordinatesDisplay.CONFIG.save();
                        return 0;
                    })
            );
        }
    }
}
