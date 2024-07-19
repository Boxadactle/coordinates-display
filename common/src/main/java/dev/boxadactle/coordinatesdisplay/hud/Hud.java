package dev.boxadactle.coordinatesdisplay.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.registry.DisplayMode;
import dev.boxadactle.coordinatesdisplay.registry.StartCorner;
import dev.boxadactle.coordinatesdisplay.registry.VisibilityFilter;
import dev.boxadactle.coordinatesdisplay.position.Position;
import org.lwjgl.opengl.GL11;

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

    public void render(PoseStack stack, Position pos, int x, int y, DisplayMode renderMode, StartCorner startCorner, boolean moveOverlay) {
        try {
            // only way to do this is the use the size of the hud from the previous frame
            Rect<Integer> newPos = renderMode.getMetadata().ignoreTranslations() ? new Rect<>(x, y, size.getWidth(), size.getHeight()) : startCorner.getModifier().translateRect(new Rect<>(x, y, size.getWidth(), size.getHeight()), new Dimension<>(
                    Math.round(ClientUtils.getClient().getWindow().getGuiScaledWidth() / scale),
                    Math.round(ClientUtils.getClient().getWindow().getGuiScaledHeight() / scale)
            ), StartCorner.TOP_LEFT);

            Rect<Integer> size = renderMode.getRenderer().renderOverlay(stack, newPos.getX(), newPos.getY(), pos);
            this.size.setX(size.getX());
            this.size.setY(size.getY());
            this.size.setWidth(size.getWidth());
            this.size.setHeight(size.getHeight());

            if (moveOverlay && renderMode.getMetadata().allowMove()) {
                renderMoveOverlay(stack, newPos.getX(), newPos.getY());
            }
        } catch (NullPointerException e) {
            CoordinatesDisplay.LOGGER.error("An unexpected error occurred!");
            CoordinatesDisplay.LOGGER.printStackTrace(e);
        }
    }

    public void render(PoseStack stack, Position pos, int x, int y, DisplayMode renderMode, StartCorner startCorner, boolean moveOverlay, float scale) {
        try {
            if (!renderMode.getMetadata().ignoreTranslations()) {
                GL11.glPushMatrix();

                GL11.glScalef(scale, scale, scale);

                this.scale = scale;

                render(stack, pos, x, y, renderMode, startCorner, moveOverlay);

                GL11.glPopMatrix();
            } else render(stack, pos, x, y, renderMode, startCorner, moveOverlay);
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

    private void renderMoveOverlay(PoseStack stack, int x, int y) {
        int color = 0x50c7c7c7;
        scaleSize = 5;
        int scaleColor = 0x99d9fffa;

        // overlay color
        RenderUtils.drawSquare(stack, x, y, size.getWidth(), size.getHeight(), color);

        // scale square
        scaleButton = calculateScaleButton(CoordinatesDisplay.getConfig().startCorner);
        int scaleX = scaleButton.getX();
        int scaleY = scaleButton.getY();
        RenderUtils.drawSquare(stack, scaleX, scaleY, scaleSize, scaleSize, scaleColor);
    }

    private Rect<Integer> calculateScaleButton(StartCorner corner) {
        Rect<Integer> pos = new Rect<>(
                size.getX() + size.getWidth() - scaleSize,
                size.getY() + size.getHeight() - scaleSize,
                scaleSize, scaleSize
        );

        switch (corner) {
            case TOP_RIGHT:
                pos.setX(size.getX());
                break;
            case BOTTOM_LEFT:
                pos.setY(size.getY());
                break;
            case BOTTOM_RIGHT:
                pos.setX(size.getX());
                pos.setY(size.getY());
                break;
            default:
                break;
        }

        return pos;
    }

    public int getX() {
        return size.getX();
    }

    public int getY() {
        return size.getY();
    }
}