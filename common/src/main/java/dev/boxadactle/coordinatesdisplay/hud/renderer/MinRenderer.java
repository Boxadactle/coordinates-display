package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.layouts.component.LayoutContainerComponent;
import dev.boxadactle.boxlib.layouts.component.LeftParagraphComponent;
import dev.boxadactle.boxlib.layouts.component.ParagraphComponent;
import dev.boxadactle.boxlib.layouts.component.TextComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.DisplayMode;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.levelgen.Column;
import oshi.util.tuples.Triplet;

@DisplayMode(
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
    public Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos) {
        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);

        RowLayout layout = new RowLayout(0, 0, config().textPadding);

        ColumnLayout row = new ColumnLayout(0, 0, config().textPadding / 2);

        { // xyz
            Component xtext = createLine("x", player.getA());
            Component ytext = createLine("y", player.getB());
            Component ztext = createLine("z", player.getC());

            ParagraphComponent paragraph = new ParagraphComponent(1, xtext, ytext, ztext);
            row.addComponent(paragraph);
        }

        // biome
        if (config().renderBiome) {
            String biomestring = pos.world.getBiome(true);
            Component biome = definition(
                    "biome",
                    GuiUtils.colorize(
                            Component.literal(biomestring),
                            CoordinatesDisplay.CONFIG.get().biomeColors ?
                                    CoordinatesDisplay.BiomeColors.getBiomeColor(biomestring, CoordinatesDisplay.CONFIG.get().dataColor) :
                                    CoordinatesDisplay.CONFIG.get().dataColor
                    )
            );

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

        return renderHud(guiGraphics, new PaddingLayout(x, y, config().padding, layout));
    }
}
