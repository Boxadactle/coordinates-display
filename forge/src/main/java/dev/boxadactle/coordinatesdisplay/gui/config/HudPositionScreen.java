package dev.boxadactle.coordinatesdisplay.gui.config;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.gui.BConfigScreen;
import dev.boxadactle.boxlib.math.MathHelper;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.gui.HudHelper;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class HudPositionScreen extends BConfigScreen implements HudHelper {

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

    boolean isDragging = false;

    public HudPositionScreen(Screen parent) {
        super(parent);

        x = CoordinatesDisplay.CONFIG.get().hudX;
        y = CoordinatesDisplay.CONFIG.get().hudY;
        scale = CoordinatesDisplay.CONFIG.get().hudScale;

        pos = this.generatePositionData();
    }

    @Override
    public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
        isDragging = true;
        return super.mouseClicked(p_94695_, p_94696_, p_94697_);
    }

    @Override
    public boolean mouseReleased(double p_94722_, double p_94723_, int p_94724_) {
        isDragging = false;
        return super.mouseReleased(p_94722_, p_94723_, p_94724_);
    }

    @Override
    protected boolean shouldRenderScrollingWidget() {
        return false;
    }

    @Override
    public void render(GuiGraphics p_96562_, int mouseX, int mouseY, float delta) {
        this.renderBackground(p_96562_);
        super.render(p_96562_, mouseX, mouseY, delta);

        if (isDragging && delay == 0) {
            if (CoordinatesDisplay.OVERLAY.isScaleButtonHovered(mouseX, mouseY) && !scaleDelta && !moveDelta) scaleDelta = true;

            if (!scaleDelta) {
                if (!clickDelta) {
                    clickDelta = true;

                    int[] distance = ModUtil.getDistance(Math.round(mouseX / scale), Math.round(mouseY / scale), CoordinatesDisplay.OVERLAY.getX(), CoordinatesDisplay.OVERLAY.getY());
                    hudOffsetX = distance[0];
                    hudOffsetY = distance[1];
                }

                x = Math.round(MathHelper.clamp(Math.round(mouseX / scale) - hudOffsetX, 0, this.width));
                y = Math.round(MathHelper.clamp(Math.round(mouseY / scale) - hudOffsetY, 0, this.height));

                if (!moveDelta) moveDelta = true;

            } else {
                if (!clickDelta) {
                    clickDelta = true;

                    hudOffsetX = Math.round(CoordinatesDisplay.OVERLAY.getX() * scale);
                    hudOffsetY = Math.round(CoordinatesDisplay.OVERLAY.getY() * scale);
                }

                scale = ModUtil.calculateMouseScale(
                        Math.round(CoordinatesDisplay.OVERLAY.getX() * scale),
                        Math.round(CoordinatesDisplay.OVERLAY.getY() * scale),
                        CoordinatesDisplay.OVERLAY.getWidth(),
                        CoordinatesDisplay.OVERLAY.getHeight(),
                        mouseX, mouseY
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

        CoordinatesDisplay.OVERLAY.render(p_96562_, pos, x, y, CoordinatesDisplay.CONFIG.get().renderMode, CoordinatesDisplay.OVERLAY.isHovered(mouseX, mouseY), scale);

    }

    @Override
    public void close() {
        super.close();

        CoordinatesDisplay.CONFIG.get().hudX = x;
        CoordinatesDisplay.CONFIG.get().hudY = y;
        CoordinatesDisplay.CONFIG.get().hudScale = scale;

        if (WorldUtils.getWorld() != null) {
            CoordinatesDisplay.CONFIG.save();
        }
    }

    @Override
    protected Component getName() {
        return  Component.translatable("screen.coordinatesdispaly.hudposition");
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.addRenderableWidget(this.createSaveButton(startX, startY, b -> this.close()));
    }

    @Override
    protected void initConfigButtons() {
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
