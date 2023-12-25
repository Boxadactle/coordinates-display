package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.math.mathutils.NumberFormatter;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.hud.HudTextHelper;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class LineRenderer extends HudTextHelper implements HudRenderer {

    private int calculateWidth(Component line, int p) {
        int a = GuiUtils.getTextRenderer().width(line);

        return config().renderBackground ? p * 2 + a : a;
    }

    private int calculateHeight(int th, int p) {
        return config().renderBackground ? p * 2 + th : th;
    }

    @Override
    public Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos) {

        Vec3<Double> vec = pos.position.getPlayerPos();
        NumberFormatter<Double> formatter = new NumberFormatter<>(CoordinatesDisplay.CONFIG.get().decimalPlaces);

        Component xtext = GuiUtils.colorize(translation(
                "x",
                GuiUtils.colorize(
                        Component.literal(formatter.formatDecimal(vec.getX())),
                        config().dataColor
                )
        ), config().definitionColor);
        Component ytext = GuiUtils.colorize(translation(
                "y",
                GuiUtils.colorize(
                        Component.literal(formatter.formatDecimal(vec.getY())),
                        config().dataColor
                )
        ), config().definitionColor);
        Component ztext = GuiUtils.colorize(translation(
                "z",
                GuiUtils.colorize(
                        Component.literal(formatter.formatDecimal(vec.getZ())),
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

    @Override
    protected String getKey() {
        return "hud.coordinatesdisplay.line.";
    }
}
