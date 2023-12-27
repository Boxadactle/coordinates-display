package dev.boxadactle.coordinatesdisplay.config.screen;

import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BBooleanButton;
import dev.boxadactle.boxlib.gui.config.widget.label.BCenteredLabel;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.HudHelper;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.CoordinatesHuds;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class RenderScreen extends BOptionScreen implements HudHelper {

    Position pos;

    public RenderScreen(Screen parent) {
        super(parent);

        pos = this.generatePositionData();
    }

    @Override
    protected Component getName() {
        return Component.translatable("screen.coordinatesdisplay.render", CoordinatesDisplay.VERSION_STRING);
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.setSaveButton(createBackButton(startX, startY, parent));

        this.setWiki(Component.translatable("button.coordinatesdisplay.wiki"), CoordinatesDisplay.WIKI_RENDER);
    }

    @Override
    protected void initConfigButtons() {

        // background
        ((BBooleanButton)this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.background",
                config().renderBackground,
                newVal -> config().renderBackground = newVal
        ))).active = CoordinatesHuds.getRenderer(config().renderMode).getMetadata().hasBackground();

        // XYZ
        ((BBooleanButton)this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.xyz",
                config().renderXYZ,
                newVal -> config().renderXYZ = newVal
        ))).active = CoordinatesHuds.getRenderer(config().renderMode).getMetadata().hasXYZ();

        // chunk pos
        ((BBooleanButton)this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.chunkpos",
                config().renderChunkData,
                newVal -> config().renderChunkData = newVal
        ))).active = CoordinatesHuds.getRenderer(config().renderMode).getMetadata().hasChunkData();

        // direction
        BBooleanButton direction = new BBooleanButton(
                "button.coordinatesdisplay.direction",
                config().renderDirection,
                newVal -> config().renderDirection = newVal
        );

        // direction int
        BBooleanButton directionint = new BBooleanButton(
                "button.coordinatesdisplay.directionint",
                config().renderDirectionInt,
                newVal -> config().renderDirectionInt = newVal
        );

        // add them
        direction.active = CoordinatesHuds.getRenderer(config().renderMode).getMetadata().hasDirection();
        directionint.active = CoordinatesHuds.getRenderer(config().renderMode).getMetadata().hasDirectionInt();
        this.addConfigLine(direction, directionint);

        // biome
        ((BBooleanButton)this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.biome",
                config().renderBiome,
                newVal -> config().renderBiome = newVal
        ))).active = CoordinatesHuds.getRenderer(config().renderMode).getMetadata().hasBiome();

        // mc version
        ((BBooleanButton)this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.mcversion",
                config().renderMCVersion,
                newVal -> config().renderMCVersion = newVal
        ))).active = CoordinatesHuds.getRenderer(config().renderMode).getMetadata().hasMCVersion();

        this.addConfigLine(new BSpacingEntry());

        // hud rendering
        this.addConfigLine(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.preview")));
        this.addConfigLine(this.createHudRenderEntry(pos));

        // since minecraft's scrolling panels can't handle different entry sizes
        for (int i = 0; i < 4; i++) {
            this.addConfigLine(new BSpacingEntry());
        }

    }
}
