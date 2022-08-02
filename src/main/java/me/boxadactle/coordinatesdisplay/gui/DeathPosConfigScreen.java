package me.boxadactle.coordinatesdisplay.gui;

import io.github.cottonmc.cotton.config.ConfigManager;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

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

    int white = 16777215;

    String TRUE = "§a" + Text.translatable("coordinatesdisplay.true").getString();
    String FALSE = "§c" + Text.translatable("coordinatesdisplay.false").getString();

    public DeathPosConfigScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config.deathpos", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION));
        this.parent = parent;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, Text.translatable("screen.coordinatesdisplay.config.deathpos", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION), this.width / 2, 5, white);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.back"), (button) -> this.client.setScreen(parent)));

        initButtons();
    }

    private void initButtons() {
        // show death pos in chat
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.deathpos.deathscreen", (CoordinatesDisplay.CONFIG.displayPosOnDeathScreen ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.displayPosOnDeathScreen = !CoordinatesDisplay.CONFIG.displayPosOnDeathScreen;
            button.setMessage(Text.translatable("button.coordinatesdisplay.deathpos.deathscreen", (CoordinatesDisplay.CONFIG.displayPosOnDeathScreen ? TRUE : FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.deathpos.deathscreen"), mouseX, mouseY);
            }
        }));

        // chunk data
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.deathpos.chat", (CoordinatesDisplay.CONFIG.showDeathPosInChat ? TRUE : FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.showDeathPosInChat = !CoordinatesDisplay.CONFIG.showDeathPosInChat;
            button.setMessage(Text.translatable("button.coordinatesdisplay.deathpos.chat", (CoordinatesDisplay.CONFIG.showDeathPosInChat ? TRUE : FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, Text.translatable("description.coordinatesdisplay.deathpos.chat"), mouseX, mouseY);
            }
        }));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
