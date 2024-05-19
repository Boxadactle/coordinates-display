package dev.boxadactle.coordinatesdisplay.config.screen;

import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.*;
import dev.boxadactle.boxlib.gui.config.widget.label.*;
import dev.boxadactle.boxlib.gui.config.widget.slider.BIntegerSlider;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.HudHelper;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.hud.CoordinatesHuds;
import dev.boxadactle.coordinatesdisplay.hud.DisplayMode;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class VisualScreen extends BOptionScreen implements HudHelper {

    Position pos;

    BEnumButton<?> startCornerButton;
    AbstractWidget changeHudPosButton;

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
        addConfigLine(
                new VisibilityFilterSlector(
                        newVal -> config().visibilityFilter = newVal
                )
        );

        // decimal places
        this.addConfigLine(new DecimalPlacesSlider(
                "button.coordinatesdisplay.decimalPlaces",
                0, 5,
                config().decimalPlaces,
                newVal -> config().decimalPlaces = newVal
        ));

        startCornerButton = new BEnumButton<>(
                "button.coordinatesdisplay.startcorner",
                config().startCorner,
                ModConfig.StartCorner.class,
                newVal -> config().startCorner = newVal,
                GuiUtils.AQUA
        );

        // display mode
        this.addConfigLine(
                new DisplayModeSelector(
                    newVal -> {
                        config().renderMode = newVal;
                        verifyButtons();
                    }
                ),
                startCornerButton
        );

        // text shadow
        this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.textshadow",
                config().hudTextShadow,
                newVal -> config().hudTextShadow = newVal
        ));

        this.addConfigLine(
                // biome colors
                new BBooleanButton(
                        "button.coordinatesdisplay.biomecolors",
                        config().biomeColors,
                        newVal -> config().biomeColors = newVal
                ),

                // dimension colors
                new BBooleanButton(
                        "button.coordinatesdisplay.dimensioncolors",
                        config().dimensionColors,
                        newVal -> config().dimensionColors = newVal
                )
        );

        // hud position screen
        changeHudPosButton = (AbstractWidget) addConfigLine(new BConfigScreenButton(
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
        for (int i = 0; i < 4; i++) {
            this.addConfigLine(new BSpacingEntry());
        }

        verifyButtons();

    }

    private void verifyButtons() {
        DisplayMode metadata = CoordinatesHuds.getRenderer(config().renderMode).getMetadata();

        if (!metadata.ignoreTranslations()) {
            startCornerButton.active = true;
            startCornerButton.setTooltip(null);
        } else {
            startCornerButton.active = false;
            startCornerButton.setTooltip(Tooltip.create(Component.translatable("message.coordintatesdisplay.disabled")));
        }

        if (CoordinatesHuds.getRenderer(config().renderMode).getMetadata().allowMove()) {
            changeHudPosButton.active = true;
            changeHudPosButton.setTooltip(null);
        } else {
            changeHudPosButton.active = false;
            changeHudPosButton.setTooltip(Tooltip.create(Component.translatable("message.coordintatesdisplay.disabled")));
        }
    }

    public static class DecimalPlacesSlider extends BIntegerSlider {

        public DecimalPlacesSlider(String key, int min, int max, int value, Consumer<Integer> function) {
            super(key, min, max, value, function);

            updateMessage();
        }

        @Override
        protected String roundNumber(Integer input) {
            return input == 0 ? "0 (" + GuiUtils.getTranslatable("button.coordinatesdisplay.decimalPlaces.block_pos") + ")" : super.roundNumber(input);
        }
    }

    public static class DisplayModeSelector extends BToggleButton<String> {
        public DisplayModeSelector(Consumer<String> function) {
            super(
                    "button.coordinatesdisplay.displayMode",
                    CoordinatesDisplay.getConfig().renderMode.toLowerCase(),
                    CoordinatesHuds.registeredOverlays.keySet().stream().toList(),
                    function
            );
        }

        @Override
        public String to(Component input) {
            return list.get(index);
        }

        @Override
        public Component from(String  input) {
            return GuiUtils.colorize(CoordinatesHuds.getRenderer(list.get(index)).getComponent(), GuiUtils.AQUA);
        }
    }

    public static class VisibilityFilterSlector extends BToggleButton<String> {
        public VisibilityFilterSlector(Consumer<String> function) {
            super(
                    "button.coordinatesdisplay.visibility",
                    CoordinatesDisplay.getConfig().visibilityFilter.toLowerCase(),
                    CoordinatesHuds.registeredVisibilityFilters.keySet().stream().toList(),
                    function
            );
        }

        @Override
        public String to(Component input) {
            return list.get(index);
        }

        @Override
        public Component from(String  input) {
            return GuiUtils.colorize(CoordinatesHuds.getVisibilityFilter(list.get(index)).getComponent(), GuiUtils.AQUA);
        }
    }

}
