package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.ChunkPos;
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

    Vector3d pos;
    ChunkPos chunkPos;
    float cameraYaw;

    public RenderConfigScreen(Screen parent) {
        super(new TranslatableComponent("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion().thisVersion()));
        this.parent = parent;

        this.pos = new Vector3d(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos(new BlockPos(pos.x, pos.y, pos.z));
        this.cameraYaw = ModUtils.randomYaw();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredString(matrices, this.font, new TranslatableComponent("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion().thisVersion()).getString(), this.width / 2, 5, ModUtils.WHITE);

        CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, null, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), (int) (this.height / 2.1));

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight, new TranslatableComponent("button.coordinatesdisplay.back"), (button) -> this.minecraft.setScreen(parent)));

        initButtons();
    }

    private void initButtons() {
        // background
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, new TranslatableComponent("button.coordinatesdisplay.render.background", (CoordinatesDisplay.CONFIG.get().renderBackground ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderBackground = !CoordinatesDisplay.CONFIG.get().renderBackground;
            button.setMessage(new TranslatableComponent("button.coordinatesdisplay.render.background", (CoordinatesDisplay.CONFIG.get().renderBackground ? ModUtils.TRUE : ModUtils.FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, new TranslatableComponent("description.coordinatesdisplay.render.background"), mouseX, mouseY);
            }
        }));

        // chunk data
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight, new TranslatableComponent("button.coordinatesdisplay.render.chunkdata", (CoordinatesDisplay.CONFIG.get().renderChunkData ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderChunkData = !CoordinatesDisplay.CONFIG.get().renderChunkData;
            button.setMessage(new TranslatableComponent("button.coordinatesdisplay.render.chunkdata", (CoordinatesDisplay.CONFIG.get().renderChunkData ? ModUtils.TRUE : ModUtils.FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, new TranslatableComponent("description.coordinatesdisplay.render.chunkdata"), mouseX, mouseY);
            }
        }));

        // direction
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight, new TranslatableComponent("button.coordinatesdisplay.render.direction", (CoordinatesDisplay.CONFIG.get().renderDirection ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderDirection = !CoordinatesDisplay.CONFIG.get().renderDirection;
            button.setMessage(new TranslatableComponent("button.coordinatesdisplay.render.direction", (CoordinatesDisplay.CONFIG.get().renderDirection ? ModUtils.TRUE : ModUtils.FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, new TranslatableComponent("description.coordinatesdisplay.render.direction"), mouseX, mouseY);
            }
        }));

        // biome
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 3, largeButtonW, buttonHeight, new TranslatableComponent("button.coordinatesdisplay.render.biome", (CoordinatesDisplay.CONFIG.get().renderBiome ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().renderBiome = !CoordinatesDisplay.CONFIG.get().renderBiome;
            button.setMessage(new TranslatableComponent("button.coordinatesdisplay.render.biome", (CoordinatesDisplay.CONFIG.get().renderBiome ? ModUtils.TRUE : ModUtils.FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, new TranslatableComponent("description.coordinatesdisplay.render.biome"), mouseX, mouseY);
            }
        }));

        // open wiki
        this.addRenderableWidget(new Button(5, 5, tinyButtonW, buttonHeight, new TranslatableComponent("button.coordinatesdisplay.help"), (button) -> this.minecraft.setScreen(new ConfirmLinkScreen((yes) -> {
            this.minecraft.setScreen(this);
            if (yes) {
                Util.getPlatform().openUri(ModUtils.CONFIG_WIKI_RENDER);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtils.CONFIG_WIKI_RENDER, false))));
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}