package me.boxadactle.coordinatesdisplay.gui.config;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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

    Vector3d pos;
    ChunkPos chunkPos;
    float cameraYaw;

    public VisualConfigScreen(Screen parent) {
        super(Component.translatable("screen.coordinatesdisplay.config.visual", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion().thisVersion()));
        this.parent = parent;

        this.pos = new Vector3d(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        this.chunkPos = new ChunkPos(new BlockPos(pos.x, pos.y, pos.z));
        this.cameraYaw = ModUtil.randomYaw();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX,  mouseY, delta);

        drawCenteredString(matrices, this.font, Component.translatable("screen.coordinatesdisplay.config.visual", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion().thisVersion()), this.width / 2, 5, ModUtil.WHITE);

        // padding
        drawString(matrices, this.font, Component.translatable("button.coordinatesdisplay.padding"), this.width / 2 - smallButtonW, start + (buttonHeight + p) * 3 + p, ModUtil.WHITE);

        // text padding
        drawString(matrices, this.font, Component.translatable("button.coordinatesdisplay.textpadding"), this.width / 2 + p, start + (buttonHeight + p) * 3 + p, ModUtil.WHITE);

        if (CoordinatesDisplay.CONFIG.get().visible) {
            CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, null, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), (int) (this.height / 1.8));
        }
    }

    protected void init() {
        super.init();

        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.back"), (button) -> this.minecraft.setScreen(parent)));

        initButtons();
        initTextFields();
    }

    private void initButtons() {
        // visible button
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.visible", CoordinatesDisplay.CONFIG.get().visible ? ModUtil.TRUE : ModUtil.FALSE), (button) -> {
            CoordinatesDisplay.CONFIG.get().visible = !CoordinatesDisplay.CONFIG.get().visible;
            button.setMessage(Component.translatable("button.coordinatesdisplay.visible", CoordinatesDisplay.CONFIG.get().visible ? ModUtil.TRUE : ModUtil.FALSE));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHoveredOrFocused()) {
                this.renderTooltip(matrices, Component.translatable("description.coordinatesdisplay.visible"), mouseX, mouseY);
            }
        }));

        // decimal rounding button
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + buttonHeight + p, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.decimal", (CoordinatesDisplay.CONFIG.get().decimalRounding ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {
            CoordinatesDisplay.CONFIG.get().decimalRounding = !CoordinatesDisplay.CONFIG.get().decimalRounding;
            button.setMessage(Component.translatable("button.coordinatesdisplay.decimal", (CoordinatesDisplay.CONFIG.get().decimalRounding ? ModUtil.TRUE : ModUtil.FALSE)));
        }, (button, matrices, mouseX, mouseY) -> {
            if (button.isHoveredOrFocused()) {
                this.renderTooltip(matrices, Component.translatable("description.coordinatesdisplay.decimal"), mouseX, mouseY);
            }
        }));

        // modify position button
        this.addRenderableWidget(new Button(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 2, largeButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.position"), (button) -> this.minecraft.setScreen(new ChangePositionScreen(this)), (button, matrices, mouseX, mouseY) -> {
            if (button.isHoveredOrFocused()) {
                this.renderTooltip(matrices, Component.translatable("description.coordinatesdisplay.position"), mouseX, mouseY);
            }
        }));

        // open wiki
        this.addRenderableWidget(new Button(5, 5, tinyButtonW, buttonHeight, Component.translatable("button.coordinatesdisplay.help"), (button) -> this.minecraft.setScreen(new ConfirmLinkScreen((yes) -> {
            this.minecraft.setScreen(this);
            if (yes) {
                Util.getPlatform().openUri(ModUtil.CONFIG_WIKI_VISUAL);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtil.CONFIG_WIKI_VISUAL, false))));
    }

    private void initTextFields() {
        EditBox padding = new EditBox(this.font, this.width / 2 - smallButtonW - p, start + (buttonHeight + p) * 4 - p * 2, smallButtonW, buttonHeight,
                Component.translatable(Integer.toString(CoordinatesDisplay.CONFIG.get().padding)));
        EditBox textPadding = new EditBox(this.font, this.width / 2 + p, start + (buttonHeight + p) * 4 - p * 2, smallButtonW, buttonHeight,
                Component.translatable(Integer.toString(CoordinatesDisplay.CONFIG.get().textPadding)));

        padding.setValue(Integer.toString(CoordinatesDisplay.CONFIG.get().padding));
        textPadding.setValue(Integer.toString(CoordinatesDisplay.CONFIG.get().textPadding));

        padding.setResponder((text) -> {
            if (!text.isEmpty()) {
                try {
                    CoordinatesDisplay.CONFIG.get().padding = Integer.parseInt(text);

                } catch (NumberFormatException e) {
                    CoordinatesDisplay.LOGGER.printStackTrace(e);
                }
            }
        });

        textPadding.setResponder((text) -> {
            if (!text.isEmpty()) {
                try {
                    CoordinatesDisplay.CONFIG.get().textPadding = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    CoordinatesDisplay.LOGGER.printStackTrace(e);
                }
            }
        });

        this.addRenderableWidget(padding);
        this.addRenderableWidget(textPadding);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}