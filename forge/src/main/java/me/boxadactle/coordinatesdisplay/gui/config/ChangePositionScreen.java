package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.gui.widget.InvisibleButtonWidget;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChangePositionScreen extends Screen {

    private final Minecraft client = Minecraft.getInstance();

    Screen parent;

    Vec3 pos;
    ChunkPos chunkPos;
    float cameraYaw;
    float cameraPitch;

    boolean lockHudPos = false;

    int x;
    int y;

    public ChangePositionScreen(Screen parent) {
        super(Component.translatable("screen.coordinatesdisplay.config.position"));
        this.parent = parent;

        this.pos = new Vec3(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos(new BlockPos((int) Math.round(pos.x), (int) Math.round(pos.y), (int) Math.round(pos.z)));
        this.cameraYaw = ModUtil.randomYawPitch();
        this.cameraPitch = ModUtil.randomYawPitch();

        x = CoordinatesDisplay.CONFIG.get().hudX;
        y = CoordinatesDisplay.CONFIG.get().hudY;
    }

    @Override
    public void renderBackground(PoseStack matrices) {
        super.renderBackground(matrices);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, new ResourceLocation("coordinatesdisplay", "textures/background/change_position_bg.png"));

        double a = 1.4;
        int b = (int) (this.width * a);

        blit(matrices, 0, 0, 0.0F, 0.0F, b, ModUtil.aspectRatio(16, 9, b), b, ModUtil.aspectRatio(16, 9, this.width));
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);

        matrices.pushPose();

        matrices.scale(0.8F, 0.8F, 0.8F);

        fill(matrices, 0,0, (int) (this.width / 0.8), 35, ModUtil.TRANSPARENT_GRAY);

        drawCenteredString(matrices, this.font, Component.translatable("screen.coordinatesdisplay.config.position").getString(), (int) ((this.width / 2) / 0.8), 5, ModUtil.WHITE);
        drawCenteredString(matrices, this.font, Component.translatable("description.coordinatesdisplay.changeposition").getString(), (int) ((this.width / 2) / 0.9), 20, ModUtil.GRAY);

        matrices.popPose();

        fill(matrices, 0, this.height - 20, this.width, this.height, ModUtil.TRANSPARENT_GRAY);

        drawCenteredString(matrices, this.font, Component.translatable("description.coordinatesdisplay.changeposition2").getString(), this.width / 2, this.height - 15, ModUtil.WHITE);

        if (lockHudPos) {
            x = mouseX;
            y = mouseY;
        }

        CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, cameraPitch, null, CoordinatesDisplay.CONFIG.get().minMode, x, y);

    }

    @Override
    public void init() {
        super.init();

        this.addRenderableWidget(new InvisibleButtonWidget(0, 0, this.width, this.height, (button -> lockHudPos = !lockHudPos)));

    }

    @Override
    public void onClose() {
        CoordinatesDisplay.CONFIG.get().hudX = x;
        CoordinatesDisplay.CONFIG.get().hudY = y;

        this.client.setScreen(parent);
    }
}