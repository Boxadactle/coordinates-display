package dev.boxadactle.coordinatesdisplay.hud.visibility;

import dev.boxadactle.coordinatesdisplay.hud.HudVisibility;
import dev.boxadactle.coordinatesdisplay.hud.HudVisibilityFilter;

@HudVisibility("never")
public class NeverVisibility implements HudVisibilityFilter {
    @Override
    public boolean isVisible() {
        return false;
    }
}
