package dev.boxadactle.coordinatesdisplay.gui;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.MouseUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CoordinatesScreen extends Screen {

    Position pos;

    int white = 16777215;

    int buttonw = 300;
    int buttonh = 20;

    int p = 5;

    public CoordinatesScreen(Position pos) {
        super(Component.literal("Coordinates Screen"));

        this.pos = pos;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);

        int x = (int)Math.round(pos.getPlayerVector().getX());
        int y = (int)Math.round(pos.getPlayerVector().getY());
        int z = (int)Math.round(pos.getPlayerVector().getZ());

        guiGraphics.drawCenteredString(this.font, Component.translatable("message.coordinatesdisplay.at"), this.width / 2, (this.height / 4) - 20, white);
        guiGraphics.drawCenteredString(this.font, Component.translatable("message.coordinatesdisplay.location", x, y, z), this.width / 2, (this.height / 4), white);

        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public void init() {
        super.init();

        int bstart = this.height / 2 - 20;

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.copy"), button -> {
            ClientUtils.getClient().keyboardHandler.setClipboard(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().copyPosMessage, this.pos));
            CoordinatesDisplay.LOGGER.player.info("Copied coordinates to clipboard");
            onClose();
        }).bounds(this.width / 2 - buttonw / 2, bstart, buttonw, buttonh).build());

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.send"), button -> {
            CoordinatesDisplay.LOGGER.player.publicChat(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().posChatMessage, this.pos));
            onClose();
        }).bounds(this.width / 2 - buttonw / 2, bstart + (buttonh + p), buttonw, buttonh).build());

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.copytp"), button -> {
            ClientUtils.getClient().keyboardHandler.setClipboard(ModUtil.toTeleportCommand(Position.of(WorldUtils.getCamera()).getPlayerVector(), WorldUtils.getCurrentDimension()));
            CoordinatesDisplay.LOGGER.player.info("Copied as TP command");
            onClose();
        }).bounds(this.width / 2 - buttonw / 2, bstart + (buttonh + p) * 2, buttonw, buttonh).build());
    }

    @Override
    public void onClose() {
        ClientUtils.setScreen(null);
        MouseUtils.getMouse().grabMouse();
    }
}