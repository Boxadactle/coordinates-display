package dev.boxadactle.coordinatesdisplay.fabric.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ToggleCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "toggle";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {

        builder.executes(this::toggleHud);

    }

    private int toggleHud(CommandContext<FabricClientCommandSource> context) {
        CoordinatesDisplay.LOGGER.info("Toggling Hud");

        CoordinatesDisplay.CONFIG.get().visible = !CoordinatesDisplay.CONFIG.get().visible;
        CoordinatesDisplay.CONFIG.save();

        CoordinatesDisplay.LOGGER.player.info(super.translatable("command.coordinatesdisplay.togglemessage"));

        return 0;
    }
}
