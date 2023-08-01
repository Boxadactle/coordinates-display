package dev.boxadactle.coordinatesdisplay.util.hud;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.math.Rect;
import dev.boxadactle.boxlib.math.Vec3;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.util.HudRenderer;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;

public class LineRenderer extends HudRenderer.Renderer {

    public LineRenderer() {
        super("hud.coordinatesdisplay.line.");
    }

    private int calculateWidth(Component line, int p) {
        int a = GuiUtils.getTextRenderer().width(line);

        return config().renderBackground ? p * 2 + a : a;
    }

    private int calculateHeight(int th, int p) {
        return config().renderBackground ? p * 2 + th : th;
    }

    @Override
    protected Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos) {

        Vec3<Double> vec = pos.position.getPlayerPos();
        DecimalFormat decimalFormat = new DecimalFormat(CoordinatesDisplay.CONFIG.get().decimalRounding ? "0.00" : "0");

        Component xtext = GuiUtils.colorize(translation(
                "x",
                GuiUtils.colorize(
                        Component.literal(decimalFormat.format(vec.getX())),
                        config().dataColor
                )
        ), config().definitionColor);
        Component ytext = GuiUtils.colorize(translation(
                "y",
                GuiUtils.colorize(
                        Component.literal(decimalFormat.format(vec.getY())),
                        config().dataColor
                )
        ), config().definitionColor);
        Component ztext = GuiUtils.colorize(translation(
                "z",
                GuiUtils.colorize(
                        Component.literal(decimalFormat.format(vec.getZ())),
                        config().dataColor
                )
        ), config().definitionColor);

        Component direction = GuiUtils.colorize(translation("direction", GuiUtils.colorize(
                translation(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw())),
                config().dataColor
        )), config().definitionColor);

        Component a = next(next(xtext, ytext), ztext);
        if (config().renderDirection) a = next(a, direction);

        int p = 2;

        int w = calculateWidth(a, p);
        int h = calculateHeight(GuiUtils.getTextRenderer().lineHeight, p);

        if (config().renderBackground) {
            RenderUtils.drawSquare(guiGraphics, x, y, w, h, config().backgroundColor);
            drawInfo(guiGraphics, a, x + p, y + p, 0xFFFFFF);
        } else {
            drawInfo(guiGraphics, a, x, y, 0xFFFFFF);
        }

        return new Rect<>(x, y, w, h);
    }

    private Component next(Component t1, Component t2) {
        return addTrailingSpace(t1).copy().append(t2);
    }

    private Component addTrailingSpace(Component input) {
        return input.copy().append(" ");
    }
}
