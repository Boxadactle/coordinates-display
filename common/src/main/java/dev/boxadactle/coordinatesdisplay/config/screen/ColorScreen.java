package dev.boxadactle.coordinatesdisplay.config.screen;

import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.field.*;
import dev.boxadactle.boxlib.gui.config.widget.label.*;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.HudHelper;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class ColorScreen extends BOptionScreen implements HudHelper {

    Position pos;

    public ColorScreen(Screen parent) {
        super(parent);

        pos = this.generatePositionData();
    }

    @Override
    protected Component getName() {
        return new TranslatableComponent("screen.coordinatesdisplay.color", CoordinatesDisplay.VERSION_STRING);
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.setSaveButton(createBackButton(startX, startY, parent));

        this.setWiki(new TranslatableComponent("button.coordinatesdisplay.wiki"), CoordinatesDisplay.WIKI_COLOR);
    }

    @Override
    protected void initConfigButtons() {

        // definition color
        addConfigLine(new BCenteredLabel(new TranslatableComponent("label.coordinatesdisplay.definitionColor")));
        addConfigLine(new BHexField(
                config().definitionColor,
                newVal -> config().definitionColor = newVal
        ));

        // data color
        addConfigLine(new BCenteredLabel(new TranslatableComponent("label.coordinatesdisplay.dataColor")));
        addConfigLine(new BHexField(
                config().dataColor,
                newVal -> config().dataColor = newVal
        ));

        // deathpos color
        addConfigLine(new BCenteredLabel(new TranslatableComponent("label.coordinatesdisplay.deathposColor")));
        addConfigLine(new BHexField(
                config().deathPosColor,
                newVal -> config().deathPosColor = newVal
        ));

        // background color
        this.addConfigLine(new BCenteredLabel(new TranslatableComponent("label.coordinatesdisplay.backgroundColor")));

        this.addConfigLine(new BArgbField(
                CoordinatesDisplay.getConfig().backgroundColor,
                newVal -> CoordinatesDisplay.getConfig().backgroundColor = newVal
        ));

        this.addConfigLine(new BSpacingEntry());

        // hud rendering
        this.addConfigLine(new BCenteredLabel(new TranslatableComponent("label.coordinatesdisplay.preview")));

        addConfigLine(new BCenteredLabel(ModUtil.makeDeathPositionComponent(pos)));

        this.addConfigLine(this.createHudRenderEntry(pos));

        // since minecraft's scrolling panels can't handle different entry sizes
        for (int i = 0; i < 4; i++) {
            this.addConfigLine(new BSpacingEntry());
        }

    }

}
