package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

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

    int deathx;
    int deathy;
    int deathz;

    String dimension;

    public ColorConfigScreen(Screen parent) {
        super(Component.translatable("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
        this.parent = parent;

        this.pos = new Vec3(Math.random() * 1000d, Math.random() * 5d, Math.random() * 1000d);
        this.chunkPos = new ChunkPos(new BlockPos(ModUtil.doubleVecToIntVec(this.pos)));
        this.cameraYaw  = (float) Math.random() * 180;
        this.cameraPitch  = (float) Math.random() * 180;

        deathx = (int) Math.round(Math.random() * 1000);
        deathy = (int) Math.round(Math.random() * 100);
        deathz = (int) Math.round(Math.random() * 1000);

        dimension = (String) ModUtil.selectRandom("minecraft:overworld", "minecraft:the_nether", "minecraft:the_end");

        CoordinatesDisplay.shouldRenderOnHud = false;

    }

    private void renderColorPicker(PoseStack matrices) {
        float s = 1.2F;
        matrices.pushPose();
        matrices.scale(s, s, s);

        int a = (int) (this.width / s);
        int b = (int) (this.height / s) + 75;
        int c = (int) (a / 2 + (5 / s));
        int d = (int) (b / 2.3) - 4;
        int e = (int) (872 / 1.8) / this.nonZeroGuiScale() / 4;
        int f = (int) (586 / 1.8) / this.nonZeroGuiScale() / 4;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, new ResourceLocation("coordinatesdisplay", "textures/color_picker.png"));

        blit(matrices, c, d, 0.0F, 0.0F, e, f, e, f);

        matrices.popPose();
    }

    private int nonZeroGuiScale() {
        int scale = Minecraft.getInstance().options.guiScale().get();
        if (scale == 0) {
            // This formula copied from the Minecraft wiki
            return (int) Math.max(1, Math.min(Math.floor(this.width / 320), Math.floor(this.height / 240)));
        } else {
            return scale;
        }
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredString(matrices, this.font, Component.translatable("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()), this.width / 2, 5, ModUtil.WHITE);

        int y = (int) (this.height / 2.3);

        drawString(matrices, this.font, Component.translatable("button.coordinatesidisplay.colors.definition"), this.width / 2 - smallButtonW, start + 8, ModUtil.WHITE);
        drawString(matrices, this.font, Component.translatable("button.coordinatesidisplay.colors.data"), this.width / 2, start + 8, ModUtil.WHITE);
        drawString(matrices, this.font, Component.translatable("button.coordinatesidisplay.colors.deathpos"), this.width / 2 - smallButtonW - p, start + 8 + (buttonHeight + p) * 2, ModUtil.WHITE);
        drawString(matrices, this.font, Component.translatable("button.coordinatesidisplay.colors.background"), this.width / 2, start + 8 + (buttonHeight + p) * 2, ModUtil.WHITE);

        CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, cameraPitch, null, this.width / 2 - CoordinatesDisplay.OVERLAY.getWidth() - 5, y + 40, CoordinatesDisplay.CONFIG.get().minMode, false);

        Component posT = ModUtil.colorize(ComponentUtils.wrapInSquareBrackets(Component.translatable("message.coordinatesdisplay.deathlocation", deathx, deathy, deathz, dimension)), CoordinatesDisplay.CONFIG.get().deathPosColor);
        Component deathPos = Component.translatable("message.coordinatesdisplay.deathpos", posT);
        drawCenteredString(matrices, this.font, deathPos, this.width / 2, y - (CoordinatesDisplay.OVERLAY.getHeight() / 4) + 40, ModUtil.WHITE);

        this.renderColorPicker(matrices);

        super.render(matrices, mouseX,  mouseY, delta);
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
