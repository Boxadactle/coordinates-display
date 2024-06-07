package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.command.BCommandManager;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BClientSubcommand;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.Minecraft;

public class PositionSubcommand implements BClientSubcommand {
    @Override
    public ArgumentBuilder<BCommandSourceStack, ?> getSubcommand() {
        return BCommandManager.literal("position");
    }

    @Override
    public void build(ArgumentBuilder<BCommandSourceStack, ?> builder) {
        // send in chat
        builder.then(BCommandManager.literal("chat")
                .executes(this::sendPosInChat)
        );

        // copy to clipboard
        builder.then(BCommandManager.literal("copy")
                .executes(this::copyPos)
        );

        // copy as tp
        builder.then(BCommandManager.literal("copytp")
                .executes(this::copyPosTp)
        );
    }

    private int sendPosInChat(CommandContext<BCommandSourceStack> ignored) {

        Position pos = Position.of(WorldUtils.getPlayer());

        CoordinatesDisplay.LOGGER.player.publicChat(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().posChatMessage, pos));
        CoordinatesDisplay.LOGGER.info("Sent position as chat message");

        return 0;
    }

    private int copyPos(CommandContext<BCommandSourceStack> context) {
        Position pos = Position.of(WorldUtils.getPlayer());

        Minecraft.getInstance().keyboardHandler.setClipboard(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().copyPosMessage, pos));
        CoordinatesDisplay.LOGGER.player.info(GuiUtils.getTranslatable("command.coordinatesdisplay.position.copy"));
        CoordinatesDisplay.LOGGER.info("Copied location to clipboard");

        return 0;
    }

    private int copyPosTp(CommandContext<BCommandSourceStack> context) {
        try {
            Position pos = Position.of(WorldUtils.getPlayer());

            Minecraft.getInstance().keyboardHandler.setClipboard(CoordinatesDisplay.getConfig().teleportMode.toCommand(pos));

            CoordinatesDisplay.LOGGER.player.info(GuiUtils.getTranslatable("command.coordinatesdisplay.position.copytp"));

            return 0;
        } catch (Exception ignored) {
            CoordinatesDisplay.LOGGER.player.info(GuiUtils.getTranslatable("command.coordinatesdisplay.internalError"));
            return 1;
        }
    }

}
