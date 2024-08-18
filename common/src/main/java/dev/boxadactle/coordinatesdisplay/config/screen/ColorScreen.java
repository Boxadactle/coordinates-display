package dev.boxadactle.coordinatesdisplay.config.screen;

import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BEnumButton;
import dev.boxadactle.boxlib.gui.config.widget.field.*;
import dev.boxadactle.boxlib.gui.config.widget.label.*;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.HudHelper;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.position.Position;
import dev.boxadactle.coordinatesdisplay.registry.HudColor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.function.Consumer;

public class ColorScreen extends BOptionScreen implements HudHelper {

    Position pos;

    public ColorScreen(Screen parent) {
        super(parent);

        pos = this.generatePositionData();
    }

    @Override
    protected String getName() {
        return GuiUtils.getTranslatable("screen.coordinatesdisplay.color", CoordinatesDisplay.VERSION_STRING);
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.setSaveButton(createBackButton(startX, startY, parent));

        this.setWiki(GuiUtils.getTranslatable("button.coordinatesdisplay.wiki"), CoordinatesDisplay.WIKI_COLOR);
    }

    @Override
    protected void initConfigButtons() {

        // definition color
        this.addConfigLine(new ColorSelector(
                "label.coordinatesdisplay.definitionColor",
                config().definitionColor,
                newVal -> config().definitionColor = newVal
        ));

        // data color
        this.addConfigLine(new ColorSelector(
                "label.coordinatesdisplay.dataColor",
                config().dataColor,
                newVal -> config().dataColor = newVal
        ));

        // deathpos color
        this.addConfigLine(new ColorSelector(
                "label.coordinatesdisplay.deathposColor",
                config().deathPosColor,
                newVal -> config().deathPosColor = newVal
        ));

        // background color
        this.addConfigLine(new BCenteredLabel(GuiUtils.getTranslatable("label.coordinatesdisplay.backgroundColor")));

        this.addConfigLine(new BArgbField(
                CoordinatesDisplay.getConfig().backgroundColor,
                newVal -> CoordinatesDisplay.getConfig().backgroundColor = newVal
        ));

        this.addConfigLine(new BSpacingEntry());

        // hud rendering
        this.addConfigLine(new BCenteredLabel(GuiUtils.getTranslatable("label.coordinatesdisplay.preview")));

        addConfigLine(new BCenteredLabel(ModUtil.makeDeathPositionComponent(pos).getColoredString()));

        this.addConfigLine(this.createHudRenderEntry(pos));

        // since minecraft's scrolling panels can't handle different entry sizes
        for (int i = 0; i < 4; i++) {
            this.addConfigLine(new BSpacingEntry());
        }

    }

    public static class ColorSelector extends BEnumButton<HudColor> {

        public ColorSelector(String key, HudColor value, Consumer<HudColor> function) {
            super(
                    key,
                    value,
                    HudColor.class,
                    function,
                    ChatFormatting.WHITE
            );
        }

        @Override
        public Component from(HudColor color) {
            return GuiUtils.colorize(new TextComponent(color.toString().toLowerCase()), color.color());
        }
    }

}
