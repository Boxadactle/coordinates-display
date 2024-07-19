package dev.boxadactle.coordinatesdisplay.hud.visibility;

import dev.boxadactle.coordinatesdisplay.hud.HudVisibility;
import dev.boxadactle.coordinatesdisplay.hud.HudVisibilityFilter;

@HudVisibility("always")
public class AlwaysVisibility implements HudVisibilityFilter {

    @Override
    public boolean isVisible() {
        return true;
    }

}
