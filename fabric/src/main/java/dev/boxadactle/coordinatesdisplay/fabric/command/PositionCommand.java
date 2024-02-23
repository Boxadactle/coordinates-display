package dev.boxadactle.coordinatesdisplay.fabric.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;

public class PositionCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "position";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {

        // send in chat
        builder.then(ClientCommandManager.literal("chat")
                .executes(this::sendPosInChat)
        );

        // copy to clipboard
        builder.then(ClientCommandManager.literal("copy")
                .executes(this::copyPos)
        );

        // copy as tp
        builder.then(ClientCommandManager.literal("copytp")
                .executes(this::copyPosTp)
        );

    }

    private int sendPosInChat(CommandContext<FabricClientCommandSource> context) {

        Position pos = Position.of(WorldUtils.getCamera());

        CoordinatesDisplay.LOGGER.player.publicChat(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().posChatMessage, pos));
        CoordinatesDisplay.LOGGER.info("Sent position as chat message");

        return 0;
    }

    private int copyPos(CommandContext<FabricClientCommandSource> context) {

        Position pos = Position.of(WorldUtils.getCamera());

        Minecraft.getInstance().keyboardHandler.setClipboard(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().copyPosMessage, pos));
        CoordinatesDisplay.LOGGER.player.info(super.translatable("command.coordinatesdisplay.position.copy"));
        CoordinatesDisplay.LOGGER.info("Copied location to clipboard");

        return 0;

    }

    private int copyPosTp(CommandContext<FabricClientCommandSource> context) {

        try {
            Position pos = Position.of(WorldUtils.getCamera());

            Minecraft.getInstance().keyboardHandler.setClipboard(CoordinatesDisplay.getConfig().teleportMode.toCommand(pos));

            CoordinatesDisplay.LOGGER.player.info(super.translatable("command.coordinatesdisplay.position.copytp"));

            return 0;
        } catch (Exception ignored) {
            CoordinatesDisplay.LOGGER.player.info(super.translatable("command.coordinatesdisplay.internalError"));
            return 1;
        }

    }

}
