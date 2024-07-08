package dev.boxadactle.coordinatesdisplay.config;

import dev.boxadactle.boxlib.core.BoxLib;
import dev.boxadactle.coordinatesdisplay.hud.HudPositionModifier;
import dev.boxadactle.coordinatesdisplay.hud.modifier.BottomLeftModifier;
import dev.boxadactle.coordinatesdisplay.hud.modifier.BottomRightModifier;
import dev.boxadactle.coordinatesdisplay.hud.modifier.TopLeftModifier;
import dev.boxadactle.coordinatesdisplay.hud.modifier.TopRightModifier;

public enum StartCorner {
    TOP_LEFT(TopLeftModifier.class),
    TOP_RIGHT(TopRightModifier.class),
    BOTTOM_LEFT(BottomLeftModifier.class),
    BOTTOM_RIGHT(BottomRightModifier.class);

    final HudPositionModifier modifier;

    StartCorner(Class<? extends HudPositionModifier> modifier) {
        this.modifier = BoxLib.initializeClass(modifier);
    }

    public HudPositionModifier getModifier() {
        return modifier;
    }
}
