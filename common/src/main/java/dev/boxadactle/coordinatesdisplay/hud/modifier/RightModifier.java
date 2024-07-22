package dev.boxadactle.coordinatesdisplay.hud.modifier;

import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.coordinatesdisplay.hud.HudPositionModifier;

public class RightModifier implements HudPositionModifier {
    @Override
    public Vec2<Integer> translateVector(Vec2<Integer> original, Dimension<Integer> window) {
        int x = original.getX();
        int y = original.getY();

        int windowWidth = window.getWidth();
        int windowHeight = window.getHeight();

        return new Vec2<>(windowWidth - x, windowHeight / 2 + y);
    }

    @Override
    public Rect<Integer> translateRect(Rect<Integer> rect, Dimension<Integer> window) {
        int x = rect.getX();
        int width = rect.getWidth();
        int y = rect.getY();
        int height = rect.getHeight();

        int windowWidth = window.getWidth();
        int windowHeight = window.getHeight();

        Rect<Integer> r = rect.clone();
        r.setX(windowWidth - x - width);
        r.setY(windowHeight / 2 + y - height / 2);
        return r;
    }

    @Override
    public Vec2<Integer> getRelativeVec(Vec2<Integer> leftTop, Dimension<Integer> window) {
        return new Vec2<>(
                window.getWidth() - leftTop.getX(),
                leftTop.getY() - window.getHeight() / 2
        );
    }

    @Override
    public Vec2<Integer> getStartCorner(Rect<Integer> rect) {
        return new Vec2<>(rect.getMaxX(), rect.getY() + rect.getHeight() / 2);
    }
}
