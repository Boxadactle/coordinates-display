package me.boxadactle.coordinatesdisplay.gui.config;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

public class RenderConfigScreen extends Screen {
    int p = 2;
    int p1 = p / 2;
    int th = 10;
    int tp = 4;

    int largeButtonW = 300;
    int smallButtonW = 150 - p;
    int tinyButtonW = 75;
    int buttonHeight = 20;

    int start = 20;

    Screen parent;

    Vec3d pos;
    ChunkPos chunkPos;
    float cameraYaw;

    String version;

    public RenderConfigScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
        this.parent = parent;

        this.pos = new Vec3d(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos(new BlockPos((int)pos.getX(), (int)pos.getY(), (int)pos.getZ()));
        this.cameraYaw  = (float) Math.random() * 180;

        version = ModVersion.getVersion();

        CoordinatesDisplay.shouldRenderOnHud = false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredTextWithShadow(matrices, this.textRenderer, Text.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, version), this.width / 2, 5, ModUtils.WHITE);

        CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, null, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), (int) (this.height / 2.1));

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.back"), (button) -> this.close()).dimensions(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight).build());

        // open wiki
        this.addDrawableChild(new PressableTextWidget(5, 5, tinyButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.help"), (button) -> this.client.setScreen(new ConfirmLinkScreen((yes) -> {
            this.client.setScreen(this);
            if (yes) {
                Util.getOperatingSystem().open(ModUtils.CONFIG_WIKI_RENDER);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtils.CONFIG_WIKI_RENDER, false)), MinecraftClient.getInstance().textRenderer));

        initButtons();
    }

    private void initButtons() {
        // background
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.render.background", (CoordinatesDisplay.CONFIG.renderBackground ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderBackground = !CoordinatesDisplay.CONFIG.renderBackground;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.background", (CoordinatesDisplay.CONFIG.renderBackground ? ModUtils.TRUE : ModUtils.FALSE)));
            CoordinatesDisplay.OVERLAY.updateConfig(CoordinatesDisplay.CONFIG);
        }).dimensions(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight).tooltip(Tooltip.of(Text.translatable("description.coordinatesdisplay.render.background"))).build());

        // chunk data
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.render.chunkdata", (CoordinatesDisplay.CONFIG.renderChunkData ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderChunkData = !CoordinatesDisplay.CONFIG.renderChunkData;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.chunkdata", (CoordinatesDisplay.CONFIG.renderChunkData ? ModUtils.TRUE : ModUtils.FALSE)));
            CoordinatesDisplay.OVERLAY.updateConfig(CoordinatesDisplay.CONFIG);
        }).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight).tooltip(Tooltip.of(Text.translatable("description.coordinatesdisplay.render.chunkdata"))).build());

        // direction
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.render.direction", (CoordinatesDisplay.CONFIG.renderDirection ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderDirection = !CoordinatesDisplay.CONFIG.renderDirection;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.direction", (CoordinatesDisplay.CONFIG.renderDirection ? ModUtils.TRUE : ModUtils.FALSE)));
            CoordinatesDisplay.OVERLAY.updateConfig(CoordinatesDisplay.CONFIG);
        }).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight).tooltip(Tooltip.of(Text.translatable("description.coordinatesdisplay.render.direction"))).build());

        // biome
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.render.biome", (CoordinatesDisplay.CONFIG.renderBiome ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderBiome = !CoordinatesDisplay.CONFIG.renderBiome;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.biome", (CoordinatesDisplay.CONFIG.renderBiome ? ModUtils.TRUE : ModUtils.FALSE)));
            CoordinatesDisplay.OVERLAY.updateConfig(CoordinatesDisplay.CONFIG);
        }).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 3, largeButtonW, buttonHeight).tooltip(Tooltip.of(Text.translatable("description.coordinatesdisplay.render.biome"))).build());
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
        CoordinatesDisplay.shouldRenderOnHud = true;
    }
}
