package dev.boxadactle.coordinatesdisplay.hud.renderer;

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

    private int calculateWidth(int p, int dpadding, Component xtext, Component ytext, Component ztext, Component biome) {
        int a = GuiUtils.getLongestLength(xtext, ytext, ztext, (config().renderBiome ? biome : Component.empty()));
        int b = GuiUtils.getTextRenderer().width("NW");

        return p + a + (config().renderDirection ? dpadding + b : 0) + p;
    }

    private int calculateHeight(int p, int th) {
        return p + (th * 3) + (config().renderBiome ? th : 0) + p;
    }

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

        Component xtext = createLine("x", player.getA());

        Component ytext = createLine("y", player.getB());

        Component ztext = createLine("z", player.getC());


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

        int p = CoordinatesDisplay.CONFIG.get().padding;
        int th = GuiUtils.getTextRenderer().lineHeight;
        int tp = CoordinatesDisplay.CONFIG.get().textPadding;

        Component[] directionTexts = createDirectionComponents(pos.headRot.wrapYaw());
        Component xDirection = directionTexts[0];
        Component directionText = directionTexts[1];
        Component zDirection = directionTexts[2];


        int w = calculateWidth(p, tp, xtext, ytext, ztext, biome);
        int h = calculateHeight(p, th);

        // rendering
        if (config().renderBackground) {
            RenderUtils.drawSquare(guiGraphics, x, y, w, h, CoordinatesDisplay.CONFIG.get().backgroundColor);
        }

        drawInfo(guiGraphics, xtext, x + p, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(guiGraphics, ytext, x + p, y + p + th, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(guiGraphics, ztext, x + p, y + p + (th * 2), CoordinatesDisplay.CONFIG.get().definitionColor);

        if (config().renderBiome) {
            drawInfo(guiGraphics, biome, x + p, y + p + (th * 3), CoordinatesDisplay.CONFIG.get().definitionColor);
        }
        if (config().renderDirection) {
            int dstart = (x + w) - p - GuiUtils.getTextRenderer().width(directionText);
            int ypstart = (x + w) - p - GuiUtils.getLongestLength(xDirection, zDirection);

            drawInfo(guiGraphics, xDirection, ypstart, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
            drawInfo(guiGraphics, directionText, dstart, y + p + th, CoordinatesDisplay.CONFIG.get().dataColor);
            drawInfo(guiGraphics, zDirection, ypstart, y + p + (th * 2), CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        return new Rect<>(x, y, w, h);
    }
}
