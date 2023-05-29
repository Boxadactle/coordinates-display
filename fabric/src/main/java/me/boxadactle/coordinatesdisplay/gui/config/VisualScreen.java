package me.boxadactle.coordinatesdisplay.gui.config;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

public class VisualScreen extends Screen {
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
    float cameraPitch;

    TextFieldWidget padding;
    TextFieldWidget textPadding;

    public VisualScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config.visual", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
        this.parent = parent;

        this.pos = new Vec3d(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos(new BlockPos(ModUtil.doubleVecToIntVec(this.pos)));
        this.cameraYaw  = (float) Math.random() * 180;
        this.cameraPitch  = (float) Math.random() * 180;

        CoordinatesDisplay.shouldRenderOnHud = false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX,  mouseY, delta);

        drawCenteredTextWithShadow(matrices, this.textRenderer, Text.translatable("screen.coordinatesdisplay.config.visual", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()), this.width / 2, 5, ModUtil.WHITE);

        // padding
        drawTextWithShadow(matrices, textRenderer, Text.translatable("button.coordinatesdisplay.padding"), this.width / 2 - smallButtonW, start + (buttonHeight + p) * 4 + p, ModUtil.WHITE);

        // text padding
        drawTextWithShadow(matrices, textRenderer, Text.translatable("button.coordinatesdisplay.textpadding"), this.width / 2 + p, start + (buttonHeight + p) * 4 + p, ModUtil.WHITE);

        if (CoordinatesDisplay.CONFIG.get().visible) {
            CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, cameraPitch, null, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), (int) (this.height / 1.8) + 10, CoordinatesDisplay.CONFIG.get().minMode, false);
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

        initButtons();
        initTextFields();
    }

    private void initButtons() {
        // visible button
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.visible", CoordinatesDisplay.CONFIG.get().visible ? ModUtil.TRUE : ModUtil.FALSE), (button) -> {
            CoordinatesDisplay.CONFIG.get().visible = !CoordinatesDisplay.CONFIG.get().visible;
            button.setMessage(Text.translatable("button.coordinatesdisplay.visible", CoordinatesDisplay.CONFIG.get().visible ? ModUtil.TRUE : ModUtil.FALSE));
        }).dimensions(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight).build());

        // decimal rounding button
        ButtonWidget a = new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.decimal", (CoordinatesDisplay.CONFIG.get().roundPosToTwoDecimals ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().roundPosToTwoDecimals = !CoordinatesDisplay.CONFIG.get().roundPosToTwoDecimals;
            button.setMessage(Text.translatable("button.coordinatesdisplay.decimal", (CoordinatesDisplay.CONFIG.get().roundPosToTwoDecimals ? ModUtil.TRUE : ModUtil.FALSE)));
        }).dimensions(this.width / 2 - largeButtonW / 2, start + buttonHeight + p, largeButtonW, buttonHeight).build();
        a.active = !CoordinatesDisplay.CONFIG.get().minMode;
        this.addDrawableChild(a);

        // minimum mode button
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.minmode", (CoordinatesDisplay.CONFIG.get().minMode ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().minMode = !CoordinatesDisplay.CONFIG.get().minMode;
            button.setMessage(Text.translatable("button.coordinatesdisplay.minmode", (CoordinatesDisplay.CONFIG.get().minMode ? ModUtil.TRUE : ModUtil.FALSE)));
            a.active = !CoordinatesDisplay.CONFIG.get().minMode;
        }).dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight).build());


        // modify position button
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.position"), (button) -> this.client.setScreen(new HudPositionScreen(this)))
                .dimensions(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 3, largeButtonW, buttonHeight).build());
    }

    private void initTextFields() {
        padding = new TextFieldWidget(textRenderer, this.width / 2 - smallButtonW - p, start + (buttonHeight + p) * 5 - p * 2, smallButtonW, buttonHeight,
                Text.literal(Integer.toString(CoordinatesDisplay.CONFIG.get().padding)));
        textPadding = new TextFieldWidget(textRenderer, this.width / 2 + p, start + (buttonHeight + p) * 5 - p * 2, smallButtonW, buttonHeight,
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

    @Override
    public void close() {
        this.client.setScreen(parent);
        CoordinatesDisplay.shouldRenderOnHud = true;
    }
}
