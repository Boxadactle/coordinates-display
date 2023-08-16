package dev.boxadactle.coordinatesdisplay.util;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.math.Rect;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class HudRenderer {

    int w = 0;
    int h = 0;

    int x = 0;
    int y = 0;

    int scaleSize;

    float scale = CoordinatesDisplay.CONFIG.get().hudScale;

    public boolean isHovered(int mouseX, int mouseY) {
        return ModUtil.isMouseHovering(Math.round(mouseX / scale), Math.round(mouseY / scale), x, y, w, h);
    }

    public boolean isScaleButtonHovered(int mouseX, int mouseY) {
        int scaleX = (x + w - scaleSize);
        int scaleY = (y + h - scaleSize);
        return ModUtil.isMouseHovering(Math.round(mouseX / scale), Math.round(mouseY / scale), scaleX, scaleY, scaleX + scaleSize, scaleY + scaleSize);
    }

    public void render(GuiGraphics guiGraphics, Position pos, int x, int y, ModConfig.RenderMode renderMode, boolean moveOverlay) {
        try {
            Renderer r = renderMode.getRenderer();

            Rect<Integer> size = r.renderOverlay(guiGraphics, x, y, pos);
            this.x = size.getX();
            this.y = size.getY();
            this.w = size.getWidth();
            this.h = size.getHeight();

            if (moveOverlay) {
                renderMoveOverlay(guiGraphics, x, y);
            }
        } catch (NullPointerException e) {
            CoordinatesDisplay.LOGGER.printStackTrace(e);
        }
    }

    public void render(GuiGraphics guiGraphics, Position pos, int x, int y, ModConfig.RenderMode renderMode, boolean moveOverlay, float scale) {
        try {
            PoseStack matrices = guiGraphics.pose();

            matrices.pushPose();

            matrices.scale(scale, scale, scale);

            this.scale = scale;

            render(guiGraphics, pos, x, y, renderMode, moveOverlay);

            matrices.popPose();
        } catch (NullPointerException e) {
            CoordinatesDisplay.LOGGER.printStackTrace(e);
        }
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    private void renderMoveOverlay(GuiGraphics guiGraphics, int x, int y) {
        int color = 0x50c7c7c7;
        scaleSize = 5;
        int scaleColor = 0x99d9fffa;

        // overlay color
        RenderUtils.drawSquare(guiGraphics, x, y, w, h, color);

        // scale square
        int scaleX = x + w - scaleSize;
        int scaleY = y + h - scaleSize;
        RenderUtils.drawSquare(guiGraphics, scaleX, scaleY, scaleSize, scaleSize, scaleColor);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract static class Renderer {
        String key;

        public Renderer(String translateKey) {
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
}