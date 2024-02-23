package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.RendererMetadata;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import oshi.util.tuples.Triplet;

@RendererMetadata(
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

    @Override
    public Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos) {
        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);

        Component xtext = definition(
                "x",
                value(player.getA())
        );

        Component ytext = definition(
                "y",
                value(player.getB())
        );

        Component ztext = definition(
                "z",
                value(player.getC())
        );


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

        double yaw = pos.headRot.wrapYaw();
        double pitch = pos.headRot.wrapPitch();
        Component directionComponent = resolveDirection(ModUtil.getDirectionFromYaw(yaw), true);
        Component pitchComponent = Component.literal(pitch > 0 ? "+" : "-");
        Component yawComponent = Component.literal(yaw > 0 ? "+" : "-");


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
            int dstart = (x + w) - p - GuiUtils.getTextRenderer().width(directionComponent);
            int ypstart = (x + w) - p - GuiUtils.getTextRenderer().width(yawComponent);

            drawInfo(guiGraphics, pitchComponent, ypstart, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
            drawInfo(guiGraphics, directionComponent, dstart, y + p + th, CoordinatesDisplay.CONFIG.get().dataColor);
            drawInfo(guiGraphics, yawComponent, ypstart, y + p + (th * 2), CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        return new Rect<>(x, y, w, h);
    }
}
