package me.boxadactle.coordinatesdisplay.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ServerboundChatPacket;

public class CoordinatesScreen extends Screen {

    int x;
    int y;
    int z;

    int white = 16777215;

    int buttonw = 300;
    int buttonh = 20;

    int p = 5;

    public CoordinatesScreen(int x, int y, int z) {
        super(new TextComponent("Coordinates Screen"));

        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredString(matrices, this.font, new TranslatableComponent("message.coordinatesdisplay.at"), this.width / 2, (this.height / 4) - 20, white);
        drawCenteredString(matrices, this.font, new TranslatableComponent("message.coordinatesdisplay.location", x, y, z), this.width / 2, (this.height / 4), white);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void init() {
        super.init();

        int bstart = this.height / 2 - 20;

        this.addRenderableWidget(new Button(this.width / 2 - buttonw / 2, bstart, buttonw, buttonh, new TranslatableComponent("button.coordinatesdisplay.copy"), button -> {
            this.minecraft.keyboardHandler.setClipboard(ModUtils.parseText(CoordinatesDisplay.CONFIG.get().copyPosMessage));
            CoordinatesDisplay.LOGGER.chatInfo("Copied coordinates to clipboard");
            onClose();
        }));

        this.addRenderableWidget(new Button(this.width / 2 - buttonw / 2, bstart + (buttonh + p), buttonw, buttonh, new TranslatableComponent("button.coordinatesdisplay.send"), button -> {
            this.minecraft.getConnection().send(new ServerboundChatPacket(ModUtils.parseText(CoordinatesDisplay.CONFIG.get().posChatMessage)));
            CoordinatesDisplay.LOGGER.chatInfo("Put Coordinates in Chat");
            onClose();
        }));

        this.addRenderableWidget(new Button(this.width / 2 - buttonw / 2, bstart + (buttonh + p) * 2, buttonw, buttonh, new TranslatableComponent("button.coordinatesdisplay.copytp"), button -> {
            this.minecraft.keyboardHandler.setClipboard(ModUtils.asTpCommand(x, y, z, ModUtils.getPlayerCurrentDimension()));
            CoordinatesDisplay.LOGGER.chatInfo("Copied as TP command");
            onClose();
        }));
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(null);
        this.minecraft.mouseHandler.grabMouse();
    }
}