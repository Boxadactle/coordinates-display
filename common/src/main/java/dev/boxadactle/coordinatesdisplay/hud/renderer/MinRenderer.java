package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.math.mathutils.NumberFormatter;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;

public class MinRenderer extends HudRenderer {

    public MinRenderer() {
        super("hud.coordinatesdisplay.min.");
    }

    private int calculateWidth(int p, int th, int dpadding, Component xtext, Component ytext, Component ztext, Component biome) {
        int a = GuiUtils.getLongestLength(xtext, ytext, ztext, biome);
        int b = GuiUtils.getTextRenderer().width("NW");

        return p + a + dpadding + b + p;
    }

    private int calculateHeight(int p, int th) {
        // this might become a real method later
        return p + (th * 4) + p;
    }

    @Override
    protected Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos) {
        NumberFormatter<Double> formatter = new NumberFormatter<>(CoordinatesDisplay.CONFIG.get().decimalPlaces);
        Vec3<Double> player = pos.position.getPlayerPos();

        Component xtext = GuiUtils.colorize(translation(
                "x",
                GuiUtils.colorize(
                        Component.literal(formatter.formatDecimal(player.getX())),
                        config().dataColor
                )
        ), config().definitionColor);

        Component ytext = GuiUtils.colorize(translation(
                "y",
                GuiUtils.colorize(
                        Component.literal(formatter.formatDecimal(player.getY())),
                        config().dataColor
                )
        ), config().definitionColor);

        Component ztext = GuiUtils.colorize(translation(
                "z",
                GuiUtils.colorize(
                        Component.literal(formatter.formatDecimal(player.getZ())),
                        config().dataColor
                )
        ), config().definitionColor);


        String biomestring = pos.world.getBiome(true);
        Component biome = GuiUtils.colorize(translation(
                "biome",
                GuiUtils.colorize(
                        Component.literal(biomestring),
                        CoordinatesDisplay.CONFIG.get().biomeColors ?
                                CoordinatesDisplay.BiomeColors.getBiomeColor(biomestring, CoordinatesDisplay.CONFIG.get().dataColor) :
                                CoordinatesDisplay.CONFIG.get().dataColor
                )
        ), config().definitionColor);

        int p = CoordinatesDisplay.CONFIG.get().padding;
        int th = GuiUtils.getTextRenderer().lineHeight;
        int tp = CoordinatesDisplay.CONFIG.get().textPadding;

        double yaw = pos.headRot.wrapYaw();
        double pitch = pos.headRot.wrapPitch();
        Component direction = translation(ModUtil.getDirectionFromYaw(yaw));
        Component pitchComponent = Component.literal(pitch > 0 ? "+" : "-");
        Component directionComponent = Component.translatable("hud.coordinatesdisplay.min." + ModUtil.getDirectionFromYaw(yaw), direction);
        Component yawComponent = Component.literal(yaw < 0 ? "+" : "-");



        int w = calculateWidth(p, th, tp, xtext, ytext, ztext, biome);
        int h = calculateHeight(p, th);

        // rendering
        if (config().renderBackground) {
            RenderUtils.drawSquare(guiGraphics, x, y, w, h, CoordinatesDisplay.CONFIG.get().backgroundColor);
        }

        drawInfo(guiGraphics, xtext, x + p, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(guiGraphics, ytext, x + p, y + p + th, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(guiGraphics, ztext, x + p, y + p + (th * 2), CoordinatesDisplay.CONFIG.get().definitionColor);

        drawInfo(guiGraphics, biome, x + p, y + p + (th * 3), CoordinatesDisplay.CONFIG.get().definitionColor);
        {
            int dstart = (x + w) - p - GuiUtils.getTextRenderer().width(directionComponent);
            int ypstart = (x + w) - p - GuiUtils.getTextRenderer().width(yawComponent);

            drawInfo(guiGraphics, pitchComponent, ypstart, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
            drawInfo(guiGraphics, directionComponent, dstart, y + p + th, CoordinatesDisplay.CONFIG.get().dataColor);
            drawInfo(guiGraphics, yawComponent, ypstart, y + p + (th * 2), CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        return new Rect<>(x, y, w, h);
    }
}
