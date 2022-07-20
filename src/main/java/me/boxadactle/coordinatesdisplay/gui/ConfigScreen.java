package me.boxadactle.coordinatesdisplay.gui;

import io.github.cottonmc.cotton.config.ConfigManager;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import javax.print.CancelablePrintJob;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigScreen extends Screen {
    int p = 2;
    int p1 = p / 2;
    int th = 10;
    int tp = 4;

    int largeButtonW = 300;
    int smallButtonW = 150 - p;
    int buttonHeight = 20;

    int start = 20;

    Screen parent;

    int white = 16777215;

    String TRUE = "§a true";
    String FALSE = "§c false";

    public ConfigScreen(Screen parent) {
        super(Text.of("Coordinates Display Config"));
        this.parent = parent;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, Text.of("Coordinates Display Configuration"), this.width / 2, 5, white);

        drawCenteredText(matrices, this.textRenderer, Text.of("Rendering"), this.width / 2, start + (buttonHeight + p) * 2 + p, white);

        drawCenteredText(matrices, this.textRenderer, Text.of("Colors"), this.width / 2, start + (buttonHeight + p) * 4  + (th + tp) + p, white);

        drawCenteredText(matrices, this.textRenderer, Text.of("Other"), this.width / 2,  start + (buttonHeight + p) * 5  + (th + tp) * 2 + p, white);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        // visible button
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, Text.of("Visible:" + (CoordinatesDisplay.CONFIG.visible ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.visible = !CoordinatesDisplay.CONFIG.visible;
            button.setMessage(Text.of("Visible:" + (CoordinatesDisplay.CONFIG.visible ? TRUE : FALSE)));
        }));

        // decimal rounding button
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + buttonHeight + p, largeButtonW, buttonHeight, Text.of("Decimal Rounding:" + (CoordinatesDisplay.CONFIG.visible ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.roundPosToTwoDecimals = !CoordinatesDisplay.CONFIG.roundPosToTwoDecimals;
            button.setMessage(Text.of("Decimal Rounding:" + (CoordinatesDisplay.CONFIG.roundPosToTwoDecimals ? TRUE : FALSE)));
        }));

        initButtonsRender();
        initButtonsColor();
        initButtonsOther();
        initButtonsExit();
    }

    private void initButtonsRender() {
        // background
        this.addDrawableChild(new ButtonWidget(this.width / 2 - smallButtonW - p1, start + (buttonHeight + p) * 2 + th + tp, smallButtonW, buttonHeight, Text.of("Background:" + (CoordinatesDisplay.CONFIG.renderBackground ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderBackground = !CoordinatesDisplay.CONFIG.renderBackground;
            button.setMessage(Text.of("Background:" + (CoordinatesDisplay.CONFIG.renderBackground ? TRUE : FALSE)));
        }));

        // chunk data
        this.addDrawableChild(new ButtonWidget(this.width / 2 + p1, start + (buttonHeight + p) * 2 + th + tp, smallButtonW, buttonHeight, Text.of("Chunk Data:" + (CoordinatesDisplay.CONFIG.renderChunkData ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderChunkData = !CoordinatesDisplay.CONFIG.renderChunkData;
            button.setMessage(Text.of("Chunk Data:" + (CoordinatesDisplay.CONFIG.renderChunkData ? TRUE : FALSE)));
        }));

        // direction
        this.addDrawableChild(new ButtonWidget(this.width / 2 - smallButtonW - p1, start + (buttonHeight + p) * 3 + th + tp, smallButtonW, buttonHeight, Text.of("Direction:" + (CoordinatesDisplay.CONFIG.renderDirection ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderDirection = !CoordinatesDisplay.CONFIG.renderDirection;
            button.setMessage(Text.of("Direction:" + (CoordinatesDisplay.CONFIG.renderDirection ? TRUE : FALSE)));
        }));

        // biome
        this.addDrawableChild(new ButtonWidget(this.width / 2 + p1, start + (buttonHeight + p) * 3 + th + tp, smallButtonW, buttonHeight, Text.of("Biome:" + (CoordinatesDisplay.CONFIG.renderBiome ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderBiome = !CoordinatesDisplay.CONFIG.renderBiome;
            button.setMessage(Text.of("Biome:" + (CoordinatesDisplay.CONFIG.renderBiome ? TRUE : FALSE)));
        }));
    }

    private void initButtonsColor() {
        String keyColor = CoordinatesDisplay.getColorIndex(CoordinatesDisplay.CONFIG.definitionColor) > 0 ?
                CoordinatesDisplay.colors[CoordinatesDisplay.getColorIndex(CoordinatesDisplay.CONFIG.definitionColor)] : "white";
        String valueColor = CoordinatesDisplay.getColorIndex(CoordinatesDisplay.CONFIG.dataColor) > 0 ?
                CoordinatesDisplay.colors[CoordinatesDisplay.getColorIndex(CoordinatesDisplay.CONFIG.dataColor)] : "white";

        String keyPrefix = CoordinatesDisplay.getColorPrefix(keyColor);
        String valuePrefix = CoordinatesDisplay.getColorPrefix(valueColor);

        // lambdas are annoying
        final int[] indexes = {CoordinatesDisplay.getColorIndex(keyColor), CoordinatesDisplay.getColorIndex(valueColor)};

        // keys
        this.addDrawableChild(new ButtonWidget(this.width / 2 - smallButtonW - p1, start + (buttonHeight + p) * 4 + (th + tp) * 2, smallButtonW, buttonHeight, Text.of("Keys: " + keyPrefix + keyColor), (button) -> {
            if (indexes[0] == CoordinatesDisplay.colors.length - 1) indexes[0] = 0;
            else indexes[0]++;

            CoordinatesDisplay.CONFIG.definitionColor = CoordinatesDisplay.colors[indexes[0]];
            CoordinatesDisplay.parseColorPrefixes();

            String newColor = CoordinatesDisplay.colors[indexes[0]];
            String newPrefix = CoordinatesDisplay.getColorPrefix(CoordinatesDisplay.colors[indexes[0]]);

            button.setMessage(Text.of("Keys: " + newPrefix + newColor));
        }));

        // values
        this.addDrawableChild(new ButtonWidget(this.width / 2 + p1, start + (buttonHeight + p) * 4 + (th + tp) * 2, smallButtonW, buttonHeight, Text.of("Values: " + valuePrefix + valueColor), (button) -> {
            if (indexes[1] == CoordinatesDisplay.colors.length - 1) indexes[1] = 0;
            else indexes[1]++;

            CoordinatesDisplay.CONFIG.dataColor = CoordinatesDisplay.colors[indexes[1]];
            CoordinatesDisplay.parseColorPrefixes();

            String newColor = CoordinatesDisplay.colors[indexes[1]];
            String newPrefix = CoordinatesDisplay.getColorPrefix(CoordinatesDisplay.colors[indexes[1]]);

            button.setMessage(Text.of("Values: " + newPrefix + newColor));
        }));
    }

    private void initButtonsOther() {
        // open config file
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 5 + (th + tp) * 3, largeButtonW, buttonHeight, Text.of("Show config file..."), (button) -> {
            if (CoordinatesDisplay.openConfigFile()) {
                button.setMessage(Text.of("Opened file in native explorer"));
                button.active = false;
            } else {
                button.active = false;
                button.setMessage(Text.of("Could not open file"));
            }
        }));

        // reset to default
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 6 + (th + tp) * 3, largeButtonW, buttonHeight, Text.of("Reset to default..."), (button) -> {
            MinecraftClient.getInstance().setScreen(new ConfirmScreen((doIt) -> {
                if (doIt) {
                    CoordinatesDisplay.resetConfig();
                    MinecraftClient.getInstance().setScreen(new ConfigScreen(parent));
                } else {
                    MinecraftClient.getInstance().setScreen(this);
                }
            }, Text.of("Confirm Reset to Default"), Text.of("Are you sure you want to reset all of your settings to default?")));
        }));
    }

    private void initButtonsExit() {
        // cancel
        this.addDrawableChild(new ButtonWidget(this.width / 2 - smallButtonW - p1, this.height - buttonHeight - p, smallButtonW, buttonHeight, Text.of("Cancel"), (button -> {
            MinecraftClient.getInstance().setScreen(parent);
            CoordinatesDisplay.LOGGER.info("Cancel pressed so reloading config");
            CoordinatesDisplay.reloadConfig();
        })));

        // save and exit
        this.addDrawableChild(new ButtonWidget(this.width / 2 + p1, this.height - buttonHeight - p, smallButtonW, buttonHeight, Text.of("Save & Exit"), (button -> {
            MinecraftClient.getInstance().setScreen(parent);
            ConfigManager.saveConfig(CoordinatesDisplay.CONFIG);
            CoordinatesDisplay.LOGGER.info("Save pressed so saving config");
        })));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
