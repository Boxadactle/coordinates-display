package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.gui.widget.InvisibleButtonWidget;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChangePositionScreen extends Screen {

    private final Minecraft client = Minecraft.getInstance();

    Screen parent;

    Vector3d pos;
    ChunkPos chunkPos;
    float cameraYaw;

    boolean lockHudPos = false;

    int x;
    int y;

    public ChangePositionScreen(Screen parent) {
        super(new TranslatableComponent("screen.coordinatesdisplay.config.position"));
        this.parent = parent;

        this.pos = new Vector3d(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos(new BlockPos(pos.x, pos.y, pos.z));
        this.cameraYaw = ModUtils.randomYaw();

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

        blit(matrices, 0, 0, 0.0F, 0.0F, b, ModUtils.aspectRatio(16, 9, b), b, ModUtils.aspectRatio(16, 9, this.width));
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);

        matrices.pushPose();

        matrices.scale(0.8F, 0.8F, 0.8F);

        fill(matrices, 0,0, (int) (this.width / 0.8), 35, ModUtils.TRANSPARENT_GRAY);

        drawCenteredString(matrices, this.font, new TranslatableComponent("screen.coordinatesdisplay.config.position").getString(), (int) ((this.width / 2) / 0.8), 5, ModUtils.WHITE);
        drawCenteredString(matrices, this.font, new TranslatableComponent("description.coordinatesdisplay.changeposition").getString(), (int) ((this.width / 2) / 0.9), 20, ModUtils.GRAY);

        matrices.popPose();

        fill(matrices, 0, this.height - 20, this.width, this.height, ModUtils.TRANSPARENT_GRAY);

        drawCenteredString(matrices, this.font, new TranslatableComponent("description.coordinatesdisplay.changeposition2").getString(), this.width / 2, this.height - 15, ModUtils.WHITE);

        if (lockHudPos) {
            x = mouseX;
            y = mouseY;
        }

        CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, null, x, y);

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