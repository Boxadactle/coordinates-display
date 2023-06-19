package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.gui.ConfigScreen;
import me.boxadactle.coordinatesdisplay.gui.widget.ConfigBooleanWidget;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.text.DecimalFormat;

@OnlyIn(Dist.CLIENT)
public class DeathPosConfigScreen extends ConfigScreen {
    public DeathPosConfigScreen(Screen parent) {
        super(parent);
        this.parent = parent;

        super.generatePositionData();
        super.setTitle(Component.translatable("screen.coordinatesdisplay.config.deathpos", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);

        super.drawTitle(guiGraphics);

        Component pos = Component.translatable("message.coordinatesdisplay.location", deathx, deathy, deathz).withStyle(style -> style.withColor(CoordinatesDisplay.CONFIG.get().deathPosColor));
        Component deathPos = Component.translatable("message.coordinatesdisplay.deathpos", pos).withStyle(style -> style.withColor(CoordinatesDisplay.CONFIG.get().definitionColor));
        guiGraphics.drawCenteredString(this.font, deathPos, this.width / 2, (int) (this.width / 1.5), ModUtil.WHITE);

        super.render(guiGraphics, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.back"), (button) -> this.minecraft.setScreen(parent)).bounds(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight).build());

        initButtons();
    }

    private void initButtons() {
        // show death pos in chat
        this.addRenderableWidget(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start,
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.deathpos.deathscreen",
                CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen,
                newValue -> CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen = newValue
        ));

        // chunk data
        this.addRenderableWidget(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start + (buttonHeight + p),
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.deathpos.chat",
                CoordinatesDisplay.CONFIG.get().showDeathPosInChat,
                newValue -> CoordinatesDisplay.CONFIG.get().showDeathPosInChat = newValue
        ));

        // open wiki
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.help"), (button) -> this.minecraft.setScreen(new ConfirmLinkScreen((yes) -> {
            this.minecraft.setScreen(this);
            if (yes) {
                Util.getPlatform().openUri(ModUtil.CONFIG_WIKI_DEATH);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtil.CONFIG_WIKI_DEATH, false))).bounds(5, 5, tinyButtonW, buttonHeight).build());
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}