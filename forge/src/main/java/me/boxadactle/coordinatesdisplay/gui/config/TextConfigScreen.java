package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.gui.ConfigScreen;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.transformer.Config;

public class TextConfigScreen extends ConfigScreen {

    public TextConfigScreen(Screen parent) {
        super(parent);

        super.generatePositionData();
        super.setTitle(Component.translatable("screen.coordinatesdisplay.config.text", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);

        super.drawTitle(guiGraphics);

        guiGraphics.drawCenteredString(this.font, Component.translatable("button.coordinatesdisplay.poschatmessage"), this.width / 2, start, ModUtil.WHITE);

        guiGraphics.drawCenteredString(this.font, Component.translatable("button.coordinatesdisplay.copyposmessage"), this.width / 2, start + (10 + p) + (buttonHeight + p), ModUtil.WHITE);

        super.render(guiGraphics, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.back"), (button) -> this.minecraft.setScreen(parent)).bounds(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight).build());

        // open wiki
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.help"), (button) -> this.minecraft.setScreen(new ConfirmLinkScreen((yes) -> {
            this.minecraft.setScreen(this);
            if (yes) {
                Util.getPlatform().openUri(ModUtil.CONFIG_WIKI_TEXTS);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtil.CONFIG_WIKI_TEXTS, false))).bounds(5, 5, tinyButtonW, buttonHeight).build());

        EditBox posChatMessage = new EditBox(this.font, this.width / 2 - largeButtonW / 2, start + (10 + p), largeButtonW, buttonHeight, Component.literal(CoordinatesDisplay.CONFIG.get().posChatMessage));
        posChatMessage.setResponder((message) -> CoordinatesDisplay.CONFIG.get().posChatMessage = message);
        posChatMessage.setMaxLength(50);
        posChatMessage.setValue(CoordinatesDisplay.CONFIG.get().posChatMessage);

        EditBox copyPosMessage = new EditBox(this.font, this.width / 2 - largeButtonW / 2, start + (10 + p) * 2 + (buttonHeight + p), largeButtonW, buttonHeight, Component.literal(CoordinatesDisplay.CONFIG.get().posChatMessage));
        copyPosMessage.setResponder((message) -> CoordinatesDisplay.CONFIG.get().copyPosMessage = message);
        copyPosMessage.setMaxLength(50);
        copyPosMessage.setValue(CoordinatesDisplay.CONFIG.get().copyPosMessage);

        this.addRenderableWidget(posChatMessage);
        this.addRenderableWidget(copyPosMessage);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}