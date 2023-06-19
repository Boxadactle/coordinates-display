package me.boxadactle.coordinatesdisplay.gui.config;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.gui.ConfigScreen;
import me.boxadactle.coordinatesdisplay.gui.widget.ConfigBooleanWidget;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.text.DecimalFormat;

public class DeathPosScreen extends ConfigScreen {
    public DeathPosScreen(Screen parent) {
        super(parent);
        this.parent = parent;

        super.generatePositionData();
        super.setTitle(Text.translatable("screen.coordinatesdisplay.config.deathpos", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));

        CoordinatesDisplay.shouldRenderOnHud = false;
    }
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);

        super.drawTitle(drawContext);

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

        this.initConfigScreen();
    }

    public void initConfigScreen() {
        // show death pos in chat
        this.addDrawableChild(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start,
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.deathpos.deathscreen",
                CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen,
                newValue -> CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen = newValue
        ));

        // chunk data
        this.addDrawableChild(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start + (buttonHeight + p),
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.deathpos.chat",
                CoordinatesDisplay.CONFIG.get().showDeathPosInChat,
                newValue -> CoordinatesDisplay.CONFIG.get().showDeathPosInChat = newValue
        ));
    }

    public void close() {
        this.client.setScreen(parent);
        CoordinatesDisplay.shouldRenderOnHud = true;
    }
}
