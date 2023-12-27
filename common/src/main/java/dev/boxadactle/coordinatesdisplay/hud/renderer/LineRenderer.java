package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.math.mathutils.NumberFormatter;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import oshi.util.tuples.Triplet;

public class LineRenderer implements HudRenderer {

    @Override
    public String getTranslationKey() {
        return "hud.coordinatesdisplay.line.";
    }

    private int calculateWidth(Component line, int p) {
        int a = GuiUtils.getTextRenderer().width(line);

        return config().renderBackground ? p * 2 + a : a;
    }

    private int calculateHeight(int th, int p) {
        return config().renderBackground ? p * 2 + th : th;
    }

    @Override
    public Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos) {
        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);

        Component xtext = definition("x", value(player.getA()));
        Component ytext = definition("y", value(player.getB()));
        Component ztext = definition("z",value(player.getC()));

        Component direction = definition("direction", valueTranslation(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw())));

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
