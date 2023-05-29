package me.boxadactle.coordinatesdisplay.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CoordinatesScreen extends Screen {

    int x;
    int y;
    int z;

    int white = 16777215;

    int buttonw = 300;
    int buttonh = 20;

    int p = 5;

    public CoordinatesScreen(int x, int y, int z) {
        super(Component.literal("Coordinates Screen"));

        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredString(matrices, this.font, Component.translatable("message.coordinatesdisplay.at"), this.width / 2, (this.height / 4) - 20, white);
        drawCenteredString(matrices, this.font, Component.translatable("message.coordinatesdisplay.location", x, y, z), this.width / 2, (this.height / 4), white);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void init() {
        super.init();

        int bstart = this.height / 2 - 20;

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.copy"), button -> {
            this.minecraft.keyboardHandler.setClipboard(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().copyPosMessage));
            CoordinatesDisplay.LOGGER.player.info("Copied coordinates to clipboard");
            onClose();
        }).bounds(this.width / 2 - buttonw / 2, bstart, buttonw, buttonh).build());

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.send"), button -> {
            CoordinatesDisplay.LOGGER.player.publicChat(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().posChatMessage));
            onClose();
        }).bounds(this.width / 2 - buttonw / 2, bstart + (buttonh + p), buttonw, buttonh).build());

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.copytp"), button -> {
            this.minecraft.keyboardHandler.setClipboard(ModUtil.asTpCommand(x, y, z, ModUtil.getPlayerCurrentDimension()));
            CoordinatesDisplay.LOGGER.player.info("Copied as TP command");
            onClose();
        }).bounds(this.width / 2 - buttonw / 2, bstart + (buttonh + p) * 2, buttonw, buttonh).build());
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(null);
        this.minecraft.mouseHandler.grabMouse();
    }
}