package dev.boxadactle.coordinatesdisplay.hud.modifier;

import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.coordinatesdisplay.hud.HudPositionModifier;

public class BottomModifier implements HudPositionModifier {
    @Override
    public Vec2<Integer> translateVector(Vec2<Integer> original, Dimension<Integer> window) {
        int x = original.getX();
        int y = original.getY();

        int windowWidth = window.getWidth();
        int windowHeight = window.getHeight();

        return new Vec2<>(windowWidth / 2 + x, windowHeight - y);
    }

    @Override
    public Rect<Integer> translateRect(Rect<Integer> rect, Dimension<Integer> window) {
        int x = rect.getX();
        int y = rect.getY();
        int width = rect.getWidth();
        int height = rect.getHeight();

        int windowWidth = window.getWidth();
        int windowHeight = window.getHeight();

        Rect<Integer> r = rect.clone();
        r.setX(windowWidth / 2 + x - width / 2);
        r.setY(windowHeight - y - height);
        return r;
    }

    @Override
    public Vec2<Integer> getRelativeVec(Vec2<Integer> leftTop, Dimension<Integer> window) {
        return new Vec2<>(
                leftTop.getX() - window.getWidth() / 2,
                window.getHeight() - leftTop.getY()
        );
    }

    @Override
    public Vec2<Integer> getStartCorner(Rect<Integer> rect) {
        return new Vec2<>(rect.getX() + rect.getWidth() / 2, rect.getMaxY());
    }
}
