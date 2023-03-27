package me.boxadactle.coordinatesdisplay.gui.config;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class TextConfigScreen extends Screen {
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

    String version;

    public TextConfigScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config.text", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
        this.parent = parent;

        version = ModVersion.getVersion();

        CoordinatesDisplay.shouldRenderOnHud = false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredTextWithShadow(matrices, this.textRenderer, Text.translatable("screen.coordinatesdisplay.config.text", CoordinatesDisplay.MOD_NAME, version), this.width / 2, 5, ModUtil.WHITE);

        drawCenteredTextWithShadow(matrices, this.textRenderer, Text.translatable("button.coordinatesdisplay.poschatmessage"), this.width / 2, start, ModUtil.WHITE);

        drawCenteredTextWithShadow(matrices, this.textRenderer, Text.translatable("button.coordinatesdisplay.copyposmessage"), this.width / 2, start + (10 + p) + (buttonHeight + p), ModUtil.WHITE);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.back"), (button) -> this.close()).dimensions(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight).build());

        // open wiki
        this.addDrawableChild(new PressableTextWidget(5, 5, tinyButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.help"), (button) -> this.client.setScreen(new ConfirmLinkScreen((yes) -> {
            this.client.setScreen(this);
            if (yes) {
                Util.getOperatingSystem().open(ModUtil.CONFIG_WIKI_TEXTS);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtil.CONFIG_WIKI_TEXTS, false)), MinecraftClient.getInstance().textRenderer));

        TextFieldWidget posChatMessage = new TextFieldWidget(this.textRenderer, this.width / 2 - largeButtonW / 2, start + (10 + p), largeButtonW, buttonHeight, Text.of(CoordinatesDisplay.CONFIG.posChatMessage));
        posChatMessage.setChangedListener((message) -> CoordinatesDisplay.CONFIG.posChatMessage = message);
        posChatMessage.setMaxLength(50);
        posChatMessage.setText(CoordinatesDisplay.CONFIG.posChatMessage);

        TextFieldWidget copyPosMessage = new TextFieldWidget(this.textRenderer, this.width / 2 - largeButtonW / 2, start + (10 + p) * 2 + (buttonHeight + p), largeButtonW, buttonHeight, Text.of(CoordinatesDisplay.CONFIG.posChatMessage));
        copyPosMessage.setChangedListener((message) -> CoordinatesDisplay.CONFIG.copyPosMessage = message);
        copyPosMessage.setMaxLength(50);
        copyPosMessage.setText(CoordinatesDisplay.CONFIG.copyPosMessage);

        this.addDrawableChild(posChatMessage);
        this.addDrawableChild(copyPosMessage);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
        CoordinatesDisplay.shouldRenderOnHud = true;
    }
}
