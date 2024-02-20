package dev.boxadactle.coordinatesdisplay.fabric.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.hud.CoordinatesHuds;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ModeCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "mode";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {

        String[] modes = CoordinatesHuds.registeredOverlays.keySet().toArray(new String[0]);

        for (String mode : modes) {
            builder.then(ClientCommandManager.literal(mode.toLowerCase())
                    .executes(c -> {
                        CoordinatesDisplay.getConfig().renderMode = mode;
                        CoordinatesDisplay.CONFIG.save();
                        return 0;
                    })
            );
        }

    }
}
