package me.boxadactle.coordinatesdisplay.gui;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ColorConfigScreen extends Screen {
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

    public ColorConfigScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION));
        this.parent = parent;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, Text.translatable("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION), this.width / 2, 5, white);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.back"), (button) -> this.client.setScreen(parent)));

        initButtons();
    }

    private void initButtons() {
        String keyColor = CoordinatesDisplay.getColorIndex(CoordinatesDisplay.CONFIG.definitionColor) > 0 ?
                CoordinatesDisplay.colors[CoordinatesDisplay.getColorIndex(CoordinatesDisplay.CONFIG.definitionColor)] : "white";
        String valueColor = CoordinatesDisplay.getColorIndex(CoordinatesDisplay.CONFIG.dataColor) > 0 ?
                CoordinatesDisplay.colors[CoordinatesDisplay.getColorIndex(CoordinatesDisplay.CONFIG.dataColor)] : "white";
        String deathPosColor = CoordinatesDisplay.getColorIndex(CoordinatesDisplay.CONFIG.deathPosColor) > 0 ?
                CoordinatesDisplay.colors[CoordinatesDisplay.getColorIndex(CoordinatesDisplay.CONFIG.deathPosColor)] : "white";

        String keyPrefix = CoordinatesDisplay.getColorPrefix(keyColor);
        String valuePrefix = CoordinatesDisplay.getColorPrefix(valueColor);
        String deathPosPrefix = CoordinatesDisplay.getColorPrefix(deathPosColor);

        // lambdas are annoying
        final int[] indexes = {CoordinatesDisplay.getColorIndex(keyColor), CoordinatesDisplay.getColorIndex(valueColor), CoordinatesDisplay.getColorIndex(deathPosColor)};

        // keys
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.colors.keys", keyPrefix + keyColor), (button) -> {
            if (indexes[0] == CoordinatesDisplay.colors.length - 1) indexes[0] = 0;
            else indexes[0]++;

            CoordinatesDisplay.CONFIG.definitionColor = CoordinatesDisplay.colors[indexes[0]];
            CoordinatesDisplay.parseColorPrefixes();

            String newColor = CoordinatesDisplay.colors[indexes[0]];
            String newPrefix = CoordinatesDisplay.getColorPrefix(newColor);

            button.setMessage(Text.translatable("button.coordinatesdisplay.colors.keys", newPrefix + newColor));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.colors.key"), mouseX, mouseY);
            }
        }));

        // values
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.colors.values", valuePrefix + valueColor), (button) -> {
            if (indexes[1] == CoordinatesDisplay.colors.length - 1) indexes[1] = 0;
            else indexes[1]++;

            CoordinatesDisplay.CONFIG.dataColor = CoordinatesDisplay.colors[indexes[1]];
            CoordinatesDisplay.parseColorPrefixes();

            String newColor = CoordinatesDisplay.colors[indexes[1]];
            String newPrefix = CoordinatesDisplay.getColorPrefix(newColor);

            button.setMessage(Text.translatable("button.coordinatesdisplay.colors.values", newPrefix + newColor));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.colors.value"), mouseX, mouseY);
            }
        }));

        // death pos
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.colors.deathpos", deathPosPrefix + deathPosColor), (button) -> {
            if (indexes[2] == CoordinatesDisplay.colors.length - 1) indexes[2] = 0;
            else indexes[2]++;

            CoordinatesDisplay.CONFIG.deathPosColor = CoordinatesDisplay.colors[indexes[2]];
            CoordinatesDisplay.parseColorPrefixes();

            String newColor = CoordinatesDisplay.colors[indexes[2]];
            String newPrefix = CoordinatesDisplay.getColorPrefix(newColor);

            button.setMessage(Text.translatable("button.coordinatesdisplay.colors.deathpos", newPrefix + newColor));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.colors.deathpos"), mouseX, mouseY);
            }
        }));

    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
