package dev.boxadactle.coordinatesdisplay.config.screen;

import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.math.mathutils.Clamps;
import dev.boxadactle.boxlib.util.MouseUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.config.HudHelper;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.hud.HudPositionModifier;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class HudPositionScreen extends BOptionScreen implements HudHelper {

    int x;
    int y;
    float scale;

    int hudOffsetX;
    int hudOffsetY;

    boolean clickDelta = false;
    boolean scaleDelta = false;
    boolean moveDelta = false;

    Position pos;

    int delay = 10;

    public HudPositionScreen(Screen parent) {
        super(parent);

        x = CoordinatesDisplay.CONFIG.get().hudX;
        y = CoordinatesDisplay.CONFIG.get().hudY;
        scale = CoordinatesDisplay.CONFIG.get().hudScale;

        pos = WorldUtils.getWorld() != null
            ? Position.of(WorldUtils.getCamera())
            : generatePositionData();

        CoordinatesDisplay.shouldHudRender = false;
    }

    @Override
    protected boolean shouldRenderScrollingWidget() {
        return false;
    }

    @Override
    public void render(GuiGraphics p_96562_, int mouseX, int mouseY, float delta) {
        this.renderBackground(p_96562_, mouseX, mouseY, delta);
        super.render(p_96562_, mouseX, mouseY, delta);

        boolean isDragging = MouseUtils.isMouseDown(0);
        HudPositionModifier modifier = CoordinatesDisplay.getConfig().startCorner.getModifier();

        if (isDragging && delay == 0 && config().renderMode.getRenderer().allowMove()) {
            if (CoordinatesDisplay.HUD.isScaleButtonHovered(mouseX, mouseY) && !scaleDelta && !moveDelta) scaleDelta = true;

            if (!scaleDelta) {
                if (!clickDelta) {
                    clickDelta = true;

                    Dimension<Integer> distance = getDistance(
                            new Vec2<>(Math.round(mouseX / scale), Math.round(mouseY / scale)),
                            calculateCorner(CoordinatesDisplay.HUD.getRect(), config().startCorner)
                    );
                    hudOffsetX = distance.getWidth();
                    hudOffsetY = distance.getHeight();
                }

                /*Rect<Integer> clamped = Clamps.clampRect(
                        new Rect<>(
                                Math.round(mouseX / scale) - hudOffsetX,
                                Math.round(mouseY / scale) - hudOffsetY,
                                CoordinatesDisplay.OVERLAY.getWidth() / scale,
                                CoordinatesDisplay.OVERLAY.getHeight() / scale
                        ),
                        new Rect<>(
                                0, 0,
                                this.width,
                                this.height
                        )

                );*/

                Vec2<Integer> vec = modifier.translateVector(new Vec2<>(
                                Clamps.clamp(Math.round(mouseX / scale) - hudOffsetX, 0, Math.round(this.width / scale)),
                                Clamps.clamp(Math.round(mouseY / scale) - hudOffsetY, 0, Math.round(this.height / scale))
                        ),
                        new Dimension<>(
                                Math.round(minecraft.getWindow().getGuiScaledWidth() / scale),
                                Math.round(minecraft.getWindow().getGuiScaledHeight() / scale)
                        ),
                        ModConfig.StartCorner.TOP_LEFT
                );

                x = vec.getX();
                y = vec.getY();

                if (!moveDelta) moveDelta = true;

            } else {
                if (!clickDelta) {
                    clickDelta = true;

                    hudOffsetX = config().hudX;
                    hudOffsetY = config().hudY;
                }

                Vec2<Integer> mouse = ModConfig.StartCorner.TOP_LEFT.getModifier().translateVector(
                        new Vec2<>(
                                (mouseX),
                                (mouseY)
                        ), new Dimension<>(
                                Math.round(minecraft.getWindow().getGuiScaledWidth()),
                                Math.round(minecraft.getWindow().getGuiScaledHeight())
                        ),
                        config().startCorner
                );
                scale = ModUtil.calculateMouseScale(
                        Math.round(config().hudX * scale),
                        Math.round(config().hudY * scale),
                        CoordinatesDisplay.HUD.getWidth(), CoordinatesDisplay.HUD.getHeight(),
                        Math.abs(mouse.getX()), Math.abs(mouse.getY())
                );

                x = Math.round(hudOffsetX / scale);
                y = Math.round(hudOffsetY / scale);

            }
        } else {
            if (delay != 0) delay -= 1;
        }

        if (!isDragging && clickDelta) {
            clickDelta = false;
            scaleDelta = false;
            moveDelta = false;
        }

        CoordinatesDisplay.HUD.render(
                p_96562_,
                pos,
                x, y,
                CoordinatesDisplay.getConfig().renderMode,
                CoordinatesDisplay.getConfig().startCorner,
                CoordinatesDisplay.HUD.isHovered(mouseX, mouseY),
                scale
        );

    }

    private Vec2<Integer> calculateCorner(Rect<Integer> size, ModConfig.StartCorner corner) {
        Vec2<Integer> s = new Vec2<>(size.getX(), size.getY());

        switch (corner) {
            case TOP_RIGHT:
                s.setX(size.getMaxX());
                break;

            case BOTTOM_LEFT:
                s.setY(size.getMaxY());
                break;

            case BOTTOM_RIGHT:
                s.setX(size.getMaxX());
                s.setY(size.getMaxY());
                break;

            default:
                break;
        }

        return s;
    }

    @Override
    public void onClose() {
        super.onClose();

        CoordinatesDisplay.CONFIG.get().hudX = x;
        CoordinatesDisplay.CONFIG.get().hudY = y;
        CoordinatesDisplay.CONFIG.get().hudScale = scale;

        if (WorldUtils.getWorld() != null) {
            CoordinatesDisplay.CONFIG.save();
        }

        CoordinatesDisplay.shouldHudRender = true;
    }

    @Override
    protected Component getName() {
        return  Component.translatable("screen.coordinatesdispaly.hudposition");
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.addRenderableWidget(createSaveButton(startX, startY, b -> this.onClose()));
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
