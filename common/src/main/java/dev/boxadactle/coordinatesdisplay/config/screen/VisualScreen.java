package dev.boxadactle.coordinatesdisplay.config.screen;

import dev.boxadactle.boxlib.gui.BOptionScreen;
import dev.boxadactle.boxlib.gui.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.widget.button.*;
import dev.boxadactle.boxlib.gui.widget.field.*;
import dev.boxadactle.boxlib.gui.widget.label.*;
import dev.boxadactle.boxlib.gui.widget.slider.BIntegerSlider;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.HudHelper;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class VisualScreen extends BOptionScreen implements HudHelper {

    Position pos;

    AbstractWidget startCornerButton;

    public VisualScreen(Screen parent) {
        super(parent);

        pos = this.generatePositionData();
    }

    @Override
    protected Component getName() {
        return Component.translatable("screen.coordinatesdisplay.visual", CoordinatesDisplay.VERSION_STRING);
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.setSaveButton(createBackButton(startX, startY, parent));

        this.setWiki(Component.translatable("button.coordinatesdisplay.wiki"), CoordinatesDisplay.WIKI_VISUAL);
    }

    @Override
    protected void initConfigButtons() {

        // visible
        this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.visible",
                config().visible,
                newVal -> config().visible = newVal
        ));

        // decimal places
        this.addConfigLine(new BIntegerSlider(
                "button.coordinatesdisplay.decimalPlaces",
                0, 5,
                config().decimalPlaces,
                newVal -> config().decimalPlaces = newVal
        ));

        // display mode
        this.addConfigLine(new BEnumButton<>(
                "button.coordinatesdisplay.displayMode",
                config().renderMode,
                ModConfig.RenderMode.class,
                newVal -> {
                    config().renderMode = newVal;
                    startCornerButton.active = ModUtil.not(config().renderMode, ModConfig.RenderMode.HOTBAR);

                    if (config().renderMode == ModConfig.RenderMode.HOTBAR) config().startCorner = ModConfig.StartCorner.TOP_LEFT;
                },
                GuiUtils.AQUA
        ));

        // text shadow
        this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.textshadow",
                config().hudTextShadow,
                newVal -> config().hudTextShadow = newVal
        ));

        startCornerButton = ((AbstractWidget)this.addConfigLine(new BEnumButton<>(
                "button.coordinatesdisplay.startcorner",
                config().startCorner,
                ModConfig.StartCorner.class,
                newVal -> config().startCorner = newVal,
                GuiUtils.AQUA
        )));
        startCornerButton.active = ModUtil.not(config().renderMode, ModConfig.RenderMode.HOTBAR);

        // hud position screen
        this.addConfigLine(new BConfigScreenButton(
                Component.translatable("button.coordinatesdisplay.editHudPos"),
                this,
                HudPositionScreen::new
        ));

        this.addConfigLine(
            // padding
            new BIntegerSlider(
                    "button.coordinatesdisplay.padding",
                    0, 10,
                    config().padding,
                    newVal -> config().padding = newVal
            ),

            // text padding
            new BIntegerSlider(
                    "button.coordinatesdisplay.textpadding",
                    0, 20,
                    config().textPadding,
                    newVal -> config().textPadding = newVal
            )
        );


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
