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

    int largeButtonW = 300;
    int smallButtonW = 150 - p;
    int buttonHeight = 20;

    int start = 20;

    Screen parent;

    int white = 16777215;

    String TRUE = "§a" + Text.translatable("coordinatesdisplay.true").getString();
    String FALSE = "§c" + Text.translatable("coordinatesdisplay.false").getString();

    public ConfigScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION));
        this.parent = parent;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, Text.translatable("screen.coordinatesdisplay.config", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION), this.width / 2, 5, white);

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

        initButtons();
        initButtonsOpen();
        initButtonsExit();
    }

    private void initButtons() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.render"), (button) -> this.client.setScreen(new RenderConfigScreen(this)), (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.render"), mouseX, mouseY);
            }
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 3, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.color"), (button) -> this.client.setScreen(new ColorConfigScreen(this)), (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.colors"), mouseX, mouseY);
            }
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 4, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.deathpos"), (button) -> this.client.setScreen(new DeathPosConfigScreen(this)), (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.deathpos"), mouseX, mouseY);
            }
        }));
    }

    private void initButtonsOpen() {
        // open config file
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 6, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.configfile"), (button) -> {
            if (CoordinatesDisplay.openConfigFile()) {
                button.setMessage(Text.translatable("message.coordinatesdisplay.openfilesuccess"));
                button.active = false;
            } else {
                button.active = false;
                button.setMessage(Text.translatable("message.coordinatesdisplay.openfilefailed"));
            }
        }));

        // reset to default
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 7, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.resetconf"), (button) -> this.client.setScreen(new ConfirmScreen((doIt) -> {
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
