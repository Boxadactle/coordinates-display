package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.text.DecimalFormat;

@OnlyIn(Dist.CLIENT)
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

    Vector3d pos;
    ChunkPos chunkPos;
    float cameraYaw;

    String deathx;
    String deathy;
    String deathz;

    public ColorConfigScreen(Screen parent) {
        super(new TranslatableComponent("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion().thisVersion()));
        this.parent = parent;

        this.pos = new Vector3d(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos(new BlockPos(pos.x, pos.y, pos.z));
        this.cameraYaw = ModUtils.randomYaw();

        DecimalFormat d = new DecimalFormat("0.00");

        deathx = d.format(Math.random() * 1000);
        deathy = d.format(Math.random() * 100);
        deathz = d.format(Math.random() * 1000);
    }

    private void drawCenteredComponent(PoseStack matrices, Font font, Component text, int centerX, int ypos, int color) {
        font.draw(matrices, text, centerX - font.width(text.getString()) / 2.0F, ypos, color);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredComponent(matrices, this.font, new TranslatableComponent("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion().thisVersion()), this.width / 2, 5, ModUtils.WHITE);

        int y = (int) (this.height / 2.3);

        CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, null, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), y);

        String pos = CoordinatesDisplay.DeathposColorPrefix + new TranslatableComponent("message.coordinatesdisplay.location", deathx, deathy, deathz).getString();
        Component deathPos = new TranslatableComponent("message.coordinatesdisplay.deathpos", pos);
        drawCenteredComponent(matrices, this.font, deathPos, this.width / 2, y - (CoordinatesDisplay.OVERLAY.getHeight() / 4), ModUtils.WHITE);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight, new TranslatableComponent("button.coordinatesdisplay.back"), (button) -> this.minecraft.setScreen(parent)));

        initButtons();
    }

    private void initButtons() {
        String keyColor = ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.get().definitionColor) > 0 ?
                ModUtils.colors[ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.get().definitionColor)] : "white";
        String valueColor = ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.get().dataColor) > 0 ?
                ModUtils.colors[ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.get().dataColor)] : "white";
        String deathPosColor = ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.get().deathPosColor) > 0 ?
                ModUtils.colors[ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.get().deathPosColor)] : "white";

        String keyPrefix = ModUtils.getColorPrefix(keyColor);
        String valuePrefix = ModUtils.getColorPrefix(valueColor);
        String deathPosPrefix = ModUtils.getColorPrefix(deathPosColor);

        // lambdas are annoying
        final int[] indexes = {ModUtils.getColorIndex(keyColor), ModUtils.getColorIndex(valueColor), ModUtils.getColorIndex(deathPosColor)};

        // keys
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, new TranslatableComponent("button.coordinatesdisplay.colors.keys", keyPrefix + ModUtils.getColor(keyColor)), (button) -> {
            if (indexes[0] == ModUtils.colors.length - 1) indexes[0] = 0;
            else indexes[0]++;

            CoordinatesDisplay.CONFIG.get().definitionColor = ModUtils.colors[indexes[0]];
            CoordinatesDisplay.parseColorPrefixes();

            String newColor = ModUtils.colors[indexes[0]];
            String newPrefix = ModUtils.getColorPrefix(newColor);

            button.setMessage(new TranslatableComponent("button.coordinatesdisplay.colors.keys", newPrefix + ModUtils.getColor(newColor)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, new TranslatableComponent("description.coordinatesdisplay.colors.key"), mouseX, mouseY);
            }
        }));

        // values
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight, new TranslatableComponent("button.coordinatesdisplay.colors.values", valuePrefix + ModUtils.getColor(valueColor)), (button) -> {
            if (indexes[1] == ModUtils.colors.length - 1) indexes[1] = 0;
            else indexes[1]++;

            CoordinatesDisplay.CONFIG.get().dataColor = ModUtils.colors[indexes[1]];
            CoordinatesDisplay.parseColorPrefixes();

            String newColor = ModUtils.colors[indexes[1]];
            String newPrefix = ModUtils.getColorPrefix(newColor);

            button.setMessage(new TranslatableComponent("button.coordinatesdisplay.colors.values", newPrefix + ModUtils.getColor(newColor)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, new TranslatableComponent("description.coordinatesdisplay.colors.value"), mouseX, mouseY);
            }
        }));

        // death pos
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight, new TranslatableComponent("button.coordinatesdisplay.colors.deathpos", deathPosPrefix + ModUtils.getColor(deathPosColor)), (button) -> {
            if (indexes[2] == ModUtils.colors.length - 1) indexes[2] = 0;
            else indexes[2]++;

            CoordinatesDisplay.CONFIG.get().deathPosColor = ModUtils.colors[indexes[2]];
            CoordinatesDisplay.parseColorPrefixes();

            String newColor = ModUtils.colors[indexes[2]];
            String newPrefix = ModUtils.getColorPrefix(newColor);

            button.setMessage(new TranslatableComponent("button.coordinatesdisplay.colors.deathpos", newPrefix + ModUtils.getColor(newColor)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHovered()) {
                this.renderTooltip(matrices, new TranslatableComponent("description.coordinatesdisplay.colors.deathpos"), mouseX, mouseY);
            }
        }));

        // open wiki
        this.addRenderableWidget(new Button(5, 5, tinyButtonW, buttonHeight, new TranslatableComponent("button.coordinatesdisplay.help"), (button) -> this.minecraft.setScreen(new ConfirmLinkScreen((yes) -> {
            this.minecraft.setScreen(this);
            if (yes) {
                Util.getPlatform().openUri(ModUtils.CONFIG_WIKI_COLOR);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtils.CONFIG_WIKI_COLOR, false))));

    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}