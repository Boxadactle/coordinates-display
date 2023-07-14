package dev.boxadactle.coordinatesdisplay.gui.config;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.gui.BConfigScreen;
import dev.boxadactle.boxlib.gui.widget.*;
import dev.boxadactle.coordinatesdisplay.gui.HudHelper;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ColorScreen extends BConfigScreen implements HudHelper {

    Position pos;

    public ColorScreen(Screen parent) {
        super(parent);

        pos = this.generatePositionData();
    }

    @Override
    protected Component getName() {
        return Component.translatable("screen.coordinatesdisplay.color", CoordinatesDisplay.getModConstants().getString());
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.setSaveButton(createBackButton(startX, startY, parent));

        this.setWiki(Component.translatable("button.coordinatesdisplay.wiki"), ModUtil.CONFIG_WIKI_COLOR);
    }

    @Override
    protected void initConfigButtons() {

        // definition color
        this.addConfigOption(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.definitionColor")));

        this.addConfigOption(new BHexField(
                CoordinatesDisplay.getConfig().definitionColor,
                newVal -> CoordinatesDisplay.getConfig().definitionColor = newVal
        ));

        // data color
        this.addConfigOption(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.dataColor")));

        this.addConfigOption(new BHexField(
                CoordinatesDisplay.getConfig().dataColor,
                newVal -> CoordinatesDisplay.getConfig().dataColor = newVal
        ));

        // deathpos color
        this.addConfigOption(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.deathposColor")));

        this.addConfigOption(new BHexField(
                CoordinatesDisplay.getConfig().deathPosColor,
                newVal -> CoordinatesDisplay.getConfig().deathPosColor = newVal
        ));

        // background color
        this.addConfigOption(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.backgroundColor")));

        this.addConfigOption(new BArgbField(
                CoordinatesDisplay.getConfig().backgroundColor,
                newVal -> CoordinatesDisplay.getConfig().backgroundColor = newVal
        ));

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
