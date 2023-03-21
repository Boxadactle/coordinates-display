package me.boxadactle.coordinatesdisplay.gui.config;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class VisualConfigScreen extends Screen {
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

    Vec3d pos;
    ChunkPos chunkPos;
    float cameraYaw;

    TextFieldWidget padding;
    TextFieldWidget textPadding;

    String version;

    public VisualConfigScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config.visual", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
        this.parent = parent;

        this.pos = new Vec3d(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos(new BlockPos((int)pos.getX(), (int)pos.getY(), (int)pos.getZ()));
        this.cameraYaw  = (float) Math.random() * 180;

        version = ModVersion.getVersion();

        CoordinatesDisplay.shouldRenderOnHud = false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX,  mouseY, delta);

        drawCenteredTextWithShadow(matrices, this.textRenderer, Text.translatable("screen.coordinatesdisplay.config.visual", CoordinatesDisplay.MOD_NAME, version), this.width / 2, 5, ModUtils.WHITE);

        // padding
        drawTextWithShadow(matrices, textRenderer, Text.translatable("button.coordinatesdisplay.padding"), this.width / 2 - smallButtonW, start + (buttonHeight + p) * 3 + p, ModUtils.WHITE);

        // text padding
        drawTextWithShadow(matrices, textRenderer, Text.translatable("button.coordinatesdisplay.textpadding"), this.width / 2 + p, start + (buttonHeight + p) * 3 + p, ModUtils.WHITE);

        if (CoordinatesDisplay.CONFIG.visible) {
            CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, null, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), (int) (this.height / 1.8));
        }
    }

    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.back"), (button) -> this.close()).dimensions(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight).build());

        // open wiki
        this.addDrawableChild(new PressableTextWidget(5, 5, tinyButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.help"), (button) -> this.client.setScreen(new ConfirmLinkScreen((yes) -> {
            this.client.setScreen(this);
            if (yes) {
                Util.getOperatingSystem().open(ModUtils.CONFIG_WIKI_VISUAL);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtils.CONFIG_WIKI_VISUAL, false)), MinecraftClient.getInstance().textRenderer));

        initButtons();
        initTextFields();
    }

    private void initButtons() {
        // visible button
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.visible", CoordinatesDisplay.CONFIG.visible ? ModUtils.TRUE : ModUtils.FALSE), (button) -> {
            CoordinatesDisplay.CONFIG.visible = !CoordinatesDisplay.CONFIG.visible;
            button.setMessage(Text.translatable("button.coordinatesdisplay.visible", CoordinatesDisplay.CONFIG.visible ? ModUtils.TRUE : ModUtils.FALSE));
            CoordinatesDisplay.OVERLAY.updateConfig(CoordinatesDisplay.CONFIG);
        }).dimensions(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight).tooltip(Tooltip.of(Text.translatable("description.coordinatesdisplay.visible"))).build());

        // decimal rounding button
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.decimal", (CoordinatesDisplay.CONFIG.roundPosToTwoDecimals ? ModUtils.TRUE : ModUtils.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.roundPosToTwoDecimals = !CoordinatesDisplay.CONFIG.roundPosToTwoDecimals;
            button.setMessage(Text.translatable("button.coordinatesdisplay.decimal", (CoordinatesDisplay.CONFIG.roundPosToTwoDecimals ? ModUtils.TRUE : ModUtils.FALSE)));
            CoordinatesDisplay.OVERLAY.updateConfig(CoordinatesDisplay.CONFIG);
        }).dimensions(this.width / 2 - largeButtonW / 2, start + buttonHeight + p, largeButtonW, buttonHeight).tooltip(Tooltip.of(Text.translatable("description.coordinatesdisplay.decimal"))).build());

        // modify position button
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.position"), (button) -> this.client.setScreen(new ChangePositionScreen(this)))
                .dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight).tooltip(Tooltip.of(Text.translatable("description.coordinatesdisplay.position"))).build());
    }

    private void initTextFields() {
        padding = new TextFieldWidget(textRenderer, this.width / 2 - smallButtonW - p, start + (buttonHeight + p) * 4 - p * 2, smallButtonW, buttonHeight,
                Text.literal(Integer.toString(CoordinatesDisplay.CONFIG.padding)));
        textPadding = new TextFieldWidget(textRenderer, this.width / 2 + p, start + (buttonHeight + p) * 4 - p * 2, smallButtonW, buttonHeight,
                Text.literal(Integer.toString(CoordinatesDisplay.CONFIG.textPadding)));

        padding.setText(Integer.toString(CoordinatesDisplay.CONFIG.padding));
        textPadding.setText(Integer.toString(CoordinatesDisplay.CONFIG.textPadding));

        padding.setChangedListener((text) -> {
            if (!text.isEmpty()) {
                try {
                    CoordinatesDisplay.CONFIG.padding = Integer.parseInt(text);

                } catch (NumberFormatException e) {
                    CoordinatesDisplay.LOGGER.printStackTrace(e);
                }
            }
        });

        textPadding.setChangedListener((text) -> {
            if (!text.isEmpty()) {
                try {
                    CoordinatesDisplay.CONFIG.textPadding = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    CoordinatesDisplay.LOGGER.printStackTrace(e);
                }
            }
        });

        this.addDrawableChild(padding);
        this.addDrawableChild(textPadding);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
        CoordinatesDisplay.shouldRenderOnHud = true;
    }
}
