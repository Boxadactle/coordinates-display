package dev.boxadactle.coordinatesdisplay.command;

import dev.boxadactle.boxlib.command.api.BSubcommand;
import dev.boxadactle.boxlib.command.api.subcommand.BasicSubcommand;
import dev.boxadactle.boxlib.command.api.subcommand.BooleanSubcommand;
import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.registry.StartCorner;
import net.minecraft.client.resources.language.I18n;

public class CornerSubcommand {

    public static BSubcommand create() {
        StartCorner[] corners = StartCorner.values();

        BasicSubcommand subcommand = new BasicSubcommand("corner", CoordinatesCommand::noArgs);

        for (StartCorner corner : corners) {
            subcommand.registerSubcommand(new BasicSubcommand(corner.name().toLowerCase(), (context) -> {
                CoordinatesDisplay.getConfig().startCorner = corner;
                CoordinatesDisplay.CONFIG.save();
                return 0;
            }).registerSubcommand(new BooleanSubcommand("relative", (context, bool) -> {
                if (bool) {
                    Vec2<Integer> relative = relative(corner);

                    CoordinatesDisplay.getConfig().hudX = relative.getX();
                    CoordinatesDisplay.getConfig().hudY = relative.getY();
                }

                CoordinatesDisplay.getConfig().startCorner = corner;
                CoordinatesDisplay.CONFIG.save();

                CoordinatesDisplay.LOGGER.player.info(I18n.get("button.coordinatesdisplay.startcorner", I18n.get("button.coordinatesdisplay.startcorner." + corner.name().toLowerCase())));

                return 0;
            })));
        }

        return subcommand;
    }

    static Vec2<Integer> relative(StartCorner corner) {
        return corner.getModifier().getRelativePos(
                CoordinatesDisplay.HUD.getRect(),
                new Dimension<>(
                        Math.round(ClientUtils.getClient().getWindow().getGuiScaledWidth() / CoordinatesDisplay.getConfig().hudScale),
                        Math.round(ClientUtils.getClient().getWindow().getGuiScaledHeight() / CoordinatesDisplay.getConfig().hudScale)
                )
        );
    }

}
