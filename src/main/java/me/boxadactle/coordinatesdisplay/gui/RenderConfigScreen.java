package me.boxadactle.coordinatesdisplay.gui;

import io.github.cottonmc.cotton.config.ConfigManager;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

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

    int white = 16777215;

    String TRUE = "§a" + Text.translatable("coordinatesdisplay.true").getString();
    String FALSE = "§c" + Text.translatable("coordinatesdisplay.false").getString();

    public RenderConfigScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION));
        this.parent = parent;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, Text.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION), this.width / 2, 5, white);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.back"), (button) -> this.client.setScreen(parent)));

        initButtons();
    }

    private void initButtons() {
        // background
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.render.background", (CoordinatesDisplay.CONFIG.renderBackground ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderBackground = !CoordinatesDisplay.CONFIG.renderBackground;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.background", (CoordinatesDisplay.CONFIG.renderBackground ? TRUE : FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.render.background"), mouseX, mouseY);
            }
        }));

        // chunk data
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.render.chunkdata", (CoordinatesDisplay.CONFIG.renderChunkData ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderChunkData = !CoordinatesDisplay.CONFIG.renderChunkData;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.chunkdata", (CoordinatesDisplay.CONFIG.renderChunkData ? TRUE : FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.render.chunkdata"), mouseX, mouseY);
            }
        }));

        // direction
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.render.direction", (CoordinatesDisplay.CONFIG.renderDirection ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderDirection = !CoordinatesDisplay.CONFIG.renderDirection;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.direction", (CoordinatesDisplay.CONFIG.renderDirection ? TRUE : FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.render.direction"), mouseX, mouseY);
            }
        }));

        // biome
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 3, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.render.biome", (CoordinatesDisplay.CONFIG.renderBiome ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderBiome = !CoordinatesDisplay.CONFIG.renderBiome;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.biome", (CoordinatesDisplay.CONFIG.renderBiome ? TRUE : FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.render.biome"), mouseX, mouseY);
            }
        }));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
