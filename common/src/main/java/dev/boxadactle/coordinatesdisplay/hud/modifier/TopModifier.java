package dev.boxadactle.coordinatesdisplay.hud.modifier;

import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.coordinatesdisplay.hud.HudPositionModifier;

public class TopModifier implements HudPositionModifier {
    @Override
    public Vec2<Integer> translateVector(Vec2<Integer> original, Dimension<Integer> window) {
        int x = original.getX();
        int y = original.getY();

        int windowWidth = window.getWidth();

        return new Vec2<>(windowWidth / 2 + x, y);
    }

    @Override
    public Rect<Integer> translateRect(Rect<Integer> rect, Dimension<Integer> window) {
        int x = rect.getX();
        int width = rect.getWidth();

        int windowWidth = window.getWidth();

        Rect<Integer> r = rect.clone();
        r.setX(windowWidth / 2 + x - width / 2);
        return r;
    }

    @Override
    public Vec2<Integer> getRelativeVec(Vec2<Integer> leftTop, Dimension<Integer> window) {
        return new Vec2<>(
                leftTop.getX() - window.getWidth() / 2,
                leftTop.getY()
        );
    }

    @Override
    public Vec2<Integer> getStartCorner(Rect<Integer> rect) {
        return new Vec2<>(rect.getX() + rect.getWidth() / 2, rect.getY());
    }
}
