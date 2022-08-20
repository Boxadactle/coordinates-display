package me.boxadactle.coordinatesdisplay.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.gui.config.*;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
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
        super(Component.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion().thisVersion()));
        this.parent = parent;

        ModUtils.initText();
    }
    
    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredString(matrices, this.font, Component.translatable("screen.coordinatesdisplay.config", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion().thisVersion()).getString(), this.width / 2, 5, ModUtils.WHITE);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        initButtons();
        initButtonsOpen();
        initButtonsExit();
    }

    private void initButtons() {
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.visual"), (button) -> this.minecraft.setScreen(new VisualConfigScreen(this)), (button, matrices, mouseX, mouseY) -> {
            if (button.isHoveredOrFocused()) {
                this.renderTooltip(matrices, Component.translatable("description.coordinatesdisplay.visual"), mouseX, mouseY);
            }
        }));

        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.render"), (button) -> this.minecraft.setScreen(new RenderConfigScreen(this)), (button, matrices, mouseX, mouseY) -> {
            if (button.isHoveredOrFocused()) {
                this.renderTooltip(matrices, Component.translatable("description.coordinatesdisplay.render"), mouseX, mouseY);
            }
        }));

        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.color"), (button) -> this.minecraft.setScreen(new ColorConfigScreen(this)), (button, matrices, mouseX, mouseY) -> {
            if (button.isHoveredOrFocused()) {
                this.renderTooltip(matrices, Component.translatable("description.coordinatesdisplay.colors"), mouseX, mouseY);
            }
        }));

        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 3, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.deathpos"), (button) -> this.minecraft.setScreen(new DeathPosConfigScreen(this)), (button, matrices, mouseX, mouseY) -> {
            if (button.isHoveredOrFocused()) {
                this.renderTooltip(matrices, Component.translatable("description.coordinatesdisplay.deathpos"), mouseX, mouseY);
            }
        }));

        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 4, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.text"), (button) -> this.minecraft.setScreen(new TextConfigScreen(this)), (button, matrices, mouseX, mouseY) -> {
            if (button.isHoveredOrFocused()) {
                this.renderTooltip(matrices, Component.translatable("description.coordinatesdisplay.text"), mouseX, mouseY);
            }
        }));
    }

    private void initButtonsOpen() {
        // open config file
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 6, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.configfile"), (button) -> ModUtils.openConfigFile()));

        // reset to default
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 7, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.resetconf"), (button) -> this.minecraft.setScreen(new ConfirmScreen((doIt) -> {
            if (doIt) {
                ModUtils.resetConfig();
                this.minecraft.setScreen(new ConfigScreen(parent));
            } else {
                this.minecraft.setScreen(this);
            }
        }, Component.translatable("screen.coordinatesdisplay.confirmreset"), Component.translatable("message.coordinatesdisplay.confirmreset")))));

        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 8, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.wiki"), (button) -> this.minecraft.setScreen(new ConfirmLinkScreen((yes) -> {
            this.minecraft.setScreen(this);
            if (yes) {
                Util.getPlatform().openUri(ModUtils.CONFIG_WIKI);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtils.CONFIG_WIKI, false))));
    }

    private void initButtonsExit() {
        // cancel
        this.addRenderableWidget(new Button(this.width / 2 - smallButtonW - p1, this.height - buttonHeight - p, smallButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.cancel"), (button -> {
            this.onClose();
            CoordinatesDisplay.LOGGER.info("Cancel pressed so reloading config");
            CoordinatesDisplay.CONFIG.load();
        })));

        // save and exit
        this.addRenderableWidget(new Button(this.width / 2 + p1, this.height - buttonHeight - p, smallButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.save"), (button -> {
            this.onClose();
            CoordinatesDisplay.CONFIG.save();
            CoordinatesDisplay.LOGGER.info("Save pressed so saving config");
        })));
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