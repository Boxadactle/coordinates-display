package me.boxadactle.coordinatesdisplay.gui;

import io.github.cottonmc.cotton.config.ConfigManager;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import me.boxadactle.coordinatesdisplay.gui.config.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class ConfigScreen extends Screen {
    int p = 2;
    int p1 = p / 2;

    int largeButtonW = 300;
    int smallButtonW = 150 - p;
    int buttonHeight = 20;

    int start = 20;

    Screen parent;

    String version;

    public ConfigScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
        this.parent = parent;

        ModUtils.initText();

        version = ModVersion.getVersion();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredTextWithShadow(matrices, this.textRenderer, Text.translatable("screen.coordinatesdisplay.config", CoordinatesDisplay.MOD_NAME, version), this.width / 2, 5, ModUtils.WHITE);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        initButtons();
        initButtonsOpen();
        initButtonsExit();
    }

    private void initButtons() {
        this.addDrawableChild(new PressableTextWidget(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.visual"), (button) -> this.client.setScreen(new VisualConfigScreen(this)), MinecraftClient.getInstance().textRenderer));

        this.addDrawableChild(new PressableTextWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.render"), (button) -> this.client.setScreen(new RenderConfigScreen(this)), MinecraftClient.getInstance().textRenderer));

        this.addDrawableChild(new PressableTextWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.color"), (button) -> this.client.setScreen(new ColorConfigScreen(this)), MinecraftClient.getInstance().textRenderer));

        this.addDrawableChild(new PressableTextWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 3, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.deathpos"), (button) -> this.client.setScreen(new DeathPosConfigScreen(this)), MinecraftClient.getInstance().textRenderer));

        this.addDrawableChild(new PressableTextWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 4, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.text"), (button) -> this.client.setScreen(new TextConfigScreen(this)), MinecraftClient.getInstance().textRenderer));
    }

    private void initButtonsOpen() {
        // open config file
        this.addDrawableChild(new PressableTextWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 6, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.configfile"), (button) -> {
            button.active = false;
            if (ModUtils.openConfigFile()) {
                button.setMessage(Text.translatable("message.coordinatesdisplay.openfilesuccess"));
            } else {
                button.setMessage(Text.translatable("message.coordinatesdisplay.openfilefailed"));
            }
        }, MinecraftClient.getInstance().textRenderer));

        // reset to default
        this.addDrawableChild(new PressableTextWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 7, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.resetconf"), (button) -> this.client.setScreen(new ConfirmScreen((doIt) -> {
            if (doIt) {
                ModUtils.resetConfig();
                this.client.setScreen(new ConfigScreen(parent));
            } else {
                this.client.setScreen(this);
            }
        }, Text.translatable("screen.coordinatesdisplay.confirmreset"), Text.translatable("message.coordinatesdisplay.confirmreset"))), MinecraftClient.getInstance().textRenderer));

        // open wiki
        this.addDrawableChild(new PressableTextWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 8, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.wiki"), (button) -> this.client.setScreen(new ConfirmLinkScreen((yes) -> {
            this.client.setScreen(this);
            if (yes) {
                Util.getOperatingSystem().open(ModUtils.CONFIG_WIKI);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtils.CONFIG_WIKI, false)), MinecraftClient.getInstance().textRenderer));
    }

    private void initButtonsExit() {
        // cancel
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.cancel"), (button -> {
            this.close();
            CoordinatesDisplay.LOGGER.info("Cancel pressed so reloading config");
            CoordinatesDisplay.reloadConfig();
        })).dimensions(this.width / 2 - smallButtonW - p1, this.height - buttonHeight - p, smallButtonW, buttonHeight).build());

        // save and exit
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.save"), (button -> {
            this.close();
            ConfigManager.saveConfig(CoordinatesDisplay.CONFIG);
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
