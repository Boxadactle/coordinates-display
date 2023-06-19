package me.boxadactle.coordinatesdisplay.gui.config;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.gui.ConfigScreen;
import me.boxadactle.coordinatesdisplay.gui.widget.ConfigBooleanWidget;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

public class RenderScreen extends ConfigScreen {

    public RenderScreen(Screen parent) {
        super(parent);
        this.parent = parent;

        super.generatePositionData();
        super.setTitle(Text.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));

        CoordinatesDisplay.shouldRenderOnHud = false;
    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);

        super.drawTitle(drawContext);

        CoordinatesDisplay.OVERLAY.render(drawContext, pos, chunkPos, cameraYaw, cameraPitch, null, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), (int) (this.height / 2.1) + 10, CoordinatesDisplay.CONFIG.get().minMode, false);

        super.render(drawContext, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.back"), (button) -> this.close()).dimensions(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight).build());

        // open wiki
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.help"), (button) -> this.client.setScreen(new ConfirmLinkScreen((yes) -> {
            this.client.setScreen(this);
            if (yes) {
                Util.getOperatingSystem().open(ModUtil.CONFIG_WIKI_RENDER);
                CoordinatesDisplay.LOGGER.info("Opened link");
            }
        }, ModUtil.CONFIG_WIKI_RENDER, false))).dimensions(5, 5, tinyButtonW, buttonHeight).build());

        this.initConfigScreen();
    }

    public void initConfigScreen() {
        // background
        this.addDrawableChild(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start,
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.render.background",
                CoordinatesDisplay.CONFIG.get().renderBackground,
                newValue -> CoordinatesDisplay.CONFIG.get().renderBackground = newValue
        ));

        // chunk data
        this.addDrawableChild(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start + (buttonHeight + p),
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.render.chunkdata",
                CoordinatesDisplay.CONFIG.get().renderChunkData,
                newValue -> CoordinatesDisplay.CONFIG.get().renderChunkData = newValue
        )).active = !CoordinatesDisplay.CONFIG.get().minMode;

        // direction
        this.addDrawableChild(new ConfigBooleanWidget(
                this.width / 2 - smallButtonW - p / 2,
                start + (buttonHeight + p) * 2,
                smallButtonW,
                buttonHeight,
                "button.coordinatesdisplay.render.direction",
                CoordinatesDisplay.CONFIG.get().renderDirection,
                newValue -> CoordinatesDisplay.CONFIG.get().renderDirection = newValue
        )).active = !CoordinatesDisplay.CONFIG.get().minMode;

        // direction int
        this.addDrawableChild(new ConfigBooleanWidget(
                this.width / 2 + p / 2,
                start + (buttonHeight + p) * 2,
                smallButtonW,
                buttonHeight,
                "button.coordinatesdisplay.render.directionint",
                CoordinatesDisplay.CONFIG.get().renderDirectionInt,
                newValue -> CoordinatesDisplay.CONFIG.get().renderDirectionInt = newValue
        )).active = !CoordinatesDisplay.CONFIG.get().minMode;

        // biome
        this.addDrawableChild(new ConfigBooleanWidget(
                this.width / 2 - smallButtonW - p / 2,
                start + (buttonHeight + p) * 3,
                smallButtonW,
                buttonHeight,
                "button.coordinatesdisplay.render.biome",
                CoordinatesDisplay.CONFIG.get().renderBiome,
                newValue -> CoordinatesDisplay.CONFIG.get().renderBiome = newValue
        )).active = !CoordinatesDisplay.CONFIG.get().minMode;

        // biome color
        this.addDrawableChild(new ConfigBooleanWidget(
                this.width / 2 + p / 2,
                start + (buttonHeight + p) * 3,
                smallButtonW,
                buttonHeight,
                "button.coordinatesdisplay.render.biomecolor",
                CoordinatesDisplay.CONFIG.get().biomeColors,
                newValue -> CoordinatesDisplay.CONFIG.get().biomeColors = newValue
        ));

        // minecraft version
        this.addDrawableChild(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start + (buttonHeight + p) * 4,
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.render.mcversion",
                CoordinatesDisplay.CONFIG.get().renderMCVersion,
                newValue -> CoordinatesDisplay.CONFIG.get().renderMCVersion = newValue
        )).active = !CoordinatesDisplay.CONFIG.get().minMode;
    }

    public void close() {
        this.client.setScreen(parent);
        CoordinatesDisplay.shouldRenderOnHud = true;
    }
}
