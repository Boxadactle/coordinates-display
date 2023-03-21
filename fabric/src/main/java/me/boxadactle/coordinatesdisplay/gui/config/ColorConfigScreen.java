package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.systems.RenderSystem;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
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

    String dimension;

    String version;

    public ColorConfigScreen(Screen parent) {
        super(Text.translatable("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
        this.parent = parent;

        this.pos = new Vec3d(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos(new BlockPos((int)pos.getX(), (int)pos.getY(), (int)pos.getZ()));
        this.cameraYaw  = (float) Math.random() * 180;

        version = ModVersion.getVersion();

        DecimalFormat d = new DecimalFormat("0.00");

        deathx = (int) Math.round(Math.random() * 1000);
        deathy = (int) Math.round(Math.random() * 100);
        deathz = (int) Math.round(Math.random() * 1000);

        dimension = (String) ModUtils.selectRandom("minecraft:overworld", "minecraft:the_nether", "minecraft:the_end");

        CoordinatesDisplay.shouldRenderOnHud = false;

    }

    private void renderColorPicker(MatrixStack matrices) {
        float s = 1.3F;
        matrices.push();
        matrices.scale(s, s, s);

        int a = (int) (this.width / s);
        int b = (int) (this.height / s);
        int c = (int) (a / 2 + (5 / s));
        int d = (int) (b / 2.3) - 4;
        int e = (int) (872 / 1.8) / this.nonZeroGuiScale();
        int f = (int) (586 / 1.8) / this.nonZeroGuiScale();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, new Identifier("coordinatesdisplay", "textures/color_picker.png"));

        drawTexture(matrices, c, d, 0.0F, 0.0F, e, f, e, f);

        matrices.pop();
    }

    private int nonZeroGuiScale() {
        int scale = this.client.options.getGuiScale().getValue();
        if (scale == 0) {
            // This formula copied from the Minecraft wiki
            return (int) Math.max(1, Math.min(Math.floor(this.width / 320), Math.floor(this.height / 240)));
        } else {
            return scale;
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredTextWithShadow(matrices, this.textRenderer, Text.translatable("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, version), this.width / 2, 5, ModUtils.WHITE);

        int y = (int) (this.height / 2.3);

        drawTextWithShadow(matrices, this.textRenderer, Text.literal("Definition Color"), this.width / 2 - smallButtonW, start + 8, ModUtils.WHITE);
        drawTextWithShadow(matrices, this.textRenderer, Text.literal("Data Color"), this.width / 2, start + 8, ModUtils.WHITE);
        drawTextWithShadow(matrices, this.textRenderer, Text.literal("Death Position Color"), this.width / 2 - smallButtonW - p, start + 8 + (buttonHeight + p) * 2, ModUtils.WHITE);
        drawTextWithShadow(matrices, this.textRenderer, Text.literal("Background Color (ARGB)"), this.width / 2, start + 8 + (buttonHeight + p) * 2, ModUtils.WHITE);

        CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, null, this.width / 2 - CoordinatesDisplay.OVERLAY.getWidth() - 5, y);

        Text posT = Texts.bracketed(Text.translatable("message.coordinatesdisplay.deathlocation", deathx, deathy, deathz, dimension)).styled(style -> style.withColor(CoordinatesDisplay.CONFIG.deathPosColor));
        Text deathPos = Text.translatable("message.coordinatesdisplay.deathpos", posT);
        drawCenteredTextWithShadow(matrices, this.textRenderer, deathPos, this.width / 2, y - (CoordinatesDisplay.OVERLAY.getHeight() / 4), ModUtils.WHITE);

        this.renderColorPicker(matrices);

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addDrawableChild(new PressableTextWidget(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.back"), (button) -> this.close(), MinecraftClient.getInstance().textRenderer));

        // open wiki
        this.addDrawableChild(new PressableTextWidget(5, 5, tinyButtonW, buttonHeight, Text.translatable("button.coordinatesdisplay.help"), (button) -> this.client.setScreen(new ConfirmLinkScreen((yes) -> {
            this.client.setScreen(this);
            if (yes) {
                Util.getOperatingSystem().open(ModUtils.CONFIG_WIKI_COLOR);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtils.CONFIG_WIKI_COLOR, false)), MinecraftClient.getInstance().textRenderer));

        initButtons();
    }

    private void initButtons() {
        TextFieldWidget definitionColor = new TextFieldWidget(this.textRenderer, this.width / 2 - smallButtonW - p, start + (buttonHeight + p), smallButtonW, buttonHeight, Text.literal(Integer.toHexString(CoordinatesDisplay.CONFIG.definitionColor)));
        TextFieldWidget dataColor = new TextFieldWidget(this.textRenderer, this.width / 2 + p, start + (buttonHeight + p), smallButtonW, buttonHeight, Text.literal(Integer.toHexString(CoordinatesDisplay.CONFIG.definitionColor)));
        TextFieldWidget deathposColor = new TextFieldWidget(this.textRenderer, this.width / 2 - smallButtonW - p, start + (buttonHeight + p) * 3, smallButtonW, buttonHeight, Text.literal(Integer.toHexString(CoordinatesDisplay.CONFIG.definitionColor)));
        TextFieldWidget backgroundColor = new TextFieldWidget(this.textRenderer, this.width / 2 + p, start + (buttonHeight + p) * 3, smallButtonW, buttonHeight, Text.literal(Integer.toHexString(CoordinatesDisplay.CONFIG.definitionColor)));

        definitionColor.setMaxLength(6);
        dataColor.setMaxLength(6);
        deathposColor.setMaxLength(6);
        backgroundColor.setMaxLength(8);

        definitionColor.setText(Integer.toHexString(CoordinatesDisplay.CONFIG.definitionColor));
        dataColor.setText(Integer.toHexString(CoordinatesDisplay.CONFIG.dataColor));
        deathposColor.setText(Integer.toHexString(CoordinatesDisplay.CONFIG.deathPosColor));
        backgroundColor.setText(Integer.toHexString(CoordinatesDisplay.CONFIG.backgroundColor));

        definitionColor.setChangedListener((txt) -> {
            if (txt.isEmpty()) return;
            try {
                CoordinatesDisplay.CONFIG.definitionColor = Integer.valueOf(txt.replaceAll("#", ""), 16);
            } catch (NumberFormatException e) {
                CoordinatesDisplay.LOGGER.error("Why you put invalid hex code?");
                CoordinatesDisplay.LOGGER.printStackTrace(e);
            }
        });

        dataColor.setChangedListener((txt) -> {
            if (txt.isEmpty()) return;
            try {
                CoordinatesDisplay.CONFIG.dataColor = Integer.valueOf(txt.replaceAll("#", ""), 16);
            } catch (NumberFormatException e) {
                CoordinatesDisplay.LOGGER.error("Why you put invalid hex code?");
                CoordinatesDisplay.LOGGER.printStackTrace(e);
            }
        });

        deathposColor.setChangedListener((txt) -> {
            if (txt.isEmpty()) return;
            try {
                CoordinatesDisplay.CONFIG.deathPosColor = Integer.valueOf(txt.replaceAll("#", ""), 16);
            } catch (NumberFormatException e) {
                CoordinatesDisplay.LOGGER.error("Why you put invalid hex code?");
                CoordinatesDisplay.LOGGER.printStackTrace(e);
            }
        });

        backgroundColor.setChangedListener((txt) -> {
            if (txt.isEmpty()) return;
            try {
                // Need to use parseUnsignedInt in order to get ARGB values over 0x7fffffff to work
                CoordinatesDisplay.CONFIG.backgroundColor = Integer.parseUnsignedInt(txt.replaceAll("#", ""), 16);
            } catch (NumberFormatException e) {
                CoordinatesDisplay.LOGGER.error("Why you put invalid hex code?");
                CoordinatesDisplay.LOGGER.printStackTrace(e);
            }
        });

        this.addDrawableChild(definitionColor);
        this.addDrawableChild(dataColor);
        this.addDrawableChild(deathposColor);
        this.addDrawableChild(backgroundColor);

        this.addDrawableChild(new PressableTextWidget(this.width / 2 - smallButtonW - p, start + 8 + (buttonHeight + p) * 4, smallButtonW, buttonHeight, Text.literal("Color Picker..."), (button -> {
            Util.getOperatingSystem().open("https://htmlcolorcodes.com/color-picker/");
        }), MinecraftClient.getInstance().textRenderer));

    }

    @Override
    public void close() {
        this.client.setScreen(parent);
        CoordinatesDisplay.shouldRenderOnHud = true;
    }
}
