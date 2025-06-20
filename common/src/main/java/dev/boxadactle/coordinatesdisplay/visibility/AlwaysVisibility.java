package dev.boxadactle.coordinatesdisplay.visibility;

import dev.boxadactle.coordinatesdisplay.HudVisibility;
import dev.boxadactle.coordinatesdisplay.HudVisibilityFilter;

@HudVisibility("always")
public class AlwaysVisibility implements HudVisibilityFilter {

    @Override
    public boolean isVisible() {
        return true;
    }

}
