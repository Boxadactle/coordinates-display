package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.gui.ConfigScreen;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;

public class ColorConfigScreen extends ConfigScreen {

    public ColorConfigScreen(Screen parent) {
        super(parent);

        super.generatePositionData();
        super.setTitle(Component.translatable("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));

        CoordinatesDisplay.shouldRenderOnHud = false;
    }

    private void renderColorPickerGraphic(GuiGraphics guiGraphics) {
        float s = 1.2F;
        PoseStack matrices = guiGraphics.pose();
        matrices.pushPose();
        matrices.scale(s, s, s);

        int a = (int) (this.width / s);
        int b = (int) (this.height / s) + 75;
        int c = (int) (a / 2 + (5 / s));
        int d = (int) (b / 2.3) - 4;
        int e = (int) (872 / 1.8) / this.nonZeroGuiScale() / 4;
        int f = (int) (586 / 1.8) / this.nonZeroGuiScale() / 4;

        guiGraphics.blit(new ResourceLocation("coordinatesdisplay", "textures/color_picker.png"), c, d, 0.0F, 0.0F, e, f, e, f);

        matrices.popPose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);

        super.drawTitle(guiGraphics);

        int y = (int) (this.height / 2.3);

        guiGraphics.drawString(this.font, Component.translatable("button.coordinatesidisplay.colors.definition"), this.width / 2 - smallButtonW, start + 8, ModUtil.WHITE);
        guiGraphics.drawString(this.font, Component.translatable("button.coordinatesidisplay.colors.data"), this.width / 2, start + 8, ModUtil.WHITE);
        guiGraphics.drawString(this.font, Component.translatable("button.coordinatesidisplay.colors.deathpos"), this.width / 2 - smallButtonW - p, start + 8 + (buttonHeight + p) * 2, ModUtil.WHITE);
        guiGraphics.drawString(this.font, Component.translatable("button.coordinatesidisplay.colors.background"), this.width / 2, start + 8 + (buttonHeight + p) * 2, ModUtil.WHITE);

        CoordinatesDisplay.OVERLAY.render(guiGraphics, pos, chunkPos, cameraYaw, cameraPitch, null, this.width / 2 - CoordinatesDisplay.OVERLAY.getWidth() - 5, y + 40, CoordinatesDisplay.CONFIG.get().minMode, false);

        Component posT = ModUtil.colorize(ComponentUtils.wrapInSquareBrackets(Component.translatable("message.coordinatesdisplay.deathlocation", deathx, deathy, deathz, dimension)), CoordinatesDisplay.CONFIG.get().deathPosColor);
        Component deathPos = Component.translatable("message.coordinatesdisplay.deathpos", posT);
        guiGraphics.drawCenteredString(this.font, deathPos, this.width / 2, y - (CoordinatesDisplay.OVERLAY.getHeight() / 4) + 40, ModUtil.WHITE);

        this.renderColorPickerGraphic(guiGraphics);

        super.render(guiGraphics, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.back"), (button) -> this.onClose()).bounds(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight).build());

        // open wiki
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.help"), (button) -> Minecraft.getInstance().setScreen(new ConfirmLinkScreen((yes) -> {
            Minecraft.getInstance().setScreen(this);
            if (yes) {
                Util.getPlatform().openUri(ModUtil.CONFIG_WIKI_COLOR);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtil.CONFIG_WIKI_COLOR, false))).bounds(5, 5, tinyButtonW, buttonHeight).build());

        initButtons();
    }

    private void initButtons() {
        EditBox definitionColor = new EditBox(this.font, this.width / 2 - smallButtonW - p, start + (buttonHeight + p), smallButtonW, buttonHeight, Component.literal(Integer.toHexString(CoordinatesDisplay.CONFIG.get().definitionColor)));
        EditBox dataColor = new EditBox(this.font, this.width / 2 + p, start + (buttonHeight + p), smallButtonW, buttonHeight, Component.literal(Integer.toHexString(CoordinatesDisplay.CONFIG.get().definitionColor)));
        EditBox deathposColor = new EditBox(this.font, this.width / 2 - smallButtonW - p, start + (buttonHeight + p) * 3, smallButtonW, buttonHeight, Component.literal(Integer.toHexString(CoordinatesDisplay.CONFIG.get().definitionColor)));
        EditBox backgroundColor = new EditBox(this.font, this.width / 2 + p, start + (buttonHeight + p) * 3, smallButtonW, buttonHeight, Component.literal(Integer.toHexString(CoordinatesDisplay.CONFIG.get().definitionColor)));

        definitionColor.setMaxLength(6);
        dataColor.setMaxLength(6);
        deathposColor.setMaxLength(6);
        backgroundColor.setMaxLength(8);

        definitionColor.setValue(Integer.toHexString(CoordinatesDisplay.CONFIG.get().definitionColor));
        dataColor.setValue(Integer.toHexString(CoordinatesDisplay.CONFIG.get().dataColor));
        deathposColor.setValue(Integer.toHexString(CoordinatesDisplay.CONFIG.get().deathPosColor));
        backgroundColor.setValue(Integer.toHexString(CoordinatesDisplay.CONFIG.get().backgroundColor));

        definitionColor.setResponder((txt) -> {
            if (txt.isEmpty()) return;
            try {
                CoordinatesDisplay.CONFIG.get().definitionColor = Integer.valueOf(txt.replaceAll("#", ""), 16);
            } catch (NumberFormatException e) {
                CoordinatesDisplay.LOGGER.error("Why you put invalid hex code?");
                CoordinatesDisplay.LOGGER.printStackTrace(e);
            }
        });

        dataColor.setResponder((txt) -> {
            if (txt.isEmpty()) return;
            try {
                CoordinatesDisplay.CONFIG.get().dataColor = Integer.valueOf(txt.replaceAll("#", ""), 16);
            } catch (NumberFormatException e) {
                CoordinatesDisplay.LOGGER.error("Why you put invalid hex code?");
                CoordinatesDisplay.LOGGER.printStackTrace(e);
            }
        });

        deathposColor.setResponder((txt) -> {
            if (txt.isEmpty()) return;
            try {
                CoordinatesDisplay.CONFIG.get().deathPosColor = Integer.valueOf(txt.replaceAll("#", ""), 16);
            } catch (NumberFormatException e) {
                CoordinatesDisplay.LOGGER.error("Why you put invalid hex code?");
                CoordinatesDisplay.LOGGER.printStackTrace(e);
            }
        });

        backgroundColor.setResponder((txt) -> {
            if (txt.isEmpty()) return;
            try {
                // Need to use parseUnsignedInt in order to get ARGB values over 0x7fffffff to work
                CoordinatesDisplay.CONFIG.get().backgroundColor = Integer.parseUnsignedInt(txt.replaceAll("#", ""), 16);
            } catch (NumberFormatException e) {
                CoordinatesDisplay.LOGGER.error("Why you put invalid hex code?");
                CoordinatesDisplay.LOGGER.printStackTrace(e);
            }
        });

        this.addRenderableWidget(definitionColor);
        this.addRenderableWidget(dataColor);
        this.addRenderableWidget(deathposColor);
        this.addRenderableWidget(backgroundColor);

        this.addRenderableWidget(new PlainTextButton(this.width / 2 - smallButtonW - p, start + 8 + (buttonHeight + p) * 4, smallButtonW, buttonHeight, Component.literal("Color Picker..."), (button -> {
            Util.getPlatform().openUri("https://htmlcolorcodes.com/color-picker/");
        }), Minecraft.getInstance().font));

    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parent);
        CoordinatesDisplay.shouldRenderOnHud = true;
    }
}
