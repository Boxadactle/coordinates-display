package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.layouts.component.LayoutContainerComponent;
import dev.boxadactle.boxlib.layouts.component.LeftParagraphComponent;
import dev.boxadactle.boxlib.layouts.component.ParagraphComponent;
import dev.boxadactle.boxlib.layouts.component.TextComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.HudDisplayMode;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.network.chat.Component;
import oshi.util.tuples.Triplet;

@HudDisplayMode(
        value = "minimum",
        hasXYZ = false,
        hasChunkData = false,
        hasDirectionInt = false,
        hasMCVersion = false,
        hasDimension = false
)
public class MinRenderer implements HudRenderer {

    private Component[] createDirectionComponents(double yaw) {
        // compiled using the debug screen
        String[][] directions = {
                // X   Z
                { " ", "+" },
                { "-", "+" },
                { "-", " " },
                { "-", "-" },
                { " ", "-" },
                { "+", "-" },
                { "+", " " },
                { "+", "+" }
        };

        String[] direction = directions[(int) Math.round(yaw / 45.0F) & 7];

        return new Component[] {
                Component.literal(direction[0]),
                resolveDirection(ModUtil.getDirectionFromYaw(yaw), true),
                Component.literal(direction[1])
        };
    }

    @Override
    public RenderingLayout renderOverlay(int x, int y, Position pos) {
        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);

        RowLayout layout = new RowLayout(0, 0, config().textPadding);

        ColumnLayout row = new ColumnLayout(0, 0, config().textPadding / 2);

        { // xyz
            Component xtext = definition(GlobalTexts.X, value(player.getA()));
            Component ytext = definition(GlobalTexts.Y, value(player.getB()));
            Component ztext = definition(GlobalTexts.Z, value(player.getC()));

            ParagraphComponent paragraph = new ParagraphComponent(1, xtext, ytext, ztext);
            row.addComponent(paragraph);
        }

        // biome
        if (config().renderBiome) {
            Component biome = ModUtil.getBiomeComponent(pos.world.getBiomeKey(), pos.world.getBiome(), config().biomeColors, config().definitionColor);

            row.addComponent(new TextComponent(biome));
        }

        layout.addComponent(new LayoutContainerComponent(row));

        // direction
        if (config().renderDirection) {
            Component[] directionTexts = createDirectionComponents(pos.headRot.wrapYaw());
            Component xDirection = definition(directionTexts[0]);
            Component directionText = value(directionTexts[1]);
            Component zDirection = definition(directionTexts[2]);

            layout.addComponent(new LeftParagraphComponent(1, xDirection, directionText, zDirection));
        }

        return new PaddingLayout(x, y, config().padding, layout);
    }
}
