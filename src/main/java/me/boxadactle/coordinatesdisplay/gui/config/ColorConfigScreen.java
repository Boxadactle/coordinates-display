package me.boxadactle.coordinatesdisplay.gui.config;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

import java.text.DecimalFormat;

public class ColorConfigScreen extends Screen {
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

    int deathx;
    int deathy;
    int deathz;

    public ColorConfigScreen(Screen parent) {
        super(new TranslatableText("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION));
        this.parent = parent;

        this.pos = new Vec3d(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos(new BlockPos(pos));
        this.cameraYaw  = (float) Math.random() * 180;

        DecimalFormat d = new DecimalFormat("0.00");

        deathx = (int) Math.round(Math.random() * 1000);
        deathy = (int) Math.round(Math.random() * 100);
        deathz = (int) Math.round(Math.random() * 1000);

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, new TranslatableText("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION), this.width / 2, 5, ModUtils.WHITE);

        int y = (int) (this.height / 2.3);

        CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, null, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), y);

        Text posT = Texts.bracketed(new TranslatableText("message.coordinatesdisplay.location2", deathx, deathy, deathz)).styled(style -> style.withColor(ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.deathPosColor)));
        Text deathPos = new TranslatableText("message.coordinatesdisplay.deathpos", posT);
        drawCenteredText(matrices, this.textRenderer, deathPos, this.width / 2, y - (CoordinatesDisplay.OVERLAY.getHeight() / 4), ModUtils.WHITE);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight, new TranslatableText("button.coordinatesdisplay.back"), (button) -> this.client.setScreen(parent)));

        initButtons();
    }

    private void initButtons() {
        String keyColor = ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.definitionColor) > 0 ?
                ModUtils.colors[ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.definitionColor)] : "white";
        String valueColor = ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.dataColor) > 0 ?
                ModUtils.colors[ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.dataColor)] : "white";
        String deathPosColor = ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.deathPosColor) > 0 ?
                ModUtils.colors[ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.deathPosColor)] : "white";

        String keyPrefix = ModUtils.getColorPrefix(keyColor);
        String valuePrefix = ModUtils.getColorPrefix(valueColor);
        String deathPosPrefix = ModUtils.getColorPrefix(deathPosColor);

        // lambdas are annoying
        final int[] indexes = {ModUtils.getColorIndex(keyColor), ModUtils.getColorIndex(valueColor), ModUtils.getColorIndex(deathPosColor)};

        // keys
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, new TranslatableText("button.coordinatesdisplay.colors.keys", keyPrefix + ModUtils.getColor(keyColor)), (button) -> {
            if (indexes[0] == ModUtils.colors.length - 1) indexes[0] = 0;
            else indexes[0]++;

            CoordinatesDisplay.CONFIG.definitionColor = ModUtils.colors[indexes[0]];
            CoordinatesDisplay.parseColorPrefixes();

            String newColor = ModUtils.colors[indexes[0]];
            String newPrefix = ModUtils.getColorPrefix(newColor);

            button.setMessage(new TranslatableText("button.coordinatesdisplay.colors.keys", newPrefix + ModUtils.getColor(newColor)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, new TranslatableText("description.coordinatesdisplay.colors.key"), mouseX, mouseY);
            }
        }));

        // values
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight, new TranslatableText("button.coordinatesdisplay.colors.values", valuePrefix + ModUtils.getColor(valueColor)), (button) -> {
            if (indexes[1] == ModUtils.colors.length - 1) indexes[1] = 0;
            else indexes[1]++;

            CoordinatesDisplay.CONFIG.dataColor = ModUtils.colors[indexes[1]];
            CoordinatesDisplay.parseColorPrefixes();

            String newColor = ModUtils.colors[indexes[1]];
            String newPrefix = ModUtils.getColorPrefix(newColor);

            button.setMessage(new TranslatableText("button.coordinatesdisplay.colors.values", newPrefix + ModUtils.getColor(newColor)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, new TranslatableText("description.coordinatesdisplay.colors.value"), mouseX, mouseY);
            }
        }));

        // death pos
        this.addDrawableChild(new ButtonWidget(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight, new TranslatableText("button.coordinatesdisplay.colors.deathpos", deathPosPrefix + ModUtils.getColor(deathPosColor)), (button) -> {
            if (indexes[2] == ModUtils.colors.length - 1) indexes[2] = 0;
            else indexes[2]++;

            CoordinatesDisplay.CONFIG.deathPosColor = ModUtils.colors[indexes[2]];
            CoordinatesDisplay.parseColorPrefixes();

            String newColor = ModUtils.colors[indexes[2]];
            String newPrefix = ModUtils.getColorPrefix(newColor);

            button.setMessage(new TranslatableText("button.coordinatesdisplay.colors.deathpos", newPrefix + ModUtils.getColor(newColor)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, new TranslatableText("description.coordinatesdisplay.colors.deathpos"), mouseX, mouseY);
            }
        }));

    }

    @Override
    public void onClose() {
        this.client.setScreen(parent);
    }
}
