package me.boxadactle.coordinatesdisplay.gui;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.gui.screen.ChatScreen;
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
        this.renderBackgroundTexture(0);

        drawCenteredText(matrices, this.textRenderer, Text.translatable("message.coordinatesdisplay.at"), this.width / 2, (this.height / 4) - 20, white);
        drawCenteredText(matrices, this.textRenderer, Text.translatable("message.coordinatesdisplay.location", x, y, z), this.width / 2, (this.height / 4), white);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget(this.width / 2 - buttonw / 2, (this.height / 2), buttonw, buttonh, Text.translatable("button.coordinatesdisplay.copy"), button -> {
            this.client.keyboard.setClipboard(x + " " + y + " " + z);
            CoordinatesDisplay.LOGGER.info("Copied coordinates to clipboard");

            this.client.setScreen(null);
            this.client.mouse.lockCursor();
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - buttonw / 2, (this.height / 2) + buttonh + p, buttonw, buttonh, Text.translatable("button.coordinatesdisplay.send"), button -> {
            this.client.setScreen(new ChatScreen( x + " " + y + " " + z));
            CoordinatesDisplay.LOGGER.info("Put Coordinates in Chat Screen");
        }));
    }
}
