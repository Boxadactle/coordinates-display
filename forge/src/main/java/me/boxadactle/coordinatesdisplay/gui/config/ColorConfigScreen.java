package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
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

    Vec3 pos;
    ChunkPos chunkPos;
    float cameraYaw;
    float cameraPitch;

    String deathx;
    String deathy;
    String deathz;

    public ColorConfigScreen(Screen parent) {
        super(Component.translatable("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION.getVersion()));
        this.parent = parent;

        this.pos = new Vec3(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos((int) Math.round(pos.x), (int) Math.round(pos.z));
        this.cameraYaw = ModUtil.randomYawPitch();
        this.cameraPitch = ModUtil.randomYawPitch();

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

        drawCenteredComponent(matrices, this.font, Component.translatable("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, CoordinatesDisplay.MOD_VERSION.getVersion()), this.width / 2, 5, ModUtil.WHITE);

        int y = (int) (this.height / 2.3);

        CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, cameraPitch, null, CoordinatesDisplay.CONFIG.get().minMode, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), y);

        Component pos = Component.translatable("message.coordinatesdisplay.location", deathx, deathy, deathz).withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().deathPosColor)));
        Component deathPos = Component.translatable("message.coordinatesdisplay.deathpos", pos);
        drawCenteredComponent(matrices, this.font, deathPos, this.width / 2, y - (CoordinatesDisplay.OVERLAY.getHeight() / 4), ModUtil.WHITE);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addRenderableWidget(new PlainTextButton(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.back"), (button) -> this.minecraft.setScreen(parent), Minecraft.getInstance().font));

        initButtons();
    }

    private void initButtons() {
        String keyColor = ModUtil.getColorIndex(CoordinatesDisplay.CONFIG.get().definitionColor) > 0 ?
                ModUtil.colors[ModUtil.getColorIndex(CoordinatesDisplay.CONFIG.get().definitionColor)] : "white";
        String valueColor = ModUtil.getColorIndex(CoordinatesDisplay.CONFIG.get().dataColor) > 0 ?
                ModUtil.colors[ModUtil.getColorIndex(CoordinatesDisplay.CONFIG.get().dataColor)] : "white";
        String deathPosColor = ModUtil.getColorIndex(CoordinatesDisplay.CONFIG.get().deathPosColor) > 0 ?
                ModUtil.colors[ModUtil.getColorIndex(CoordinatesDisplay.CONFIG.get().deathPosColor)] : "white";

        Component key = Component.translatable("coordinatesdisplay.color." + keyColor).withStyle(style -> style.withColor(ModUtil.getColorDecimal(keyColor)));
        Component value = Component.translatable("coordinatesdisplay.color." + valueColor).withStyle(style -> style.withColor(ModUtil.getColorDecimal(valueColor)));
        Component deathpos = Component.translatable("coordinatesdisplay.color." + deathPosColor).withStyle(style -> style.withColor(ModUtil.getColorDecimal(deathPosColor)));

        // lambdas are annoying
        final int[] indexes = {ModUtil.getColorIndex(keyColor), ModUtil.getColorIndex(valueColor), ModUtil.getColorIndex(deathPosColor)};

        // keys
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.colors.keys", key), (button) -> {
            if (indexes[0] == ModUtil.colors.length - 1) indexes[0] = 0;
            else indexes[0]++;

            CoordinatesDisplay.CONFIG.get().definitionColor = ModUtil.colors[indexes[0]];

            String newColor = ModUtil.colors[indexes[0]];

            button.setMessage(Component.translatable("button.coordinatesdisplay.colors.keys", Component.literal(ModUtil.getColor(newColor)))
                    .withStyle(style -> style.withColor(ModUtil.getColorDecimal(newColor))));
        }).bounds(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight).build());

        // values
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.colors.values", value), (button) -> {
            if (indexes[1] == ModUtil.colors.length - 1) indexes[1] = 0;
            else indexes[1]++;

            CoordinatesDisplay.CONFIG.get().dataColor = ModUtil.colors[indexes[1]];

            String newColor = ModUtil.colors[indexes[1]];

            button.setMessage(Component.translatable("button.coordinatesdisplay.colors.values", Component.literal(ModUtil.getColor(newColor)))
                    .withStyle(style -> style.withColor(ModUtil.getColorDecimal(newColor))));
        }).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight).build());

        // death pos
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.colors.deathpos", deathpos), (button) -> {
            if (indexes[2] == ModUtil.colors.length - 1) indexes[2] = 0;
            else indexes[2]++;

            CoordinatesDisplay.CONFIG.get().deathPosColor = ModUtil.colors[indexes[2]];

            String newColor = ModUtil.colors[indexes[2]];

            button.setMessage(Component.translatable("button.coordinatesdisplay.colors.deathpos", Component.literal(ModUtil.getColor(newColor))
                    .withStyle(style -> style.withColor(ModUtil.getColorDecimal(newColor)))));
        }).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight).build());

        // open wiki
        this.addRenderableWidget(new PlainTextButton(5, 5, tinyButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.help"), (button) -> this.minecraft.setScreen(new ConfirmLinkScreen((yes) -> {
            this.minecraft.setScreen(this);
            if (yes) {
                Util.getPlatform().openUri(ModUtil.CONFIG_WIKI_COLOR);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtil.CONFIG_WIKI_COLOR, false)), Minecraft.getInstance().font));

    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}