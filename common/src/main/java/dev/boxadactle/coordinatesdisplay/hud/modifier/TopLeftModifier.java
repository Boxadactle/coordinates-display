package dev.boxadactle.coordinatesdisplay.hud.modifier;

import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.coordinatesdisplay.registry.StartCorner;
import dev.boxadactle.coordinatesdisplay.hud.HudPositionModifier;

public class TopLeftModifier implements HudPositionModifier {
    @Override
    public Vec2<Integer> translateVector(Vec2<Integer> original, Dimension<Integer> window) {
        return original;
    }

    @Override
    public Rect<Integer> translateRect(Rect<Integer> rect, Dimension<Integer> window) {
        return rect;
    }

    @Override
    public Vec2<Integer> getRelativeVec(Vec2<Integer> leftTop, Dimension<Integer> window) {
        return leftTop;
    }

    @Override
    public Vec2<Integer> getStartCorner(Rect<Integer> rect) {
        return new Vec2<>(rect.getX(), rect.getY());
    }

}
