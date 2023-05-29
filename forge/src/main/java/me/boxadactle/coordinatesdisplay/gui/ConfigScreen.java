package me.boxadactle.coordinatesdisplay.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.gui.config.*;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends Screen {
    int p = 2;
    int p1 = p / 2;

    int largeButtonW = 300;
    int smallButtonW = 150 - p;
    int buttonHeight = 20;

    int start = 20;

    Screen parent;

    public ConfigScreen(Screen parent) {
        super(Component.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
        this.parent = parent;

        ModUtil.initText();
    }
    
    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredString(matrices, this.font, Component.translatable("screen.coordinatesdisplay.config", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()).getString(), this.width / 2, 5, ModUtil.WHITE);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        initButtons();
        initButtonsOpen();
        initButtonsExit();
    }

    private void initButtons() {
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.visual"), (button) -> this.minecraft.setScreen(new VisualConfigScreen(this))).bounds(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight).build());

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.render"), (button) -> this.minecraft.setScreen(new RenderConfigScreen(this))).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight).build());

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.color"), (button) -> this.minecraft.setScreen(new ColorConfigScreen(this))).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight).build());

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.deathpos"), (button) -> this.minecraft.setScreen(new DeathPosConfigScreen(this))).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 3, largeButtonW, buttonHeight).build());

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.text"), (button) -> this.minecraft.setScreen(new TextConfigScreen(this))).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 4, largeButtonW, buttonHeight).build());
    }

    private void initButtonsOpen() {
        // open config file
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.configfile"), (button) -> ModUtil.openConfigFile()).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 6, largeButtonW, buttonHeight).build());

        // reset to default
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.resetconf"), (button) -> this.minecraft.setScreen(new ConfirmScreen((doIt) -> {
            if (doIt) {
                ModUtil.resetConfig();
                this.minecraft.setScreen(new ConfigScreen(parent));
            } else {
                this.minecraft.setScreen(this);
            }
        }, Component.translatable("screen.coordinatesdisplay.confirmreset"), Component.translatable("message.coordinatesdisplay.confirmreset")))).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 7, largeButtonW, buttonHeight).build());

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.wiki"), (button) -> this.minecraft.setScreen(new ConfirmLinkScreen((yes) -> {
            this.minecraft.setScreen(this);
            if (yes) {
                Util.getPlatform().openUri(ModUtil.CONFIG_WIKI);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtil.CONFIG_WIKI, false))).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 8, largeButtonW, buttonHeight).build());
    }

    private void initButtonsExit() {
        // cancel
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.cancel"), (button -> {
            this.onClose();
            CoordinatesDisplay.LOGGER.info("Cancel pressed so reloading config");
            CoordinatesDisplay.CONFIG.load();
        })).bounds(this.width / 2 - smallButtonW - p1, this.height - buttonHeight - p, smallButtonW, buttonHeight).build());

        // save and exit
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.save"), (button -> {
            this.onClose();
            CoordinatesDisplay.CONFIG.save();
            CoordinatesDisplay.LOGGER.info("Save pressed so saving config");
        })).bounds(this.width / 2 + p1, this.height - buttonHeight - p, smallButtonW, buttonHeight).build());
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}