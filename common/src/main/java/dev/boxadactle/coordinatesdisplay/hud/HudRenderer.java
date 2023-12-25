package dev.boxadactle.coordinatesdisplay.hud;

import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public interface HudRenderer {

    default boolean ignoreTranslations() {
        return false;
    }

    default boolean allowMove() {
        return true;
    }

    default ModConfig config() {
        return CoordinatesDisplay.getConfig();
    }

    default void drawInfo(GuiGraphics guiGraphics, Component component, int x, int y, int color) {
        guiGraphics.drawString(GuiUtils.getTextRenderer(), component, x, y, color, CoordinatesDisplay.CONFIG.get().hudTextShadow);
    }

    default void drawInfo(GuiGraphics guiGraphics, Component component, int x, int y) {
        drawInfo(guiGraphics, component, x, y, GuiUtils.WHITE);
    }

    Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos);



}
