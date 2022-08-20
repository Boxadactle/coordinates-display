package me.boxadactle.coordinatesdisplay.gui;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
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

        drawCenteredText(matrices, this.textRenderer, Text.translatable("message.coordinatesdisplay.at"), this.width / 2, (this.height / 4) - 20, white);
        drawCenteredText(matrices, this.textRenderer, Text.translatable("message.coordinatesdisplay.location", x, y, z), this.width / 2, (this.height / 4), white);

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

        this.addDrawableChild(new ButtonWidget(this.width / 2 - buttonw / 2, bstart, buttonw, buttonh, Text.translatable("button.coordinatesdisplay.copy"), button -> {
            this.client.keyboard.setClipboard(ModUtils.parseText(CoordinatesDisplay.CONFIG.copyPosMessage));
            CoordinatesDisplay.LOGGER.player.info("Copied coordinates to clipboard");
            resume();
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - buttonw / 2, bstart + (buttonh + p), buttonw, buttonh, Text.translatable("button.coordinatesdisplay.send"), button -> {
            CoordinatesDisplay.LOGGER.player.publicChat(ModUtils.parseText(CoordinatesDisplay.CONFIG.posChatMessage));
            CoordinatesDisplay.LOGGER.player.info("Put Coordinates in Chat");
            resume();
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - buttonw / 2, bstart + (buttonh + p) * 2, buttonw, buttonh, Text.translatable("button.coordinatesdisplay.copytp"), button -> {
            this.client.keyboard.setClipboard(ModUtils.asTpCommand(x, y, z, ModUtils.getPlayerCurrentDimension()));
            CoordinatesDisplay.LOGGER.player.info("Copied as TP command");
            resume();
        }));
    }

    @Override
    public void close() {
        this.client.setScreen(null);
    }
}
