package me.boxadactle.coordinatesdisplay.gui.config;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.text.DecimalFormat;

public class DeathPosScreen extends Screen {
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

    public DeathPosScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config.deathpos", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
        this.parent = parent;

        DecimalFormat d = new DecimalFormat("0.00");

        deathx = d.format(Math.random() * 1000);
        deathy = d.format(Math.random() * 100);
        deathz = d.format(Math.random() * 1000);

        CoordinatesDisplay.shouldRenderOnHud = false;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);

        drawContext.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("screen.coordinatesdisplay.config.deathpos", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()), this.width / 2, 5, ModUtil.WHITE);

        Text pos = Text.translatable("message.coordinatesdisplay.location", deathx, deathy, deathz).styled(style -> style.withColor(CoordinatesDisplay.CONFIG.get().deathPosColor));
        Text deathPos = Text.translatable("message.coordinatesdisplay.deathpos", pos).styled(style -> style.withColor(CoordinatesDisplay.CONFIG.get().definitionColor));
        drawContext.drawCenteredTextWithShadow(this.textRenderer, deathPos, this.width / 2, (int) (this.width / 1.5), ModUtil.WHITE);

        super.render(drawContext, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.back"), (button) -> this.close()).dimensions(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight).build());

        // open wiki
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.help"), (button) -> this.client.setScreen(new ConfirmLinkScreen((yes) -> {
            this.client.setScreen(this);
            if (yes) {
                Util.getOperatingSystem().open(ModUtil.CONFIG_WIKI_DEATH);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtil.CONFIG_WIKI_DEATH, false))).dimensions(5, 5, tinyButtonW, buttonHeight).build());

        initButtons();
    }

    private void initButtons() {
        // show death pos in chat
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.deathpos.deathscreen", (CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen = !CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen;
            button.setMessage(Text.translatable("button.coordinatesdisplay.deathpos.deathscreen", (CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen ? ModUtil.TRUE : ModUtil.FALSE)));
        }).dimensions(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight).build());

        // chunk data
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.deathpos.chat", (CoordinatesDisplay.CONFIG.get().showDeathPosInChat ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().showDeathPosInChat = !CoordinatesDisplay.CONFIG.get().showDeathPosInChat;
            button.setMessage(Text.translatable("button.coordinatesdisplay.deathpos.chat", (CoordinatesDisplay.CONFIG.get().showDeathPosInChat ? ModUtil.TRUE : ModUtil.FALSE)));
        }).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight).build());
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
        CoordinatesDisplay.shouldRenderOnHud = true;
    }
}
