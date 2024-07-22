package dev.boxadactle.coordinatesdisplay.screen.config;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.*;
import dev.boxadactle.boxlib.gui.config.widget.label.*;
import dev.boxadactle.boxlib.gui.config.widget.slider.BIntegerSlider;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.screen.HudHelper;
import dev.boxadactle.coordinatesdisplay.hud.HudDisplayMode;
import dev.boxadactle.coordinatesdisplay.registry.DisplayMode;
import dev.boxadactle.coordinatesdisplay.registry.StartCorner;
import dev.boxadactle.coordinatesdisplay.registry.VisibilityFilter;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class VisualScreen extends BOptionScreen implements HudHelper {

    Position pos;

    TooltipEnumButton<?> startCornerButton;
    TooltipScreenButton changeHudPosButton;

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

        // visibility filters
        Consumer<VisibilityFilter> var2 = newVal -> config().visibilityFilter = newVal;
        try {
            addConfigLine(new VisibilitySelector(config().visibilityFilter, var2));
        } catch (RuntimeException ignored) {
            CoordinatesDisplay.LOGGER.warn("Unknown visibility filter selected in config! Reverting to default.");
            config().visibilityFilter = VisibilityFilter.ALWAYS;

            addConfigLine(new VisibilitySelector(config().visibilityFilter, var2));
        }

        startCornerButton = addConfigLine(new TooltipEnumButton<>(
                "button.coordinatesdisplay.startcorner",
                config().startCorner,
                StartCorner.class,
                newVal -> config().startCorner = newVal,
                GuiUtils.AQUA
        ));

        // display mode
        Consumer<DisplayMode> var4 = newVal -> {
            config().renderMode = newVal;
            verifyButtons();
        };
        try {
            addConfigLine(new DisplayModeSelector(config().renderMode, var4));
        } catch (RuntimeException e) {
            CoordinatesDisplay.LOGGER.warn("Unknown hud renderer selected in config! Reverting to default.");
            config().renderMode = DisplayMode.DEFAULT;

            addConfigLine(new DisplayModeSelector(config().renderMode, var4));
        }

        addConfigLine(new BSpacingEntry());

        // decimal places
        this.addConfigLine(new DecimalPlacesSlider(
                "button.coordinatesdisplay.decimalPlaces",
                0, 5,
                config().decimalPlaces,
                newVal -> config().decimalPlaces = newVal
        ));

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
        changeHudPosButton = addConfigLine(new TooltipScreenButton(
                Component.translatable("button.coordinatesdisplay.editHudPos"),
                this,
                PositionScreen::new
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
        HudDisplayMode metadata = config().renderMode.getMetadata();

        if (!metadata.ignoreTranslations()) {
            startCornerButton.active = true;
            startCornerButton.setTooltip(null);
        } else {
            startCornerButton.active = false;
            startCornerButton.setTooltip(Component.translatable("message.coordintatesdisplay.disabled"));
        }

        if (config().renderMode.getMetadata().allowMove()) {
            changeHudPosButton.active = true;
            changeHudPosButton.setTooltip(null);
        } else {
            changeHudPosButton.active = false;
            changeHudPosButton.setTooltip(Component.translatable("message.coordintatesdisplay.disabled"));
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

    public static class VisibilitySelector extends BEnumButton<VisibilityFilter> {
        public VisibilitySelector(VisibilityFilter value, Consumer<VisibilityFilter> function) {
            super(
                    "button.coordinatesdisplay.visibility",
                    value,
                    VisibilityFilter.class,
                    function,
                    GuiUtils.AQUA
            );
        }

        @Override
        public Component from(VisibilityFilter input) {
            return GuiUtils.colorize(input.getComponent(), valColor);
        }
    }

    public static class DisplayModeSelector extends BEnumButton<DisplayMode> {
        public DisplayModeSelector(DisplayMode value, Consumer<DisplayMode> function) {
            super(
                    "button.coordinatesdisplay.displayMode",
                    value,
                    DisplayMode.class,
                    function,
                    GuiUtils.AQUA
            );
        }

        @Override
        public Component from(DisplayMode input) {
            return GuiUtils.colorize(input.getComponent(), valColor);
        }
    }

    public class TooltipEnumButton<T extends Enum<T>> extends BEnumButton<T> {
        Component tooltip;

        public TooltipEnumButton(String key, T value, Class<T> tEnum, Consumer<T> function, int valColor) {
            super(key, value, tEnum, function, valColor);
        }

        public void setTooltip(Component tooltip) {
            this.tooltip = tooltip;
        }

        @Override
        public void renderToolTip(PoseStack poseStack, int i, int j) {
            if (tooltip != null) {
                VisualScreen.this.renderTooltip(poseStack, tooltip, i, j);
            }
        }
    }

    public class TooltipScreenButton extends BConfigScreenButton {
        Component tooltip;

        public TooltipScreenButton(Component message, Screen parent, Provider<?> function) {
            super(message, parent, function);
        }

        public void setTooltip(Component tooltip) {
            this.tooltip = tooltip;
        }

        @Override
        public void renderToolTip(PoseStack poseStack, int i, int j) {
            if (tooltip != null) {
                VisualScreen.this.renderTooltip(poseStack, tooltip, i, j);
            }
        }
    }

}
