package me.boxadactle.coordinatesdisplay.gui.config;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.client.gui.screen.ConfirmChatLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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

    ModVersion version;

    public DeathPosConfigScreen(Screen parent) {
        super(new TranslatableText("screen.coordinatesdisplay.config.deathpos", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion().thisVersion()));
        this.parent = parent;

        DecimalFormat d = new DecimalFormat("0.00");

        deathx = d.format(Math.random() * 1000);
        deathy = d.format(Math.random() * 100);
        deathz = d.format(Math.random() * 1000);

        version = ModVersion.getVersion();

        CoordinatesDisplay.shouldRenderOnHud = false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, new TranslatableText("screen.coordinatesdisplay.config.deathpos", CoordinatesDisplay.MOD_NAME, version.thisVersion()), this.width / 2, 5, ModUtils.WHITE);

        Text pos = new TranslatableText("message.coordinatesdisplay.location", deathx, deathy, deathz).styled(style -> style.withColor(CoordinatesDisplay.CONFIG.deathPosColor));
        Text deathPos = new TranslatableText("message.coordinatesdisplay.deathpos", pos).styled(style -> style.withColor(CoordinatesDisplay.CONFIG.definitionColor));
        drawCenteredText(matrices, this.textRenderer, deathPos, this.width / 2, (int) (this.width / 1.5), ModUtils.WHITE);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight, new TranslatableText("button.coordinatesdisplay.back"), (button) -> this.onClose()));

        // open wiki
        this.addDrawableChild(new ButtonWidget(5, 5, tinyButtonW, buttonHeight, new TranslatableText("button.coordinatesdisplay.help"), (button) -> this.client.setScreen(new ConfirmChatLinkScreen((yes) -> {
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
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, new TranslatableText("button.coordinatesdisplay.deathpos.deathscreen", (CoordinatesDisplay.CONFIG.displayPosOnDeathScreen ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.displayPosOnDeathScreen = !CoordinatesDisplay.CONFIG.displayPosOnDeathScreen;
            button.setMessage(new TranslatableText("button.coordinatesdisplay.deathpos.deathscreen", (CoordinatesDisplay.CONFIG.displayPosOnDeathScreen ? ModUtils.TRUE : ModUtils.FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, new TranslatableText("description.coordinatesdisplay.deathpos.deathscreen"), mouseX, mouseY);
            }
        }));

        // chunk data
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight, new TranslatableText("button.coordinatesdisplay.deathpos.chat", (CoordinatesDisplay.CONFIG.showDeathPosInChat ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.showDeathPosInChat = !CoordinatesDisplay.CONFIG.showDeathPosInChat;
            button.setMessage(new TranslatableText("button.coordinatesdisplay.deathpos.chat", (CoordinatesDisplay.CONFIG.showDeathPosInChat ? ModUtils.TRUE : ModUtils.FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, new TranslatableText("description.coordinatesdisplay.deathpos.chat"), mouseX, mouseY);
            }
        }));
    }

    @Override
    public void onClose() {
        this.client.setScreen(parent);
        CoordinatesDisplay.shouldRenderOnHud = true;
    }
}
