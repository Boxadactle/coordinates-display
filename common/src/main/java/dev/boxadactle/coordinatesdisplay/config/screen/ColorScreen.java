package dev.boxadactle.coordinatesdisplay.config.screen;

import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.field.*;
import dev.boxadactle.boxlib.gui.config.widget.label.*;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.HudHelper;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ColorScreen extends BOptionScreen implements HudHelper {

    Position pos;

    public ColorScreen(Screen parent) {
        super(parent);

        pos = this.generatePositionData();
    }

    @Override
    protected Component getName() {
        return Component.translatable("screen.coordinatesdisplay.color", CoordinatesDisplay.VERSION_STRING);
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.setSaveButton(createBackButton(startX, startY, parent));

        this.setWiki(Component.translatable("button.coordinatesdisplay.wiki"), CoordinatesDisplay.WIKI_COLOR);
    }

    @Override
    protected void initConfigButtons() {

        // definition color
        this.addConfigLine(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.definitionColor")));

        this.addConfigLine(new BHexField(
                CoordinatesDisplay.getConfig().definitionColor,
                newVal -> CoordinatesDisplay.getConfig().definitionColor = newVal
        ));

        // data color
        this.addConfigLine(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.dataColor")));

        this.addConfigLine(new BHexField(
                CoordinatesDisplay.getConfig().dataColor,
                newVal -> CoordinatesDisplay.getConfig().dataColor = newVal
        ));

        // deathpos color
        this.addConfigLine(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.deathposColor")));

        this.addConfigLine(new BHexField(
                CoordinatesDisplay.getConfig().deathPosColor,
                newVal -> CoordinatesDisplay.getConfig().deathPosColor = newVal
        ));

        // background color
        this.addConfigLine(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.backgroundColor")));

        this.addConfigLine(new BArgbField(
                CoordinatesDisplay.getConfig().backgroundColor,
                newVal -> CoordinatesDisplay.getConfig().backgroundColor = newVal
        ));

        this.addConfigLine(new BSpacingEntry());

        // hud rendering
        this.addConfigLine(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.preview")));

        addConfigLine(new BCenteredLabel(ModUtil.makeDeathPositionComponent(pos)));

        this.addConfigLine(this.createHudRenderEntry(pos));

        // since minecraft's scrolling panels can't handle different entry sizes
        for (int i = 0; i < (ModUtil.not(config().renderMode, ModConfig.RenderMode.MAXIMUM) ? 3 : 4); i++) {
            this.addConfigLine(new BSpacingEntry());
        }

    }

}
