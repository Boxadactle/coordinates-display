package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.text.DecimalFormat;

@OnlyIn(Dist.CLIENT)
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

    public DeathPosConfigScreen(Screen parent) {
        super(Component.translatable("screen.coordinatesdisplay.config.deathpos", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion().thisVersion()));
        this.parent = parent;

        DecimalFormat d = new DecimalFormat("0.00");

        deathx = d.format(Math.random() * 1000);
        deathy = d.format(Math.random() * 100);
        deathz = d.format(Math.random() * 1000);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredString(matrices, this.font, Component.translatable("screen.coordinatesdisplay.config.deathpos", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion().thisVersion()), this.width / 2, 5, ModUtils.WHITE);

<<<<<<< Updated upstream
        Component pos = Component.translatable("message.coordinatesdisplay.location", deathx, deathy, deathz).withStyle(style -> style.withColor(ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().deathPosColor)));
        Component deathPos = Component.translatable("message.coordinatesdisplay.deathpos", pos).withStyle(style -> style.withColor(ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor)));
        drawCenteredString(matrices, this.font, deathPos, this.width / 2, (int) (this.width / 1.5), ModUtils.WHITE);
=======
        Component pos = Component.translatable("message.coordinatesdisplay.location", deathx, deathy, deathz).withStyle(style -> style.withColor(CoordinatesDisplay.CONFIG.get().deathPosColor));
        Component deathPos = Component.translatable("message.coordinatesdisplay.deathpos", pos).withStyle(style -> style.withColor(CoordinatesDisplay.CONFIG.get().definitionColor));
        drawCenteredString(matrices, this.font, deathPos, this.width / 2, (int) (this.width / 1.5), ModUtil.WHITE);
>>>>>>> Stashed changes

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.back"), (button) -> this.minecraft.setScreen(parent)));

        initButtons();
    }

    private void initButtons() {
        // show death pos in chat
<<<<<<< Updated upstream
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.deathpos.deathscreen", (CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen = !CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen;
            button.setMessage(Component.translatable("button.coordinatesdisplay.deathpos.deathscreen", (CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen ? ModUtils.TRUE : ModUtils.FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHoveredOrFocused()) {
                this.renderTooltip(matrices, Component.translatable("description.coordinatesdisplay.deathpos.deathscreen"), mouseX, mouseY);
            }
        }));

        // chunk data
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.deathpos.chat", (CoordinatesDisplay.CONFIG.get().showDeathPosInChat ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().showDeathPosInChat = !CoordinatesDisplay.CONFIG.get().showDeathPosInChat;
            button.setMessage(Component.translatable("button.coordinatesdisplay.deathpos.chat", (CoordinatesDisplay.CONFIG.get().showDeathPosInChat ? ModUtils.TRUE : ModUtils.FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHoveredOrFocused()) {
                this.renderTooltip(matrices, Component.translatable("description.coordinatesdisplay.deathpos.chat"), mouseX, mouseY);
            }
=======
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.deathpos.deathscreen", (CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen = !CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen;
            button.setMessage(Component.translatable("button.coordinatesdisplay.deathpos.deathscreen", (CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen ? ModUtil.TRUE : ModUtil.FALSE)));
        }));

        // chunk data
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.deathpos.chat", (CoordinatesDisplay.CONFIG.get().showDeathPosInChat ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().showDeathPosInChat = !CoordinatesDisplay.CONFIG.get().showDeathPosInChat;
            button.setMessage(Component.translatable("button.coordinatesdisplay.deathpos.chat", (CoordinatesDisplay.CONFIG.get().showDeathPosInChat ? ModUtil.TRUE : ModUtil.FALSE)));
>>>>>>> Stashed changes
        }));

        // open wiki
        this.addRenderableWidget(new Button(5, 5, tinyButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.help"), (button) -> this.minecraft.setScreen(new ConfirmLinkScreen((yes) -> {
            this.minecraft.setScreen(this);
            if (yes) {
                Util.getPlatform().openUri(ModUtils.CONFIG_WIKI_DEATH);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtils.CONFIG_WIKI_DEATH, false))));
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}