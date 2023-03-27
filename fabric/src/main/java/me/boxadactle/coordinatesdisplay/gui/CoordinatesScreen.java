package me.boxadactle.coordinatesdisplay.gui;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class CoordinatesScreen extends Screen {

    int x;
    int y;
    int z;

    int white = 16777215;

    int buttonw = 200;
    int buttonh = 20;

    int p = 5;

    public CoordinatesScreen(int x, int y, int z) {
        super(Text.literal("Coordinates Screen"));

        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredTextWithShadow(matrices, this.textRenderer, Text.translatable("message.coordinatesdisplay.at"), this.width / 2, (this.height / 4) - 20, white);
        drawCenteredTextWithShadow(matrices, this.textRenderer, Text.translatable("message.coordinatesdisplay.location", x, y, z), this.width / 2, (this.height / 4), white);

        super.render(matrices, mouseX, mouseY, delta);
    }

    private void resume() {
        this.client.setScreen(null);
        this.client.mouse.lockCursor();
    }

    @Override
    public void init() {
        super.init();

        int bstart = this.height / 2 - 20;

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.copy"), button -> {
            this.client.keyboard.setClipboard(ModUtil.parseText(CoordinatesDisplay.CONFIG.copyPosMessage));
            CoordinatesDisplay.LOGGER.player.info("Copied coordinates to clipboard");
            resume();
        }).dimensions(this.width / 2 - buttonw / 2, bstart, buttonw, buttonh).build());

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.send"), button -> {
            CoordinatesDisplay.LOGGER.player.publicChat(ModUtil.parseText(CoordinatesDisplay.CONFIG.posChatMessage));
            CoordinatesDisplay.LOGGER.player.info("Put Coordinates in Chat");
            resume();
        }).dimensions(this.width / 2 - buttonw / 2, bstart + (buttonh + p), buttonw, buttonh).build());

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.copytp"), button -> {
            this.client.keyboard.setClipboard(ModUtil.asTpCommand(x, y, z, ModUtil.getPlayerCurrentDimension()));
            CoordinatesDisplay.LOGGER.player.info("Copied as TP command");
            resume();
        }).dimensions(this.width / 2 - buttonw / 2, bstart + (buttonh + p) * 2, buttonw, buttonh).build());
    }

    @Override
    public void close() {
        this.client.setScreen(null);
    }
}
