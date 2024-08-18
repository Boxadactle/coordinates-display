package dev.boxadactle.coordinatesdisplay.screen.config;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.math.mathutils.Clamps;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.registry.StartCorner;
import dev.boxadactle.coordinatesdisplay.screen.HudHelper;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PositionScreen extends BOptionScreen implements HudHelper {

    int x;
    int y;
    float scale;

    int hudOffsetX;
    int hudOffsetY;

    boolean clickDelta = false;
    boolean scaleDelta = false;
    boolean moveDelta = false;

    Position pos;

    boolean isDragging = false;

    public PositionScreen(Screen parent) {
        super(parent);

        pos = WorldUtils.getWorld() != null
            ? Position.of(WorldUtils.getPlayer())
            : generatePositionData();

        CoordinatesDisplay.shouldHudRender = false;
    }

    @Override
    protected void init() {
        super.init();

        Rect<Integer> hud = CoordinatesDisplay.HUD.preRender(pos, config().hudX, config().hudY, config().renderMode, config().startCorner).calculateRect();

        x = hud.getX();
        y = hud.getY();
        scale = CoordinatesDisplay.CONFIG.get().hudScale;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        renderBackground(guiGraphics, i, j, f);
        super.render(guiGraphics, i, j, f);

        if (moveHud(i, j)) {
            Rect<Integer> rect = Clamps.clampRect(
                    x, y,
                    CoordinatesDisplay.HUD.getWidth(),
                    CoordinatesDisplay.HUD.getHeight(),
                    0, 0,
                    Math.round(width / scale),
                    Math.round(height / scale)
            );

            x = rect.getX();
            y = rect.getY();
        }

        CoordinatesDisplay.HUD.render(
                guiGraphics,
                pos,
                x,
                y,
                config().renderMode,
                StartCorner.TOP_LEFT,
                scale
        );

        if (CoordinatesDisplay.HUD.isHovered(i, j)) {
            PoseStack stack = guiGraphics.pose();

            stack.pushPose();
            stack.scale(scale, scale, scale);

            CoordinatesDisplay.HUD.renderMoveOverlay(guiGraphics, x, y);

            stack.popPose();
        }
    }

    public boolean moveHud(int mouseX, int mouseY) {
        if (isDragging) {
            if (!clickDelta) {
                // it is our first time clicking the mouse
                clickDelta = true;

                // so we need to see whether or not we're scaling or moving the hud
                // and retrieve the hud offsets
                if (CoordinatesDisplay.HUD.isScaleButtonHovered(mouseX, mouseY)) {
                    scaleDelta = true;

                    // we retrieve the position of the hud unscaled
                    hudOffsetX = Math.round(x * scale);
                    hudOffsetY = Math.round(y * scale);
                } else {
                    moveDelta = true;

                    // we retrieve the distance between the mouse and the hud position
                    Dimension<Integer> offset = getDistance(new Vec2<>(Math.round(mouseX / scale), Math.round(mouseY / scale)), new Vec2<>(x, y));
                    hudOffsetX = offset.getWidth();
                    hudOffsetY = offset.getHeight();
                }
            }

            if (scaleDelta) {
                scale = CoordinatesDisplay.HUD.calculateScale(hudOffsetX, hudOffsetY, mouseX, mouseY);

                x = Math.round(hudOffsetX / scale);
                y = Math.round(hudOffsetY / scale);
            } else {
                x = Math.round(mouseX / scale) - hudOffsetX;
                y = Math.round(mouseY / scale) - hudOffsetY;
            }

            return true;
        } else {
            if (clickDelta) {
                clickDelta = false;
                scaleDelta = false;
                moveDelta = false;
            }
        }

        return false;
    }

    @Override
    protected boolean shouldRenderScrollingWidget() {
        return false;
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        isDragging = true;
        return super.mouseClicked(d, e, i);
    }

    @Override
    public boolean mouseReleased(double d, double e, int i) {
        isDragging = false;
        return super.mouseReleased(d, e, i);
    }

    @Override
    public void onClose() {
        super.onClose();

        Vec2<Integer> translated = config().startCorner.getModifier().getRelativePos(
                new Rect<>(
                        x, y,
                        CoordinatesDisplay.HUD.getWidth(),
                        CoordinatesDisplay.HUD.getHeight()
                ),
                new Dimension<>(
                        Math.round(width / scale),
                        Math.round(height / scale)
                )
        );

        config().hudX = translated.getX();
        config().hudY = translated.getY();
        config().hudScale = scale;

        if (WorldUtils.getWorld() != null) {
            CoordinatesDisplay.CONFIG.save();
        }

        CoordinatesDisplay.shouldHudRender = true;
    }

    @Override
    protected Component getName() {
        return Component.translatable("screen.coordinatesdispaly.hudposition");
    }

    @Override
    protected void initFooter(int startX, int startY) {
        addRenderableWidget(createSaveButton(startX, startY, b -> this.onClose()));
    }

    @Override
    protected void initConfigButtons() {
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private Dimension<Integer> getDistance(Vec2<Integer> vec1, Vec2<Integer> vec2) {
        int distanceX = vec1.getX() - vec2.getX();
        int distanceY = vec1.getY() - vec2.getY();

        return new Dimension<>(distanceX, distanceY);
    }
}
