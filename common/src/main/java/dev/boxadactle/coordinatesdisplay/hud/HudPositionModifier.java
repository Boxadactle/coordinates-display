package dev.boxadactle.coordinatesdisplay.hud;

import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.coordinatesdisplay.config.StartCorner;

public interface HudPositionModifier {

    Vec2<Integer> translateVector(Vec2<Integer> original, Dimension<Integer> window, StartCorner currentCorner);

    Rect<Integer> translateRect(Rect<Integer> rect, Dimension<Integer> window, StartCorner currentCorner);

}
