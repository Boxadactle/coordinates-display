package me.boxadactle.coordinatesdisplay.gui.config;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.gui.ConfigScreen;
import me.boxadactle.coordinatesdisplay.gui.widget.ConfigBooleanWidget;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VisualConfigScreen extends ConfigScreen {

    public VisualConfigScreen(Screen parent) {
        super(parent);

        super.generatePositionData();
        super.setTitle(Component.translatable("screen.coordinatesdisplay.config.visual", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);

        super.render(guiGraphics, mouseX,  mouseY, delta);

        super.drawTitle(guiGraphics);

        // padding
        guiGraphics.drawString(this.font, Component.translatable("button.coordinatesdisplay.padding"), this.width / 2 - smallButtonW, start + (buttonHeight + p) * 5 + p, ModUtil.WHITE);

        // text padding
        guiGraphics.drawString(this.font, Component.translatable("button.coordinatesdisplay.textpadding"), this.width / 2 + p, start + (buttonHeight + p) * 5 + p, ModUtil.WHITE);

        if (CoordinatesDisplay.CONFIG.get().visible) {
            CoordinatesDisplay.OVERLAY.render(guiGraphics, pos, chunkPos, cameraYaw, cameraPitch, null, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), (int) (this.height / 1.8) + 40, CoordinatesDisplay.CONFIG.get().minMode, false);
        }
    }

    protected void init() {
        super.init();

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.back"), (button) -> this.minecraft.setScreen(parent)).bounds(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight).build());

        initButtons();
        initTextFields();
    }

    private void initButtons() {
        // visible button
        this.addRenderableWidget(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start,
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.visible",
                CoordinatesDisplay.CONFIG.get().visible,
                newValue -> CoordinatesDisplay.CONFIG.get().visible = newValue
        ));

        // decimal rounding button
        Button a = new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start + buttonHeight + p,
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.decimal",
                CoordinatesDisplay.CONFIG.get().decimalRounding,
                newValue -> CoordinatesDisplay.CONFIG.get().decimalRounding = newValue
        );
        a.active = !CoordinatesDisplay.CONFIG.get().minMode;
        this.addRenderableWidget(a);

        // minimum mode button
        this.addRenderableWidget(new ConfigBooleanWidget(
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
        this.addRenderableWidget(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start + (buttonHeight + p) * 3,
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.textshadow",
                CoordinatesDisplay.CONFIG.get().hudTextShadow,
                newValue -> CoordinatesDisplay.CONFIG.get().hudTextShadow = newValue
        ));

        // modify position button
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.position"), (button) -> this.minecraft.setScreen(new HudPositionScreen(this))).bounds(this.width / 2 - largeButtonW / 2, start + (buttonHeight + p) * 4, largeButtonW, buttonHeight).build());

        // open wiki
        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.help"), (button) -> this.minecraft.setScreen(new ConfirmLinkScreen((yes) -> {
            this.minecraft.setScreen(this);
            if (yes) {
                Util.getPlatform().openUri(ModUtil.CONFIG_WIKI_VISUAL);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtil.CONFIG_WIKI_VISUAL, false))).bounds(5, 5, tinyButtonW, buttonHeight).build());
    }

    private void initTextFields() {
        EditBox padding = new EditBox(this.font, this.width / 2 - smallButtonW - p, start + (buttonHeight + p) * 6 - p * 2, smallButtonW, buttonHeight,
                Component.translatable(Integer.toString(CoordinatesDisplay.CONFIG.get().padding)));
        EditBox textPadding = new EditBox(this.font, this.width / 2 + p, start + (buttonHeight + p) * 6 - p * 2, smallButtonW, buttonHeight,
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