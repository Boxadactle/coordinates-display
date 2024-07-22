package dev.boxadactle.coordinatesdisplay.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.math.mathutils.Clamps;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.registry.DisplayMode;
import dev.boxadactle.coordinatesdisplay.registry.StartCorner;
import dev.boxadactle.coordinatesdisplay.registry.VisibilityFilter;
import dev.boxadactle.coordinatesdisplay.position.Position;

import java.lang.reflect.InvocationTargetException;

public class Hud {

    Rect<Integer> size = new Rect<>(0, 0, 0, 0);

    int scaleSize;

    float scale = CoordinatesDisplay.CONFIG.get().hudScale;

    Rect<Integer> scaleButton = new Rect<>(0, 0, 0, 0);

    public boolean isHovered(int mouseX, int mouseY) {
        return ModUtil.isMouseHovering(
                Math.round(mouseX / scale),
                Math.round(mouseY / scale),
                size.getX(), size.getY(),
                size.getWidth(),
                size.getHeight()
        );
    }

    public boolean isScaleButtonHovered(int mouseX, int mouseY) {
        return scaleButton.containsPoint(new Vec2<>(Math.round(mouseX / scale), Math.round(mouseY / scale)));
    }

    public boolean shouldRender(VisibilityFilter filter) throws UnknownVisibilityFilterException {
        boolean bl = true;

        // have you ever seen anyone use this operand
        bl &= !ClientUtils.getOptions().hideGui;
        bl &= !ClientUtils.getOptions().renderDebug;
        bl &= CoordinatesDisplay.shouldHudRender;
        bl &= filter.getFilter().isVisible();

        return bl && CoordinatesDisplay.getConfig().enabled;
    }

    public RenderingLayout preRender(Position pos, int x, int y, DisplayMode renderMode, StartCorner startCorner) {
        try {
            RenderingLayout layout = renderMode.getRenderer().renderOverlay(x, y, pos);

            Rect<Integer> startSize = layout.calculateRect();

            Dimension<Integer> window = new Dimension<>(
                    Math.round(ClientUtils.getClient().getWindow().getGuiScaledWidth() / scale),
                    Math.round(ClientUtils.getClient().getWindow().getGuiScaledHeight() / scale)
            );
            Rect<Integer> newPos = renderMode.getMetadata().ignoreTranslations() ?
                    renderMode.getMetadata().positionModifier().getDeclaredConstructor().newInstance().getPosition(startSize, window) :
                    startCorner.getModifier().translateRect(startSize, window);

            layout.setPosition(newPos.getX(), newPos.getY());

            return layout;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(PoseStack stack, RenderingLayout layout, DisplayMode renderMode) {
        try {
            Rect<Integer> size = HudRenderer.renderHud(stack, layout, renderMode.getMetadata().hasBackground());

            this.size.setX(size.getX());
            this.size.setY(size.getY());
            this.size.setWidth(size.getWidth());
            this.size.setHeight(size.getHeight());
        } catch (NullPointerException e) {
            CoordinatesDisplay.LOGGER.error("An unexpected error occurred!");
            CoordinatesDisplay.LOGGER.printStackTrace(e);
        }
    }

    public void render(PoseStack stack, Position pos, int x, int y, DisplayMode renderMode, StartCorner startCorner) {
        RenderingLayout layout = preRender(pos, x, y, renderMode, startCorner);

        render(stack, layout, renderMode);
    }

    public void render(PoseStack stack, Position pos, int x, int y, DisplayMode renderMode, StartCorner startCorner, float scale) {
        try {
            if (!renderMode.getMetadata().ignoreTranslations()) {
                stack.pushPose();

                stack.scale(scale, scale, scale);

                this.scale = scale;

                render(stack, pos, x, y, renderMode, startCorner);

                stack.popPose();
            } else render(stack, pos, x, y, renderMode, startCorner);
        } catch (NullPointerException e) {
            CoordinatesDisplay.LOGGER.printStackTrace(e);
        }
    }

    public Rect<Integer> getRect() {
        return size;
    }

    public int getWidth() {
        return size.getWidth();
    }

    public int getHeight() {
        return size.getHeight();
    }

    public void renderMoveOverlay(PoseStack stack, int x, int y) {
        int color = 0x50c7c7c7;
        scaleSize = 5;
        int scaleColor = 0x99d9fffa;

        // overlay color
        RenderUtils.drawSquare(stack, x, y, size.getWidth(), size.getHeight(), color);

        // scale square
        scaleButton = new Rect<>(
                size.getMaxX() - scaleSize,
                size.getMaxY() - scaleSize,
                scaleSize, scaleSize
        );
        int scaleX = scaleButton.getX();
        int scaleY = scaleButton.getY();
        RenderUtils.drawSquare(stack, scaleX, scaleY, scaleSize, scaleSize, scaleColor);
    }

    public float calculateScale(int x, int y, int mouseX, int mouseY) {
        float rectSize = ModUtil.calculatePointDistance(x, y, x + size.getWidth(), y + size.getY());
        float mouseSize = ModUtil.calculatePointDistance(x, y, mouseX, mouseY);

        float scaleFactor = mouseSize / rectSize;

        scaleFactor = Math.round(scaleFactor * 10) / 10.0f;
        scaleFactor = Clamps.clamp(scaleFactor, 0.5f, 2.0f);

        return scaleFactor;
    }

    public int getX() {
        return size.getX();
    }

    public int getY() {
        return size.getY();
    }
}