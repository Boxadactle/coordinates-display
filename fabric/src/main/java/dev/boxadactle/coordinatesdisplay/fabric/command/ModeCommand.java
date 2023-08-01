package dev.boxadactle.coordinatesdisplay.fabric.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.ModConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.Commands;

public class ModeCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "mode";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {

        ModConfig.RenderMode[] modes =  ModConfig.RenderMode.values();

        for (ModConfig.RenderMode mode : modes) {
            builder.then(ClientCommandManager.literal(mode.name().toLowerCase())
                    .executes(c -> {
                        CoordinatesDisplay.getConfig().renderMode = mode;
                        CoordinatesDisplay.CONFIG.save();
                        return 0;
                    })
            );
        }

    }
}
