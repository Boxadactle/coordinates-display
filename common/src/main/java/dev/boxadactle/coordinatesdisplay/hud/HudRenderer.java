package dev.boxadactle.coordinatesdisplay.hud;

import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public abstract class HudRenderer {
    String key;

    public HudRenderer(String translateKey) {
        key = translateKey;
    }

    protected Component translation(String t, Object ...args) {
        return Component.translatable(key + t, args);
    }

    protected ModConfig config() {
        return CoordinatesDisplay.getConfig();
    }

    protected void drawInfo(GuiGraphics guiGraphics, Component component, int x, int y, int color) {
        guiGraphics.drawString(GuiUtils.getTextRenderer(), component, x, y, color, CoordinatesDisplay.CONFIG.get().hudTextShadow);
    }

    protected void drawInfo(GuiGraphics guiGraphics, Component component, int x, int y) {
        drawInfo(guiGraphics, component, x, y, GuiUtils.WHITE);
    }

    protected abstract Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos);

    protected Component definition(Component t) {
        return GuiUtils.colorize(t, config().definitionColor);
    }

    protected Component value(Component t) {
        return GuiUtils.colorize(t, config().dataColor);
    }

    protected Component definition(String t) {
        return GuiUtils.colorize(Component.literal(t), config().definitionColor);
    }

    protected Component value(String t) {
        return GuiUtils.colorize(Component.literal(t), config().dataColor);
    }

}

