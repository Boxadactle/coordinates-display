package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class PositionCommand extends CoordinatesCommand {
    @Override
    public String getName() {
        return "position";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {

        // send in chat
        builder.then(Commands.literal("chat")
                .executes(this::sendPosInChat)
        );

        // copy to clipboard
        builder.then(Commands.literal("copy")
                .executes(this::copyPos)
        );

        // copy as tp
        builder.then(Commands.literal("copytp")
                .executes(this::copyPosTp)
        );

    }

    private int sendPosInChat(CommandContext<CommandSourceStack> context) {

        Position pos = Position.of(WorldUtils.getCamera());

        CoordinatesDisplay.LOGGER.player.publicChat(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().posChatMessage, pos));
        CoordinatesDisplay.LOGGER.info("Sent position as chat message");

        return 0;
    }

    private int copyPos(CommandContext<CommandSourceStack> context) {

        Position pos = Position.of(WorldUtils.getCamera());

        Minecraft.getInstance().keyboardHandler.setClipboard(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().copyPosMessage, pos));
        CoordinatesDisplay.LOGGER.player.info(super.translatable("command.coordinatesdisplay.position.copy"));
        CoordinatesDisplay.LOGGER.info("Copied location to clipboard");

        return 0;

    }

    private int copyPosTp(CommandContext<CommandSourceStack> context) {

        try {
            Position pos = Position.of(WorldUtils.getCamera());

            Minecraft.getInstance().keyboardHandler.setClipboard(ModUtil.toTeleportCommand(pos.getPlayerVector(), WorldUtils.getCurrentDimension()));

            CoordinatesDisplay.LOGGER.player.info(super.translatable("command.coordinatesdisplay.position.copytp"));

            return 0;
        } catch (Exception ignored) {
            CoordinatesDisplay.LOGGER.player.info(super.translatable("command.coordinatesdisplay.internalError"));
            return 1;
        }

    }

}
