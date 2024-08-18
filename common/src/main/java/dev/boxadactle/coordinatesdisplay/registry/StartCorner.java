package dev.boxadactle.coordinatesdisplay.registry;

import dev.boxadactle.boxlib.core.BoxLib;
import dev.boxadactle.coordinatesdisplay.hud.HudPositionModifier;
import dev.boxadactle.coordinatesdisplay.hud.modifier.*;

public enum StartCorner {
    TOP_LEFT(TopLeftModifier.class),
    TOP_RIGHT(TopRightModifier.class),
    BOTTOM_LEFT(BottomLeftModifier.class),
    BOTTOM_RIGHT(BottomRightModifier.class),
    TOP(TopModifier.class),
    LEFT(LeftModifier.class),
    RIGHT(RightModifier.class),
    BOTTOM(BottomModifier.class);

    final HudPositionModifier modifier;

    StartCorner(Class<? extends HudPositionModifier> modifier) {
        this.modifier = BoxLib.initializeClass(modifier);
    }

    public HudPositionModifier getModifier() {
        return modifier;
    }
}
