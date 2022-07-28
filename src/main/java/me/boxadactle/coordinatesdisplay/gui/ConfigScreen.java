package me.boxadactle.coordinatesdisplay.gui;

import io.github.cottonmc.cotton.config.ConfigManager;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
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

    String TRUE = "§atrue";
    String FALSE = "§cfalse";

    public ConfigScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION));
        this.parent = parent;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, Text.translatable("screen.coordinatesdisplay.config", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION), this.width / 2, 5, white);

        drawCenteredText(matrices, this.textRenderer, Text.translatable("label.coordinatesdisplay.render"), this.width / 2, start + (buttonHeight + p) * 2 + p, white);

        drawCenteredText(matrices, this.textRenderer, Text.translatable("label.coordinatesdisplay.color"), this.width / 2, start + (buttonHeight + p) * 4  + (th + tp) + p, white);

        drawCenteredText(matrices, this.textRenderer, Text.translatable("label.coordinatesdisplay.other"), this.width / 2,  start + (buttonHeight + p) * 5  + (th + tp) * 2 + p, white);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        // visible button
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.visible", CoordinatesDisplay.CONFIG.visible ? TRUE : FALSE), (button) -> {
            CoordinatesDisplay.CONFIG.visible = !CoordinatesDisplay.CONFIG.visible;
            button.setMessage(Text.translatable("button.coordinatesdisplay.visible", CoordinatesDisplay.CONFIG.visible ? TRUE : FALSE));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.visible"), mouseX, mouseY);
            }
        }));

        // decimal rounding button
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + buttonHeight + p, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.decimal", (CoordinatesDisplay.CONFIG.visible ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.roundPosToTwoDecimals = !CoordinatesDisplay.CONFIG.roundPosToTwoDecimals;
            button.setMessage(Text.translatable("button.coordinatesdisplay.decimal", (CoordinatesDisplay.CONFIG.roundPosToTwoDecimals ? TRUE : FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.decimal"), mouseX, mouseY);
            }
        }));

        initButtonsRender();
        initButtonsColor();
        initButtonsOther();
        initButtonsExit();
    }

    private void initButtonsRender() {
        // background
        this.addDrawableChild(new ButtonWidget(this.width / 2 - smallButtonW - p1, start + (buttonHeight + p) * 2 + th + tp, smallButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.render.background", (CoordinatesDisplay.CONFIG.renderBackground ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderBackground = !CoordinatesDisplay.CONFIG.renderBackground;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.background", (CoordinatesDisplay.CONFIG.renderBackground ? TRUE : FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.render.background"), mouseX, mouseY);
            }
        }));

        // chunk data
        this.addDrawableChild(new ButtonWidget(this.width / 2 + p1, start + (buttonHeight + p) * 2 + th + tp, smallButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.render.chunkdata", (CoordinatesDisplay.CONFIG.renderChunkData ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderChunkData = !CoordinatesDisplay.CONFIG.renderChunkData;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.chunkdata", (CoordinatesDisplay.CONFIG.renderChunkData ? TRUE : FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.render.chunkdata"), mouseX, mouseY);
            }
        }));

        // direction
        this.addDrawableChild(new ButtonWidget(this.width / 2 - smallButtonW - p1, start + (buttonHeight + p) * 3 + th + tp, smallButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.render.direction", (CoordinatesDisplay.CONFIG.renderDirection ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderDirection = !CoordinatesDisplay.CONFIG.renderDirection;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.direction", (CoordinatesDisplay.CONFIG.renderDirection ? TRUE : FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.render.direction"), mouseX, mouseY);
            }
        }));

        // biome
        this.addDrawableChild(new ButtonWidget(this.width / 2 + p1, start + (buttonHeight + p) * 3 + th + tp, smallButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.render.biome", (CoordinatesDisplay.CONFIG.renderBiome ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.renderBiome = !CoordinatesDisplay.CONFIG.renderBiome;
            button.setMessage(Text.translatable("button.coordinatesdisplay.render.biome", (CoordinatesDisplay.CONFIG.renderBiome ? TRUE : FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.render.biome"), mouseX, mouseY);
            }
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
        this.addDrawableChild(new ButtonWidget(this.width / 2 - smallButtonW - p1, start + (buttonHeight + p) * 4 + (th + tp) * 2, smallButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.colors.keys", keyPrefix + keyColor), (button) -> {
            if (indexes[0] == CoordinatesDisplay.colors.length - 1) indexes[0] = 0;
            else indexes[0]++;

            CoordinatesDisplay.CONFIG.definitionColor = CoordinatesDisplay.colors[indexes[0]];
            CoordinatesDisplay.parseColorPrefixes();

            String newColor = CoordinatesDisplay.colors[indexes[0]];
            String newPrefix = CoordinatesDisplay.getColorPrefix(CoordinatesDisplay.colors[indexes[0]]);

            button.setMessage(Text.translatable("button.coordinatesdisplay.colors.keys", newPrefix + newColor));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.colors.key"), mouseX, mouseY);
            }
        }));

        // values
        this.addDrawableChild(new ButtonWidget(this.width / 2 + p1, start + (buttonHeight + p) * 4 + (th + tp) * 2, smallButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.colors.values", valuePrefix + valueColor), (button) -> {
            if (indexes[1] == CoordinatesDisplay.colors.length - 1) indexes[1] = 0;
            else indexes[1]++;

            CoordinatesDisplay.CONFIG.dataColor = CoordinatesDisplay.colors[indexes[1]];
            CoordinatesDisplay.parseColorPrefixes();

            String newColor = CoordinatesDisplay.colors[indexes[1]];
            String newPrefix = CoordinatesDisplay.getColorPrefix(CoordinatesDisplay.colors[indexes[1]]);

            button.setMessage(Text.translatable("button.coordinatesdisplay.colors.values", newPrefix + newColor));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.colors.value"), mouseX, mouseY);
            }
        }));
    }

    private void initButtonsOther() {
        // open config file
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 5 + (th + tp) * 3, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.configfile"), (button) -> {
            if (CoordinatesDisplay.openConfigFile()) {
                button.setMessage(Text.translatable("message.coordinatesdisplay.openfilesuccess"));
                button.active = false;
            } else {
                button.active = false;
                button.setMessage(Text.translatable("message.coordinatesdisplay.openfilefailed"));
            }
        }));

        // reset to default
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 6 + (th + tp) * 3, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.resetconf"), (button) -> this.client.setScreen(new ConfirmScreen((doIt) -> {
            if (doIt) {
                CoordinatesDisplay.resetConfig();
                this.client.setScreen(new ConfigScreen(parent));
            } else {
                this.client.setScreen(this);
            }
        }, Text.translatable("screen.coordinatesdisplay.confirmreset"), Text.translatable("message.coordinatesdisplay.confirmreset")))));
    }

    private void initButtonsExit() {
        // cancel
        this.addDrawableChild(new ButtonWidget(this.width / 2 - smallButtonW - p1, this.height - buttonHeight - p, smallButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.cancel"), (button -> {
            this.client.setScreen(parent);
            CoordinatesDisplay.LOGGER.info("Cancel pressed so reloading config");
            CoordinatesDisplay.reloadConfig();
        })));

        // save and exit
        this.addDrawableChild(new ButtonWidget(this.width / 2 + p1, this.height - buttonHeight - p, smallButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.save"), (button -> {
            this.client.setScreen(parent);
            ConfigManager.saveConfig(CoordinatesDisplay.CONFIG);
            CoordinatesDisplay.LOGGER.info("Save pressed so saving config");
        })));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
