package dev.boxadactle.coordinatesdisplay.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;

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

    public boolean shouldRender(String visibilityFilter) {
        CoordinatesHuds.RegisteredVisibilityFilter filter = CoordinatesHuds.getVisibilityFilter(visibilityFilter);
        boolean bl = true;

        // have you ever seen anyone use this operand
        bl &= !ClientUtils.getOptions().hideGui;
        bl &= !ClientUtils.getClient().getDebugOverlay().showDebugScreen();
        bl &= CoordinatesDisplay.shouldHudRender;
        bl &= filter.getFilter().isVisible();

        return bl && CoordinatesDisplay.getConfig().enabled;
    }

    public void render(GuiGraphics guiGraphics, Position pos, int x, int y, String renderMode, ModConfig.StartCorner startCorner, boolean moveOverlay) {
        try {
            CoordinatesHuds.RegisteredRenderer overlay = CoordinatesHuds.getRenderer(renderMode);

            if (overlay == null) {
                throw new UnknownRendererException(renderMode);
            }

            // only way to do this is the use the size of the hud from the previous frame
            Rect<Integer> newPos = overlay.getMetadata().ignoreTranslations() ? new Rect<>(x, y, size.getWidth(), size.getHeight()) : startCorner.getModifier().translateRect(new Rect<>(x, y, size.getWidth(), size.getHeight()), new Dimension<>(
                    Math.round(ClientUtils.getClient().getWindow().getGuiScaledWidth() / scale),
                    Math.round(ClientUtils.getClient().getWindow().getGuiScaledHeight() / scale)
            ), ModConfig.StartCorner.TOP_LEFT);

            Rect<Integer> size = overlay.getRenderer().renderOverlay(guiGraphics, newPos.getX(), newPos.getY(), pos);
            this.size.setX(size.getX());
            this.size.setY(size.getY());
            this.size.setWidth(size.getWidth());
            this.size.setHeight(size.getHeight());

            if (moveOverlay && overlay.getMetadata().allowMove()) {
                renderMoveOverlay(guiGraphics, newPos.getX(), newPos.getY());
            }
        } catch (NullPointerException e) {
            CoordinatesDisplay.LOGGER.error("An unexpected error occurred!");
            CoordinatesDisplay.LOGGER.printStackTrace(e);
        }
    }

    public void render(GuiGraphics guiGraphics, Position pos, int x, int y, String renderMode, ModConfig.StartCorner startCorner, boolean moveOverlay, float scale) {
        try {
            CoordinatesHuds.RegisteredRenderer overlay = CoordinatesHuds.getRenderer(renderMode);

            if (overlay == null) {
                throw new UnknownRendererException(renderMode);
            }

            if (!overlay.getMetadata().ignoreTranslations()) {
                PoseStack matrices = guiGraphics.pose();

                matrices.pushPose();

                matrices.scale(scale, scale, scale);

                this.scale = scale;

                render(guiGraphics, pos, x, y, renderMode, startCorner, moveOverlay);

                matrices.popPose();
            } else render(guiGraphics, pos, x, y, renderMode, startCorner, moveOverlay);
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

    private void renderMoveOverlay(GuiGraphics guiGraphics, int x, int y) {
        int color = 0x50c7c7c7;
        scaleSize = 5;
        int scaleColor = 0x99d9fffa;

        // overlay color
        RenderUtils.drawSquare(guiGraphics, x, y, size.getWidth(), size.getHeight(), color);

        // scale square
        scaleButton = calculateScaleButton(CoordinatesDisplay.getConfig().startCorner);
        int scaleX = scaleButton.getX();
        int scaleY = scaleButton.getY();
        RenderUtils.drawSquare(guiGraphics, scaleX, scaleY, scaleSize, scaleSize, scaleColor);
    }

    private Rect<Integer> calculateScaleButton(ModConfig.StartCorner corner) {
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