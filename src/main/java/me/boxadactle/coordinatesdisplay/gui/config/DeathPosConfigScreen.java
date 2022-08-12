package me.boxadactle.coordinatesdisplay.gui.config;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.gui.widget.InvisibleButtonWidget;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.text.DecimalFormat;

public class DeathPosConfigScreen extends Screen {
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

    String deathx;
    String deathy;
    String deathz;

    String dimension;

    public DeathPosConfigScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config.deathpos", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION));
        this.parent = parent;

        DecimalFormat d = new DecimalFormat("0.00");

        deathx = d.format(Math.random() * 1000);
        deathy = d.format(Math.random() * 100);
        deathz = d.format(Math.random() * 1000);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, Text.translatable("screen.coordinatesdisplay.config.deathpos", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION), this.width / 2, 5, ModUtils.WHITE);

        String pos = CoordinatesDisplay.DeathposColorPrefix + Text.translatable("message.coordinatesdisplay.location", deathx, deathy, deathz).getString();
        Text deathPos = Text.translatable("message.coordinatesdisplay.deathpos", pos);
        drawCenteredText(matrices, this.textRenderer, deathPos, this.width / 2, (int) (this.width / 1.5), ModUtils.WHITE);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.back"), (button) -> this.client.setScreen(parent)));

        // open wiki
        this.addDrawableChild(new ButtonWidget(5, 5, tinyButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.help"), (button) -> this.client.setScreen(new ConfirmLinkScreen((yes) -> {
            this.client.setScreen(this);
            if (yes) {
                Util.getOperatingSystem().open(ModUtils.CONFIG_WIKI_DEATH);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtils.CONFIG_WIKI_DEATH, false))));

        initButtons();
    }

    private void initButtons() {
        // show death pos in chat
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.deathpos.deathscreen", (CoordinatesDisplay.CONFIG.displayPosOnDeathScreen ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.displayPosOnDeathScreen = !CoordinatesDisplay.CONFIG.displayPosOnDeathScreen;
            button.setMessage(Text.translatable("button.coordinatesdisplay.deathpos.deathscreen", (CoordinatesDisplay.CONFIG.displayPosOnDeathScreen ? ModUtils.TRUE : ModUtils.FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.deathpos.deathscreen"), mouseX, mouseY);
            }
        }));

        // chunk data
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.deathpos.chat", (CoordinatesDisplay.CONFIG.showDeathPosInChat ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.showDeathPosInChat = !CoordinatesDisplay.CONFIG.showDeathPosInChat;
            button.setMessage(Text.translatable("button.coordinatesdisplay.deathpos.chat", (CoordinatesDisplay.CONFIG.showDeathPosInChat ? ModUtils.TRUE : ModUtils.FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.deathpos.chat"), mouseX, mouseY);
            }
        }));
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
