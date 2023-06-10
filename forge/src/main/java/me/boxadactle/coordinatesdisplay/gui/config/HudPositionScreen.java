package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

public class HudPositionScreen extends Screen {

    private final Minecraft client = Minecraft.getInstance();

    Screen parent;

    Vec3 pos;
    ChunkPos chunkPos;
    float cameraYaw;
    float cameraPitch;

    boolean lockHudPos = false;

    int x;
    int y;
    float scale;

    int hudOffsetX;
    int hudOffsetY;

    boolean clickDelta = false;
    boolean scaleDelta = false;
    boolean moveDelta = false;

    int delay = 10;

    public HudPositionScreen(Screen parent) {
        super(Component.translatable("screen.coordinatesdisplay.config.position"));
        this.parent = parent;

        this.pos = new Vec3(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos((int)Math.round(this.pos.x), (int)Math.round(this.pos.z));
        this.cameraYaw = ModUtil.randomDegrees();
        this.cameraPitch = ModUtil.randomDegrees();

        x = CoordinatesDisplay.CONFIG.get().hudX;
        y = CoordinatesDisplay.CONFIG.get().hudY;
        scale = CoordinatesDisplay.CONFIG.get().hudScale;

        if (Minecraft.getInstance().level != null) {
            CoordinatesDisplay.shouldRenderOnHud = false;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);

        if (ModUtil.isMousePressed() && delay == 0) {
            if (CoordinatesDisplay.OVERLAY.isScaleButtonHovered(mouseX, mouseY) && !scaleDelta && !moveDelta) scaleDelta = true;

            if (!scaleDelta) {
                if (!clickDelta) {
                    clickDelta = true;

                    int[] distance = ModUtil.getDistance(Math.round(mouseX / scale), Math.round(mouseY / scale), CoordinatesDisplay.OVERLAY.getX(), CoordinatesDisplay.OVERLAY.getY());
                    hudOffsetX = distance[0];
                    hudOffsetY = distance[1];
                }

                x = Math.round(ModUtil.clampToZero(Math.round(mouseX / scale) - hudOffsetX));
                y = Math.round(ModUtil.clampToZero(Math.round(mouseY / scale) - hudOffsetY));

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

        if (!ModUtil.isMousePressed() && clickDelta) {
            clickDelta = false;
            scaleDelta = false;
            moveDelta = false;
        }

        CoordinatesDisplay.OVERLAY.render(guiGraphics, pos, chunkPos, cameraYaw, cameraPitch, null, x , y, CoordinatesDisplay.CONFIG.get().minMode, CoordinatesDisplay.OVERLAY.isHovered(mouseX, mouseY), scale);

    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void onClose() {
        CoordinatesDisplay.CONFIG.get().hudX = x;
        CoordinatesDisplay.CONFIG.get().hudY = y;
        CoordinatesDisplay.CONFIG.get().hudScale = scale;

        if (Minecraft.getInstance().level != null) {
            CoordinatesDisplay.shouldRenderOnHud = true;
            CoordinatesDisplay.CONFIG.save();
        }

        this.client.setScreen(parent);
    }
}
