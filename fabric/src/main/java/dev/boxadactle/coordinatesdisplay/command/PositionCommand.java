package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

import static dev.boxadactle.coordinatesdisplay.CoordinatesDisplay.LOGGER;

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

        LOGGER.player.publicChat(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().posChatMessage, Position.of(WorldUtils.getCamera())));
        LOGGER.info("Sent position as chat message");

        return 0;
    }

    private int copyPos(CommandContext<FabricClientCommandSource> context) {

        MinecraftClient.getInstance().keyboard.setClipboard(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().copyPosMessage, Position.of(WorldUtils.getCamera())));
        LOGGER.player.info(super.translatable("command.coordinatesdisplay.position.copy"));
        LOGGER.info("Copied location to clipboard");

        return 0;

    }

    private int copyPosTp(CommandContext<FabricClientCommandSource> context) {

        try {
            Position pos = Position.of(MinecraftClient.getInstance().getCameraEntity());

            RegistryKey<World> registry = MinecraftClient.getInstance().player.clientWorld.getRegistryKey();

            MinecraftClient.getInstance().keyboard.setClipboard(ModUtil.toTeleportCommand(pos.getPlayerVector(), (registry != null ? registry.getValue().toString() : null)));

            LOGGER.player.info(super.translatable("command.coordinatesdisplay.position.copytp"));

            return 0;
        } catch (Exception ignored) {
            LOGGER.player.info(super.translatable("command.coordinatesdisplay.internalError"));
            return 1;
        }

    }
}
