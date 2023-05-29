package me.boxadactle.coordinatesdisplay.gui.config;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

public class HudPositionScreen extends Screen {

    private final MinecraftClient client = MinecraftClient.getInstance();

    Screen parent;

    Vec3d pos;
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
        super(Text.translatable("screen.coordinatesdisplay.config.position"));
        this.parent = parent;

        this.pos = new Vec3d(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos((int)Math.round(this.pos.x), (int)Math.round(this.pos.z));
        this.cameraYaw  = (float) Math.random() * 180;
        this.cameraPitch  = (float) Math.random() * 180;

        x = CoordinatesDisplay.CONFIG.get().hudX;
        y = CoordinatesDisplay.CONFIG.get().hudY;
        scale = CoordinatesDisplay.CONFIG.get().hudScale;

        if (MinecraftClient.getInstance().world != null) {
            CoordinatesDisplay.shouldRenderOnHud = false;
        }

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);

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

        CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, cameraPitch, null, x , y, CoordinatesDisplay.CONFIG.get().minMode, CoordinatesDisplay.OVERLAY.isHovered(mouseX, mouseY), scale);

    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void close() {
        CoordinatesDisplay.CONFIG.get().hudX = x;
        CoordinatesDisplay.CONFIG.get().hudY = y;
        CoordinatesDisplay.CONFIG.get().hudScale = scale;

        if (MinecraftClient.getInstance().world != null) {
            CoordinatesDisplay.shouldRenderOnHud = true;
            CoordinatesDisplay.CONFIG.save();
        }

        this.client.setScreen(parent);
    }
}
