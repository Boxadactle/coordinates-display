package dev.boxadactle.coordinatesdisplay.config.screen;

import dev.boxadactle.boxlib.gui.BOptionScreen;
import dev.boxadactle.boxlib.gui.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.widget.button.BBooleanButton;
import dev.boxadactle.boxlib.gui.widget.label.BCenteredLabel;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.HudHelper;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.ModUtil;
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
        this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.background",
                config().renderBackground,
                newVal -> config().renderBackground = newVal
        ));

        // chunk pos
        ((BBooleanButton)this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.chunkpos",
                config().renderChunkData,
                newVal -> config().renderChunkData = newVal
        ))).active = ModUtil.or(config().renderMode, ModConfig.RenderMode.DEFAULT, ModConfig.RenderMode.MAXIMUM);

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
        direction.active = !(config().renderMode.equals(ModConfig.RenderMode.MINIMUM));
        directionint.active = !(config().renderMode.equals(ModConfig.RenderMode.LINE));
        this.addConfigLine(direction, directionint);

        // biome
        ((BBooleanButton)this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.biome",
                config().renderBiome,
                newVal -> config().renderBiome = newVal
        ))).active = ModUtil.or(config().renderMode, ModConfig.RenderMode.DEFAULT, ModConfig.RenderMode.MAXIMUM);

        // mc version
        ((BBooleanButton)this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.mcversion",
                config().renderMCVersion,
                newVal -> config().renderMCVersion = newVal
        ))).active = ModUtil.or(config().renderMode, ModConfig.RenderMode.MAXIMUM, ModConfig.RenderMode.DEFAULT);

        this.addConfigLine(new BSpacingEntry());

        // hud rendering
        this.addConfigLine(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.preview")));
        this.addConfigLine(this.createHudRenderEntry(pos));

        // since minecraft's scrolling panels can't handle different entry sizes
        for (int i = 0; i < (ModUtil.not(config().renderMode, ModConfig.RenderMode.MAXIMUM) ? 3 : 4); i++) {
            this.addConfigLine(new BSpacingEntry());
        }

    }
}
