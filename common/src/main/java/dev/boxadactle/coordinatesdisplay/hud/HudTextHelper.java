package dev.boxadactle.coordinatesdisplay.hud;

import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.network.chat.Component;

public abstract class HudTextHelper {
    protected abstract String getKey();

    protected Component translation(String t, Object ...args) {
        return Component.translatable(getKey() + t, args);
    }

    protected Component definition(Component t) {
        return GuiUtils.colorize(t, CoordinatesDisplay.getConfig().definitionColor);
    }

    protected Component definition(String t) {
        return GuiUtils.colorize(Component.literal(t), CoordinatesDisplay.getConfig().definitionColor);
    }

    protected Component definition(String k, Object ...args) {
        return definition(translation(k, args));
    }

    protected Component value(String t) {
        return GuiUtils.colorize(Component.literal(t), CoordinatesDisplay.getConfig().dataColor);
    }

    protected Component value(Component t) {
        return GuiUtils.colorize(t, CoordinatesDisplay.getConfig().dataColor);
    }

    protected Component value(String k, Object ...args) {
        return value(translation(k, args));
    }

}
