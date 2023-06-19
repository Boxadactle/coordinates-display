package me.boxadactle.coordinatesdisplay.gui.config;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.gui.ConfigScreen;
import me.boxadactle.coordinatesdisplay.gui.widget.ConfigBooleanWidget;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderConfigScreen extends ConfigScreen {

    public RenderConfigScreen(Screen parent) {
        super(parent);

        super.generatePositionData();
        super.setTitle(Component.translatable("screen.coordinatesdisplay.config.render", CoordinatesDisplay.MOD_NAME, ModVersion.getVersion()));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);

        super.drawTitle(guiGraphics);

        CoordinatesDisplay.OVERLAY.render(guiGraphics, pos, chunkPos, cameraYaw, cameraPitch, null, this.width / 2 - (CoordinatesDisplay.OVERLAY.getWidth() / 2), (int) ((this.height / 2.1) + 35), CoordinatesDisplay.CONFIG.get().minMode, false);

        super.render(guiGraphics, mouseX,  mouseY, delta);
    }

    protected void init() {
        super.init();

        this.addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.back"), (button) -> this.minecraft.setScreen(parent)).bounds(this.width / 2 - largeButtonW / 2, this.height - buttonHeight - p, largeButtonW, buttonHeight).build());

        initButtons();
    }

    private void initButtons() {
        // background
        this.addRenderableWidget(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start,
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.render.background",
                CoordinatesDisplay.CONFIG.get().renderBackground,
                newValue -> CoordinatesDisplay.CONFIG.get().renderBackground = newValue
        ));

        // chunk data
        this.addRenderableWidget(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start + (buttonHeight + p),
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.render.chunkdata",
                CoordinatesDisplay.CONFIG.get().renderChunkData,
                newValue -> CoordinatesDisplay.CONFIG.get().renderChunkData = newValue
        )).active = !CoordinatesDisplay.CONFIG.get().minMode;

        // direction
        this.addRenderableWidget(new ConfigBooleanWidget(
                this.width / 2 - smallButtonW - p / 2,
                start + (buttonHeight + p) * 2,
                smallButtonW,
                buttonHeight,
                "button.coordinatesdisplay.render.direction",
                CoordinatesDisplay.CONFIG.get().renderDirection,
                newValue -> CoordinatesDisplay.CONFIG.get().renderDirection = newValue
        )).active = !CoordinatesDisplay.CONFIG.get().minMode;

        // direction int
        this.addRenderableWidget(new ConfigBooleanWidget(
                this.width / 2 + p / 2,
                start + (buttonHeight + p) * 2,
                smallButtonW,
                buttonHeight,
                "button.coordinatesdisplay.render.directionint",
                CoordinatesDisplay.CONFIG.get().renderDirectionInt,
                newValue -> CoordinatesDisplay.CONFIG.get().renderDirectionInt = newValue
        )).active = !CoordinatesDisplay.CONFIG.get().minMode;

        // biome
        this.addRenderableWidget(new ConfigBooleanWidget(
                this.width / 2 - smallButtonW - p / 2,
                start + (buttonHeight + p) * 3,
                smallButtonW,
                buttonHeight,
                "button.coordinatesdisplay.render.biome",
                CoordinatesDisplay.CONFIG.get().renderBiome,
                newValue -> CoordinatesDisplay.CONFIG.get().renderBiome = newValue
        )).active = !CoordinatesDisplay.CONFIG.get().minMode;

        // biome color
        this.addRenderableWidget(new ConfigBooleanWidget(
                this.width / 2 + p / 2,
                start + (buttonHeight + p) * 3,
                smallButtonW,
                buttonHeight,
                "button.coordinatesdisplay.render.biomecolor",
                CoordinatesDisplay.CONFIG.get().biomeColors,
                newValue -> CoordinatesDisplay.CONFIG.get().biomeColors = newValue
        ));

        // minecraft version
        this.addRenderableWidget(new ConfigBooleanWidget(
                this.width / 2 - largeButtonW / 2,
                start + (buttonHeight + p) * 4,
                largeButtonW,
                buttonHeight,
                "button.coordinatesdisplay.render.mcversion",
                CoordinatesDisplay.CONFIG.get().renderMCVersion,
                newValue -> CoordinatesDisplay.CONFIG.get().renderMCVersion = newValue
        )).active = !CoordinatesDisplay.CONFIG.get().minMode;    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}