package dev.boxadactle.coordinatesdisplay.util.hud;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.HudRenderer;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import dev.boxadactle.boxlib.math.Rect;
import dev.boxadactle.boxlib.math.Vec3;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.text.DecimalFormat;

public class LineRenderer extends HudRenderer.Renderer {

    public LineRenderer() {
        super("hud.coordinatesdisplay.line.");
    }

    private int calculateWidth(Text line, int p) {
        int a = GuiUtils.getTextRenderer().getWidth(line);

        return config().renderBackground ? p * 2 + a : a;
    }

    private int calculateHeight(int th, int p) {
        return config().renderBackground ? p * 2 + th : th;
    }

    @Override
    protected Rect<Integer> renderOverlay(DrawContext drawContext, int x, int y, Position pos) {

        Vec3<Double> vec = pos.position.getPlayerPos();
        DecimalFormat decimalFormat = new DecimalFormat(CoordinatesDisplay.CONFIG.get().decimalRounding ? "0.00" : "0");

        Text xtext = GuiUtils.colorize(translation(
                "x",
                GuiUtils.colorize(
                        Text.literal(decimalFormat.format(vec.getX())),
                        config().dataColor
                )
        ), config().definitionColor);
        Text ytext = GuiUtils.colorize(translation(
                "y",
                GuiUtils.colorize(
                        Text.literal(decimalFormat.format(vec.getY())),
                        config().dataColor
                )
        ), config().definitionColor);
        Text ztext = GuiUtils.colorize(translation(
                "z",
                GuiUtils.colorize(
                        Text.literal(decimalFormat.format(vec.getZ())),
                        config().dataColor
                )
        ), config().definitionColor);

        Text direction = GuiUtils.colorize(translation("direction", GuiUtils.colorize(
                translation(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw())),
                config().dataColor
        )), config().definitionColor);

        Text a = next(next(xtext, ytext), ztext);
        if (config().renderDirection) a = next(a, direction);

        int p = 2;

        int w = calculateWidth(a, p);
        int h = calculateHeight(GuiUtils.getTextRenderer().fontHeight, p);

        if (config().renderBackground) {
            RenderUtils.drawSquare(drawContext, x, y, w, h, config().backgroundColor);
            drawInfo(drawContext, a, x + p, y + p, 0xFFFFFF);
        } else {
            drawInfo(drawContext, a, x, y, 0xFFFFFF);
        }

        return new Rect<>(x, y, w, h);
    }

    private Text next(Text t1, Text t2) {
        return addTrailingSpace(t1).copy().append(t2);
    }

    private Text addTrailingSpace(Text input) {
        return input.copy().append(" ");
    }
}
