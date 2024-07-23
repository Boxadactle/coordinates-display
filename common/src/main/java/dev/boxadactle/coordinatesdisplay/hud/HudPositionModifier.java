package dev.boxadactle.coordinatesdisplay.hud;

import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;

public interface HudPositionModifier {

    Vec2<Integer> translateVector(Vec2<Integer> original, Dimension<Integer> window);

    Rect<Integer> translateRect(Rect<Integer> rect, Dimension<Integer> window);

    Vec2<Integer> getRelativeVec(Vec2<Integer> leftTop, Dimension<Integer> window);

    Vec2<Integer> getStartCorner(Rect<Integer> rect);

    default Vec2<Integer> getRelativePos(Rect<Integer> rect, Dimension<Integer> window) {
        return getRelativeVec(getStartCorner(rect), window);
    }

    @FunctionalInterface
    interface BasicPositionModifier {
        Rect<Integer> getPosition(Rect<Integer> rect, Dimension<Integer> ignored, Hud.RenderType type);
    }

    class Basic implements BasicPositionModifier {

        @Override
        public Rect<Integer> getPosition(Rect<Integer> rect, Dimension<Integer> ignored, Hud.RenderType ignored2) {
            return rect;
        }
    }
}
