package dev.boxadactle.coordinatesdisplay.util;

import dev.boxadactle.coordinatesdisplay.util.position.Position;
import dev.boxadactle.boxlib.math.Rect;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class HudRenderer {

    protected int w = 0;
    protected int h = 0;

    protected int x = 0;
    protected int y = 0;

    int scaleSize;

    float scale = CoordinatesDisplay.CONFIG.get().hudScale;

    static HashMap<ModConfig.RenderMode, Renderer> renderers = new HashMap<>();

    public boolean isHovered(int mouseX, int mouseY) {
        return ModUtil.isMouseHovering(Math.round(mouseX / scale), Math.round(mouseY / scale), x, y, w, h);
    }

    public <T extends Renderer> T register(ModConfig.RenderMode mode, T renderer) {
        renderers.put(mode, renderer);

        CoordinatesDisplay.LOGGER.info("Registered renderer for render mode: " + mode.name());

        return renderer;
    }

    public boolean isScaleButtonHovered(int mouseX, int mouseY) {
        int scaleX = (x + w - scaleSize);
        int scaleY = (y + h - scaleSize);
        return ModUtil.isMouseHovering(Math.round(mouseX / scale), Math.round(mouseY / scale), scaleX, scaleY, scaleX + scaleSize, scaleY + scaleSize);
    }

    public void render(DrawContext drawContext, int x, int y, Position pos, ModConfig.RenderMode renderMode, boolean moveOverlay) throws UnregisteredRendererException {
        try {
            AtomicBoolean hasRendered = new AtomicBoolean(false);
            renderers.forEach((d, r) -> {
                if (renderMode.equals(d)) {
                    Rect<Integer> rect = r.renderOverlay(drawContext, x, y, pos);
                    this.x = rect.getX();
                    this.y = rect.getY();
                    this.w = rect.getWidth();
                    this.h = rect.getHeight();

                    hasRendered.set(true);
                }
            });

            if (!hasRendered.get()) throw new UnregisteredRendererException(renderMode);

            if (moveOverlay) {
                renderMoveOverlay(drawContext, x, y);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void render(DrawContext drawContext, int x, int y, Position pos, ModConfig.RenderMode renderMode, boolean moveOverlay, float scale) throws UnregisteredRendererException {
        try {
            MatrixStack matrices = drawContext.getMatrices();

            matrices.push();

            matrices.scale(scale, scale, scale);

            this.scale = scale;

            render(drawContext, x, y, pos, renderMode, moveOverlay);

            matrices.pop();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    private void renderMoveOverlay(DrawContext drawContext, int x, int y) {
        int color = 0x50c7c7c7;
        scaleSize = 5;
        int scaleColor = 0x99d9fffa;

        // overlay color
        RenderUtils.drawSquare(drawContext, x, y, w, h, color);

        // scale square
        int scaleX = x + w - scaleSize;
        int scaleY = y + h - scaleSize;
        RenderUtils.drawSquare(drawContext, scaleX, scaleY, scaleSize, scaleSize, scaleColor);
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

        protected Text translation(String t, Object ...args) {
            return Text.translatable(key + t, args);
        }

        protected ModConfig config() {
            return CoordinatesDisplay.getConfig();
        }

        protected void drawInfo(DrawContext drawContext, Text text, int x, int y, int color) {
            drawContext.drawText(GuiUtils.getTextRenderer(), text, x, y, color, CoordinatesDisplay.CONFIG.get().hudTextShadow);
        }

        protected abstract Rect<Integer> renderOverlay(DrawContext drawContext, int x, int y, Position pos);
    }
}
