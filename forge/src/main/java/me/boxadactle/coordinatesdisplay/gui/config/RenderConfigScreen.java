package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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

    Vec3 pos;
    ChunkPos chunkPos;
    float cameraYaw;
    float cameraPitch;

    public RenderConfigScreen(Screen parent) {
        super(Component.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
        this.parent = parent;

        this.pos = new Vec3(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos(new BlockPos(ModUtil.doubleVecToIntVec(this.pos)));
        this.cameraYaw = ModUtil.randomYaw();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredString(matrices, this.font, Component.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()).getString(), this.width / 2, 5, ModUtil.WHITE);

        CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, cameraPitch, null, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), (int) ((this.height / 2.1) + 35), CoordinatesDisplay.CONFIG.get().minMode, false);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.back"), (button) -> this.minecraft.setScreen(parent)).bounds(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight).build());

        initButtons();
    }

    private void initButtons() {
        // background
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.render.background", (CoordinatesDisplay.CONFIG.get().renderBackground ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderBackground = !CoordinatesDisplay.CONFIG.get().renderBackground;
            button.setMessage(Component.translatable("button.coordinatesdisplay.render.background", (CoordinatesDisplay.CONFIG.get().renderBackground ? ModUtil.TRUE : ModUtil.FALSE)));
        }).bounds(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight).build());

        // chunk data
        Button chunk = new Button.Builder(Component.translatable("button.coordinatesdisplay.render.chunkdata", (CoordinatesDisplay.CONFIG.get().renderChunkData ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderChunkData = !CoordinatesDisplay.CONFIG.get().renderChunkData;
            button.setMessage(Component.translatable("button.coordinatesdisplay.render.chunkdata", (CoordinatesDisplay.CONFIG.get().renderChunkData ? ModUtil.TRUE : ModUtil.FALSE)));
        }).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight).build();
        chunk.active = !CoordinatesDisplay.CONFIG.get().minMode;
        this.addRenderableWidget(chunk);

        // direction
        Button direction = new Button.Builder(Component.translatable("button.coordinatesdisplay.render.direction", (CoordinatesDisplay.CONFIG.get().renderDirection ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderDirection = !CoordinatesDisplay.CONFIG.get().renderDirection;
            button.setMessage(Component.translatable("button.coordinatesdisplay.render.direction", (CoordinatesDisplay.CONFIG.get().renderDirection ? ModUtil.TRUE : ModUtil.FALSE)));
        }).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight).build();
        direction.active = !CoordinatesDisplay.CONFIG.get().minMode;
        this.addRenderableWidget(direction);

        // direction int
        Button directionint = new Button.Builder(Component.translatable("button.coordinatesdisplay.render.directionint", (CoordinatesDisplay.CONFIG.get().renderDirectionInt ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderDirectionInt = !CoordinatesDisplay.CONFIG.get().renderDirectionInt;
            button.setMessage(Component.translatable("button.coordinatesdisplay.render.directionint", (CoordinatesDisplay.CONFIG.get().renderDirectionInt ? ModUtil.TRUE : ModUtil.FALSE)));
        }).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 3, largeButtonW, buttonHeight).build();
        directionint.active = !CoordinatesDisplay.CONFIG.get().minMode;
        this.addRenderableWidget(directionint);

        // biome
        Button biome = new Button.Builder(Component.translatable("button.coordinatesdisplay.render.biome", (CoordinatesDisplay.CONFIG.get().renderBiome ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderBiome = !CoordinatesDisplay.CONFIG.get().renderBiome;
            button.setMessage(Component.translatable("button.coordinatesdisplay.render.biome", (CoordinatesDisplay.CONFIG.get().renderBiome ? ModUtil.TRUE : ModUtil.FALSE)));
        }).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 4, largeButtonW, buttonHeight).build();
        biome.active = !CoordinatesDisplay.CONFIG.get().minMode;
        this.addRenderableWidget(biome);

        // mc version
        Button version = new Button.Builder(Component.translatable("button.coordinatesdisplay.render.mcversion", (CoordinatesDisplay.CONFIG.get().renderMCVersion ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderMCVersion = !CoordinatesDisplay.CONFIG.get().renderMCVersion;
            button.setMessage(Component.translatable("button.coordinatesdisplay.render.mcversion", (CoordinatesDisplay.CONFIG.get().renderMCVersion ? ModUtil.TRUE : ModUtil.FALSE)));
        }).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 5, largeButtonW, buttonHeight).build();
        version.active = !CoordinatesDisplay.CONFIG.get().minMode;
        this.addRenderableWidget(version);

        // open wiki
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.help"), (button) -> this.minecraft.setScreen(new ConfirmLinkScreen((yes) -> {
            this.minecraft.setScreen(this);
            if (yes) {
                Util.getPlatform().openUri(ModUtil.CONFIG_WIKI_RENDER);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtil.CONFIG_WIKI_RENDER, false))).bounds(5, 5, tinyButtonW, buttonHeight).build());
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}