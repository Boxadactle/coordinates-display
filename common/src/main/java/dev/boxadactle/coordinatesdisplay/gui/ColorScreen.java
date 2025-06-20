package dev.boxadactle.coordinatesdisplay.gui;

import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BColorPickerButton;
import dev.boxadactle.boxlib.gui.config.widget.label.*;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ColorScreen extends BOptionScreen implements HudHelper {

    Position pos;

    public ColorScreen(Screen parent) {
        super(parent, Component.translatable("screen.coordinatesdisplay.color", CoordinatesDisplay.VERSION_STRING));

        pos = this.generatePositionData();
    }

    @Override
    protected void initFooter(LinearLayout layout) {
        this.setSaveButton(layout.addChild(createBackButton(lastScreen)));

        this.setWiki(Component.translatable("button.coordinatesdisplay.wiki"), CoordinatesDisplay.WIKI_COLOR);
    }

    @Override
    protected void addOptions() {

        // definition color
        addConfigLine(new BColorPickerButton(
                "button.coordinatesdisplay.definitionColor",
                this,
                false,
                config().definitionColor,
                newVal -> config().definitionColor = newVal
        ));

        // data color
        addConfigLine(new BColorPickerButton(
                "button.coordinatesdisplay.dataColor",
                this,
                false,
                config().dataColor,
                newVal -> config().dataColor = newVal
        ));

        // deathpos color
        addConfigLine(new BColorPickerButton(
                "button.coordinatesdisplay.deathposColor",
                this,
                false,
                config().deathPosColor,
                newVal -> config().deathPosColor = newVal
        ));

        // background color
        addConfigLine(new BColorPickerButton(
                "button.coordinatesdisplay.backgroundColor",
                this,
                true,
                config().backgroundColor,
                newVal -> config().backgroundColor = newVal
        ));

        this.addConfigLine(new BSpacingEntry());

        // hud rendering
        this.addConfigLine(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.preview")));

        addConfigLine(new BCenteredLabel(ModUtil.makeDeathPositionComponent(pos)));

        this.addConfigLine(this.createHudRenderEntry(pos));

        // since minecraft's scrolling panels can't handle different entry sizes
        for (int i = 0; i < 4; i++) {
            this.addConfigLine(new BSpacingEntry());
        }

    }

}
