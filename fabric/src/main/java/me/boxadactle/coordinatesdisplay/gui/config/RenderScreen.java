package me.boxadactle.coordinatesdisplay.gui.config;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

public class RenderScreen extends Screen {
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
    float cameraPitch;

    public RenderScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
        this.parent = parent;

        this.pos = new Vec3d(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos(new BlockPos(ModUtil.doubleVecToIntVec(this.pos)));
        this.cameraYaw  = (float) Math.random() * 180;
        this.cameraPitch  = (float) Math.random() * 180;

        CoordinatesDisplay.shouldRenderOnHud = false;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);

        drawContext.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()), this.width / 2, 5, ModUtil.WHITE);

        CoordinatesDisplay.OVERLAY.render(drawContext, pos, chunkPos, cameraYaw, cameraPitch, null, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), (int) (this.height / 2.1) + 35, CoordinatesDisplay.CONFIG.get().minMode, false);

        super.render(drawContext, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.back"), (button) -> this.close()).dimensions(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight).build());

        // open wiki
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.help"), (button) -> this.client.setScreen(new ConfirmLinkScreen((yes) -> {
            this.client.setScreen(this);
            if (yes) {
                Util.getOperatingSystem().open(ModUtil.CONFIG_WIKI_RENDER);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtil.CONFIG_WIKI_RENDER, false))).dimensions(5, 5, tinyButtonW, buttonHeight).build());

        initButtons();
    }

    private void initButtons() {
        // background
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.render.background", (CoordinatesDisplay.CONFIG.get().renderBackground ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderBackground = !CoordinatesDisplay.CONFIG.get().renderBackground;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.background", (CoordinatesDisplay.CONFIG.get().renderBackground ? ModUtil.TRUE : ModUtil.FALSE)));
        }).dimensions(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight).build());

        // chunk data
        ButtonWidget chunk = new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.render.chunkdata", (CoordinatesDisplay.CONFIG.get().renderChunkData ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderChunkData = !CoordinatesDisplay.CONFIG.get().renderChunkData;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.chunkdata", (CoordinatesDisplay.CONFIG.get().renderChunkData ? ModUtil.TRUE : ModUtil.FALSE)));
        }).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight).build();
        chunk.active = !CoordinatesDisplay.CONFIG.get().minMode;
        this.addDrawableChild(chunk);

        // direction
        ButtonWidget direction = new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.render.direction", (CoordinatesDisplay.CONFIG.get().renderDirection ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderDirection = !CoordinatesDisplay.CONFIG.get().renderDirection;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.direction", (CoordinatesDisplay.CONFIG.get().renderDirection ? ModUtil.TRUE : ModUtil.FALSE)));
        }).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight).build();
        direction.active = !CoordinatesDisplay.CONFIG.get().minMode;
        this.addDrawableChild(direction);

        // biome
        ButtonWidget biome = new ButtonWidget.Builder(  Text.translatable("button.coordinatesdisplay.render.biome", (CoordinatesDisplay.CONFIG.get().renderBiome ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderBiome = !CoordinatesDisplay.CONFIG.get().renderBiome;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.biome", (CoordinatesDisplay.CONFIG.get().renderBiome ? ModUtil.TRUE : ModUtil.FALSE)));
        }).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 3, largeButtonW, buttonHeight).build();
        biome.active = !CoordinatesDisplay.CONFIG.get().minMode;
        this.addDrawableChild(biome);

        // direction int
        ButtonWidget directionint = new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.render.directionint", (CoordinatesDisplay.CONFIG.get().renderDirectionInt ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderDirectionInt = !CoordinatesDisplay.CONFIG.get().renderDirectionInt;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.directionint", (CoordinatesDisplay.CONFIG.get().renderDirectionInt ? ModUtil.TRUE : ModUtil.FALSE)));
        }).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 4, largeButtonW, buttonHeight).build();
        directionint.active = !CoordinatesDisplay.CONFIG.get().minMode;
        this.addDrawableChild(directionint);

        // minecraft version
        ButtonWidget mcversion = new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.render.mcversion", (CoordinatesDisplay.CONFIG.get().renderMCVersion ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderMCVersion = !CoordinatesDisplay.CONFIG.get().renderMCVersion;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.mcversion", (CoordinatesDisplay.CONFIG.get().renderMCVersion ? ModUtil.TRUE : ModUtil.FALSE)));
        }).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 5, largeButtonW, buttonHeight).build();
        mcversion.active = !CoordinatesDisplay.CONFIG.get().minMode;
        this.addDrawableChild(mcversion);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
        CoordinatesDisplay.shouldRenderOnHud = true;
    }
}
