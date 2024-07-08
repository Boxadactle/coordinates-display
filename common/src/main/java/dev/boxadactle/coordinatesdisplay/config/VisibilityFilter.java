package dev.boxadactle.coordinatesdisplay.config;

import dev.boxadactle.boxlib.core.BoxLib;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.hud.HudVisibility;
import dev.boxadactle.coordinatesdisplay.hud.HudVisibilityFilter;
import dev.boxadactle.coordinatesdisplay.hud.visibility.*;
import net.minecraft.network.chat.Component;

public enum VisibilityFilter {
    ALWAYS(AlwaysVisibility.class),
    HOLD_COMPASS(HoldCompassVisibility.class),
    OWN_COMPASS(OwnCompassVisibility.class),
    HOLD_MAP(HoldMapVisibility.class),
    OWN_MAP(OwnMapVisibility.class);

    HudVisibilityFilter filter;
    HudVisibility metadata;

     VisibilityFilter(Class<? extends HudVisibilityFilter> filter) {
        this.filter = BoxLib.initializeClass(filter);

        HudVisibility m = filter.getAnnotation(HudVisibility.class);
        if (m != null) {
            metadata = m;
        } else {
            throw new IllegalStateException("Attempting to register Hud visibility filter without Hud.VisibilityFilter annotation!");
        }
    }

    public HudVisibilityFilter getFilter() {
        return filter;
    }

    public HudVisibility getMetadata() {
        return metadata;
    }

    public Component getComponent() {
        return Component.translatable(filter.getNameKey());
    }

    public String getName() {
        return GuiUtils.getTranslatable(filter.getTranslationKey());
    }

    public String getId() {
        return metadata.value();
    }

}
