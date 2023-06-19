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
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

public class VisualScreen extends ConfigScreen {
    TextFieldWidget padding;
    TextFieldWidget textPadding;

    public VisualScreen(Screen parent) {
        super(parent);
        this.parent = parent;

        super.generatePositionData();
        super.setTitle(Text.translatable("screen.coordinatesdisplay.config.visual", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));

        CoordinatesDisplay.shouldRenderOnHud = false;
    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);

        super.render(drawContext, mouseX,  mouseY, delta);

        super.drawTitle(drawContext);

        // padding
        drawContext.drawTextWithShadow(textRenderer, Text.translatable("button.coordinatesdisplay.padding"), this.width / 2 - smallButtonW, start + (buttonHeight + p) * 5 + p, ModUtil.WHITE);

        // text padding
        drawContext.drawTextWithShadow(textRenderer, Text.translatable("button.coordinatesdisplay.textpadding"), this.width / 2 + p, start + (buttonHeight + p) * 5 + p, ModUtil.WHITE);

        if (CoordinatesDisplay.CONFIG.get().visible) {
            CoordinatesDisplay.OVERLAY.render(drawContext, pos, chunkPos, cameraYaw, cameraPitch, null, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), (int) (this.height / 1.8) + 40, CoordinatesDisplay.CONFIG.get().minMode, false);
        }
    }

    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.back"), (button) -> this.close()).dimensions(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight).build());

        // open wiki
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.help"), (button) -> this.client.setScreen(new ConfirmLinkScreen((yes) -> {
            this.client.setScreen(this);
            if (yes) {
                Util.getOperatingSystem().open(ModUtil.CONFIG_WIKI_VISUAL);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtil.CONFIG_WIKI_VISUAL, false))).dimensions(5, 5, tinyButtonW, buttonHeight).build());

        this.initConfigScreen();
    }

    public void initConfigScreen() {
        // visible button
        this.addDrawableChild(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start,
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.visible",
                CoordinatesDisplay.CONFIG.get().visible,
                newValue -> CoordinatesDisplay.CONFIG.get().visible = newValue
        ));

        // decimal rounding button
        ButtonWidget a = new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start + buttonHeight + p,
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.decimal",
                CoordinatesDisplay.CONFIG.get().roundPosToTwoDecimals,
                newValue -> CoordinatesDisplay.CONFIG.get().roundPosToTwoDecimals = newValue
        );
        a.active = !CoordinatesDisplay.CONFIG.get().minMode;
        this.addDrawableChild(a);

        // minimum mode button
        this.addDrawableChild(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start + (buttonHeight + p) * 2,
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.minmode",
                CoordinatesDisplay.CONFIG.get().minMode,
                newValue -> {
                    CoordinatesDisplay.CONFIG.get().minMode = newValue;
                    a.active = !newValue;
                }
        ));

        // text shadow button
        this.addDrawableChild(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start + (buttonHeight + p) * 3,
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.textshadow",
                CoordinatesDisplay.CONFIG.get().hudTextShadow,
                newValue -> CoordinatesDisplay.CONFIG.get().hudTextShadow = newValue
        ));


        // modify position button
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.position"), (button) -> this.client.setScreen(new HudPositionScreen(this)))
                .dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 4, largeButtonW, buttonHeight).build());

        this.initTextFields();
    }

    private void initTextFields() {
        padding = new TextFieldWidget(textRenderer, this.width / 2 - smallButtonW - p, start + (buttonHeight + p) * 6 - p * 2, smallButtonW, buttonHeight,
                Text.literal(Integer.toString(CoordinatesDisplay.CONFIG.get().padding)));
        textPadding = new TextFieldWidget(textRenderer, this.width / 2 + p, start + (buttonHeight + p) * 6 - p * 2, smallButtonW, buttonHeight,
                Text.literal(Integer.toString(CoordinatesDisplay.CONFIG.get().textPadding)));

        padding.setText(Integer.toString(CoordinatesDisplay.CONFIG.get().padding));
        textPadding.setText(Integer.toString(CoordinatesDisplay.CONFIG.get().textPadding));

        padding.setChangedListener((text) -> {
            if (!text.isEmpty()) {
                try {
                    CoordinatesDisplay.CONFIG.get().padding = Integer.parseInt(text);

                } catch (NumberFormatException e) {
                    CoordinatesDisplay.LOGGER.printStackTrace(e);
                }
            }
        });

        textPadding.setChangedListener((text) -> {
            if (!text.isEmpty()) {
                try {
                    CoordinatesDisplay.CONFIG.get().textPadding = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    CoordinatesDisplay.LOGGER.printStackTrace(e);
                }
            }
        });

        this.addDrawableChild(padding);
        this.addDrawableChild(textPadding);
    }

    public void close() {
        this.client.setScreen(parent);
        CoordinatesDisplay.shouldRenderOnHud = true;
    }
}
