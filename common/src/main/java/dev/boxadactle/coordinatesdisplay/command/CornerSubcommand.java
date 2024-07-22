package dev.boxadactle.coordinatesdisplay.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.boxadactle.boxlib.command.BCommandManager;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BClientSubcommand;
import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.registry.StartCorner;

public class CornerSubcommand implements BClientSubcommand {
    @Override
    public ArgumentBuilder<BCommandSourceStack, ?> getSubcommand() {
        return BCommandManager.literal("corner");
    }

    @Override
    public void build(ArgumentBuilder<BCommandSourceStack, ?> builder) {
        StartCorner[] corners =  StartCorner.values();

        for (StartCorner corner : corners) {
            builder.then(BCommandManager.literal(corner.name().toLowerCase())
                    .executes(c -> {
                        Vec2<Integer> relative = corner.getModifier().getRelativePos(
                                CoordinatesDisplay.HUD.getRect(),
                                new Dimension<>(
                                        Math.round(ClientUtils.getClient().getWindow().getGuiScaledWidth() / CoordinatesDisplay.getConfig().hudScale),
                                        Math.round(ClientUtils.getClient().getWindow().getGuiScaledHeight() / CoordinatesDisplay.getConfig().hudScale)
                                )
                        );

                        CoordinatesDisplay.getConfig().hudX = relative.getX();
                        CoordinatesDisplay.getConfig().hudY = relative.getY();
                        CoordinatesDisplay.getConfig().startCorner = corner;
                        CoordinatesDisplay.CONFIG.save();
                        return 0;
                    })
            );
        }
    }
}