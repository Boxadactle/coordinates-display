package dev.boxadactle.coordinatesdisplay.gui.config;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.gui.BConfigScreen;
import dev.boxadactle.boxlib.gui.widget.BBooleanButton;
import dev.boxadactle.boxlib.gui.widget.BCenteredLabel;
import dev.boxadactle.boxlib.gui.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.widget.BWidgetContainer;
import dev.boxadactle.coordinatesdisplay.gui.HudHelper;
import dev.boxadactle.coordinatesdisplay.util.ModConfig;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class RenderScreen extends BConfigScreen implements HudHelper {

    Position pos;

    public RenderScreen(Screen parent) {
        super(parent);

        pos = this.generatePositionData();
    }

    @Override
    protected Component getName() {
        return Component.translatable("screen.coordinatesdisplay.render", CoordinatesDisplay.getModConstants().getString());
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.setSaveButton(createBackButton(startX, startY, parent));

        this.setWiki(Component.translatable("button.coordinatesdisplay.wiki"), ModUtil.CONFIG_WIKI_RENDER);
    }

    @Override
    protected void initConfigButtons() {

        // background
        this.addConfigOption(new BBooleanButton(
                "button.coordinatesdisplay.background",
                config().renderBackground,
                newVal -> config().renderBackground = newVal
        ));

        // chunk pos
        ((BBooleanButton)this.addConfigOption(new BBooleanButton(
                "button.coordinatesdisplay.chunkpos",
                config().renderChunkData,
                newVal -> config().renderChunkData = newVal
        ))).active = config().renderMode.equals(ModConfig.RenderMode.DEFAULT);

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
        this.addConfigOption(new BWidgetContainer(direction, directionint));

        // biome
        ((BBooleanButton)this.addConfigOption(new BBooleanButton(
                "button.coordinatesdisplay.biome",
                config().renderBiome,
                newVal -> config().renderBiome = newVal
        ))).active = config().renderMode.equals(ModConfig.RenderMode.DEFAULT);

        // mc version
        ((BBooleanButton)this.addConfigOption(new BBooleanButton(
                "button.coordinatesdisplay.mcversion",
                config().renderMCVersion,
                newVal -> config().renderMCVersion = newVal
        ))).active = config().renderMode.equals(ModConfig.RenderMode.DEFAULT);

        this.addConfigOption(new BSpacingEntry());

        // hud rendering
        this.addConfigOption(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.preview")));
        this.addConfigOption(this.createHudRenderEntry(pos));

        // since minecraft's scrolling panels can't handle different entry sizes
        for (int i = 0; i < 3; i++) {
            this.addConfigOption(new BSpacingEntry());
        }

    }
}
