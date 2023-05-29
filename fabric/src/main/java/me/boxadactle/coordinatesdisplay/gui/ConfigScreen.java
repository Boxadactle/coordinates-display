package me.boxadactle.coordinatesdisplay.gui;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import me.boxadactle.coordinatesdisplay.gui.config.*;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class ConfigScreen extends Screen {
    int p = 2;
    int p1 = p / 2;
    int start = 20;

    Screen parent;

    int largeButtonW = 300;
    int smallButtonW = largeButtonW / 2 - p;
    int buttonHeight = 20;


    public ConfigScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
        this.parent = parent;

        ModUtil.initText();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredTextWithShadow(matrices, this.textRenderer, Text.translatable("screen.coordinatesdisplay.config", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()), this.width / 2, 5, ModUtil.WHITE);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        initSectionButtons();
        initButtonsOpen();
        initButtonsExit();
    }

    private void initSectionButtons() {
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.visual"), (button) -> this.client.setScreen(new VisualScreen(this))).dimensions(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight).build());

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.render"), (button) -> this.client.setScreen(new RenderScreen(this))).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight).build());

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.color"), (button) -> this.client.setScreen(new ColorScreen(this))).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight).build());

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.deathpos"), (button) -> this.client.setScreen(new DeathPosScreen(this))).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 3, largeButtonW, buttonHeight).build());

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.text"), (button) -> this.client.setScreen(new TextScreen(this))).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 4, largeButtonW, buttonHeight).build());
    }

    private void initButtonsOpen() {
        // open config file
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.configfile"), (button) -> {
            button.active = false;
            if (ModUtil.openConfigFile()) {
                button.setMessage(Text.translatable("message.coordinatesdisplay.openfilesuccess"));
            } else {
                button.setMessage(Text.translatable("message.coordinatesdisplay.openfilefailed"));
            }
        }).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 6, largeButtonW, buttonHeight).build());

        // reset to default
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.resetconf"), (button) -> this.client.setScreen(new ConfirmScreen((doIt) -> {
            if (doIt) {
                CoordinatesDisplay.resetConfig();
                this.client.setScreen(new ConfigScreen(parent));
            } else {
                this.client.setScreen(this);
            }
        }, Text.translatable("screen.coordinatesdisplay.confirmreset"), Text.translatable("message.coordinatesdisplay.confirmreset")))).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 7, largeButtonW, buttonHeight).build());

        // open wiki
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.wiki"), (button) -> this.client.setScreen(new ConfirmLinkScreen((yes) -> {
            this.client.setScreen(this);
            if (yes) {
                Util.getOperatingSystem().open(ModUtil.CONFIG_WIKI);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtil.CONFIG_WIKI, false))).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 8, largeButtonW, buttonHeight).build());
    }

    private void initButtonsExit() {
        // cancel
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.cancel"), (button -> {
            this.close();
            CoordinatesDisplay.LOGGER.info("Cancel pressed so reloading config");
            CoordinatesDisplay.resetConfig();
        })).dimensions(this.width / 2 - smallButtonW - p1, this.height - buttonHeight - p, smallButtonW, buttonHeight).build());

        // save and exit
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.save"), (button -> {
            this.close();
            CoordinatesDisplay.CONFIG.save();
            CoordinatesDisplay.LOGGER.info("Save pressed so saving config");
        })).dimensions(this.width / 2 + p1, this.height - buttonHeight - p, smallButtonW, buttonHeight).build());
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
