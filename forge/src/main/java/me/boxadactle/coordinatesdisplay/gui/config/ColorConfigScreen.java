package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.Util;
<<<<<<< Updated upstream
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
=======
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
>>>>>>> Stashed changes
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
<<<<<<< Updated upstream
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
=======
import net.minecraft.world.phys.Vec3;
>>>>>>> Stashed changes

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

    Vector3d pos;
    ChunkPos chunkPos;
    float cameraYaw;

    int deathx;
    int deathy;
    int deathz;

    String dimension;

    String version;

    public ColorConfigScreen(Screen parent) {
        super(Component.translatable("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion().thisVersion()));
        this.parent = parent;

<<<<<<< Updated upstream
        this.pos = new Vector3d(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos(new BlockPos(pos.x, pos.y, pos.z));
        this.cameraYaw = ModUtils.randomYaw();
=======
        this.pos = new Vec3(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos((int)Math.round(this.pos.x), (int)Math.round(this.pos.z));
        this.cameraYaw  = (float) Math.random() * 180;
        this.cameraPitch  = (float) Math.random() * 180;

        version = CoordinatesDisplay.MOD_VERSION.getVersion();
>>>>>>> Stashed changes

        DecimalFormat d = new DecimalFormat("0.00");

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
        int f = (int) (586 / 1.8) / this.nonZeroGuiScale() /4;

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

<<<<<<< Updated upstream
        drawCenteredComponent(matrices, this.font, Component.translatable("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion().thisVersion()), this.width / 2, 5, ModUtils.WHITE);

        int y = (int) (this.height / 2.3);

        CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, null, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), y);

        Component pos = Component.translatable("message.coordinatesdisplay.location", deathx, deathy, deathz).withStyle(style -> style.withColor(ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().deathPosColor)));
        Component deathPos = Component.translatable("message.coordinatesdisplay.deathpos", pos);
        drawCenteredComponent(matrices, this.font, deathPos, this.width / 2, y - (CoordinatesDisplay.OVERLAY.getHeight() / 4), ModUtils.WHITE);
=======
        drawCenteredString(matrices, this.font, Component.translatable("screen.coordinatesdisplay.config.color", CoordinatesDisplay.MOD_NAME, version), this.width / 2, 5, ModUtil.WHITE);

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
>>>>>>> Stashed changes

        super.render(matrices, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

<<<<<<< Updated upstream
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.back"), (button) -> this.minecraft.setScreen(parent)));

        initButtons();
    }

    private void initButtons() {
        String keyColor = ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.get().definitionColor) > 0 ?
                ModUtils.colors[ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.get().definitionColor)] : "white";
        String valueColor = ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.get().dataColor) > 0 ?
                ModUtils.colors[ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.get().dataColor)] : "white";
        String deathPosColor = ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.get().deathPosColor) > 0 ?
                ModUtils.colors[ModUtils.getColorIndex(CoordinatesDisplay.CONFIG.get().deathPosColor)] : "white";

        Component key = Component.translatable("coordinatesdisplay.color." + keyColor).withStyle(style -> style.withColor(ModUtils.getColorDecimal(keyColor)));
        Component value = Component.translatable("coordinatesdisplay.color." + valueColor).withStyle(style -> style.withColor(ModUtils.getColorDecimal(valueColor)));
        Component deathpos = Component.translatable("coordinatesdisplay.color." + deathPosColor).withStyle(style -> style.withColor(ModUtils.getColorDecimal(deathPosColor)));

        // lambdas are annoying
        final int[] indexes = {ModUtils.getColorIndex(keyColor), ModUtils.getColorIndex(valueColor), ModUtils.getColorIndex(deathPosColor)};

        // keys
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.colors.keys", key), (button) -> {
            if (indexes[0] == ModUtils.colors.length - 1) indexes[0] = 0;
            else indexes[0]++;

            CoordinatesDisplay.CONFIG.get().definitionColor = ModUtils.colors[indexes[0]];

            String newColor = ModUtils.colors[indexes[0]];

            button.setMessage(Component.translatable("button.coordinatesdisplay.colors.keys", Component.literal(ModUtils.getColor(newColor)))
                    .withStyle(style -> style.withColor(ModUtils.getColorDecimal(newColor))));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHoveredOrFocused()) {
                this.renderTooltip(matrices, Component.translatable("description.coordinatesdisplay.colors.key"), mouseX, mouseY);
            }
        }));

        // values
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p), largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.colors.values", value), (button) -> {
            if (indexes[1] == ModUtils.colors.length - 1) indexes[1] = 0;
            else indexes[1]++;

            CoordinatesDisplay.CONFIG.get().dataColor = ModUtils.colors[indexes[1]];

            String newColor = ModUtils.colors[indexes[1]];

            button.setMessage(Component.translatable("button.coordinatesdisplay.colors.values", Component.literal(ModUtils.getColor(newColor)))
                    .withStyle(style -> style.withColor(ModUtils.getColorDecimal(newColor))));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHoveredOrFocused()) {
                this.renderTooltip(matrices, Component.translatable("description.coordinatesdisplay.colors.value"), mouseX, mouseY);
            }
        }));

        // death pos
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.colors.deathpos", deathpos), (button) -> {
            if (indexes[2] == ModUtils.colors.length - 1) indexes[2] = 0;
            else indexes[2]++;

            CoordinatesDisplay.CONFIG.get().deathPosColor = ModUtils.colors[indexes[2]];

            String newColor = ModUtils.colors[indexes[2]];

            button.setMessage(Component.translatable("button.coordinatesdisplay.colors.deathpos", Component.literal(ModUtils.getColor(newColor))
                    .withStyle(style -> style.withColor(ModUtils.getColorDecimal(newColor)))));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHoveredOrFocused()) {
                this.renderTooltip(matrices, Component.translatable("description.coordinatesdisplay.colors.deathpos"), mouseX, mouseY);
            }
        }));

        // open wiki
        this.addRenderableWidget(new Button(5, 5, tinyButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.help"), (button) -> this.minecraft.setScreen(new ConfirmLinkScreen((yes) -> {
            this.minecraft.setScreen(this);
=======
        this.addRenderableWidget(new PlainTextButton(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.back"), (button) -> this.onClose(), Minecraft.getInstance().font));

        // open wiki
        this.addRenderableWidget(new PlainTextButton(5, 5, tinyButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.help"), (button) -> Minecraft.getInstance().setScreen(new ConfirmLinkScreen((yes) -> {
            Minecraft.getInstance().setScreen(this);
>>>>>>> Stashed changes
            if (yes) {
                Util.getPlatform().openUri(ModUtils.CONFIG_WIKI_COLOR);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtils.CONFIG_WIKI_COLOR, false))));

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
