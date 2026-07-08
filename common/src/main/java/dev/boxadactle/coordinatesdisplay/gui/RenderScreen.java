package dev.boxadactle.coordinatesdisplay.gui;

import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BBooleanButton;
import dev.boxadactle.boxlib.gui.config.widget.button.BColorPickerButton;
import dev.boxadactle.boxlib.gui.config.widget.label.BCenteredLabel;
import dev.boxadactle.boxlib.gui.config.widget.label.BLabel;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.HudDisplayMode;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class RenderScreen extends BOptionScreen implements HudHelper {

    Position pos;

    public RenderScreen(Screen parent) {
        super(parent, Component.translatable("screen.coordinatesdisplay.render", CoordinatesDisplay.VERSION_STRING));

        pos = this.generatePositionData();
    }

    @Override
    protected void initFooter(LinearLayout layout) {
        this.setSaveButton(layout.addChild(createBackButton(lastScreen)));

        this.setWiki(Component.translatable("button.coordinatesdisplay.wiki"), CoordinatesDisplay.WIKI_RENDER);
    }

    @Override
    protected void addOptions() {
        addConfigLine(new BLabel(Component.translatable("label.coordinatesdisplay.components")));
        initComponents();

        addConfigLine(new BLabel(Component.translatable("label.coordinatesdisplay.colors")));
        initColors();

        this.addConfigLine(new BLabel(Component.translatable("label.coordinatesdisplay.preview")));
        this.addConfigLine(this.createHudRenderEntry(pos));

        // since minecraft's scrolling panels can't handle different entry sizes
        for (int i = 0; i < 4; i++) {
            this.addConfigLine(new BSpacingEntry());
        }

    }

    private void initComponents() {
        HudDisplayMode metadata = config().renderMode.getMetadata();

        // background
        this.addConfigLine(new HudOption(
                "button.coordinatesdisplay.background",
                config().renderBackground,
                newVal -> config().renderBackground = newVal,
                metadata.hasBackground()
        ));

        this.addConfigLine(
                // XYZ
                new HudOption(
                        "button.coordinatesdisplay.xyz",
                        config().renderXYZ,
                        newVal -> config().renderXYZ = newVal,
                        metadata.hasXYZ()
                ),

                // chunk pos
                new HudOption(
                        "button.coordinatesdisplay.chunkpos",
                        config().renderChunkData,
                        newVal -> config().renderChunkData = newVal,
                        metadata.hasChunkData()
                )
        );

        this.addConfigLine(
                // direction
                new HudOption(
                        "button.coordinatesdisplay.direction",
                        config().renderDirection,
                        newVal -> config().renderDirection = newVal,
                        metadata.hasDirection()
                ),

                // direction int
                new HudOption(
                        "button.coordinatesdisplay.directionint",
                        config().renderDirectionInt,
                        newVal -> config().renderDirectionInt = newVal,
                        metadata.hasDirectionInt()
                )
        );

        this.addConfigLine(
                // biome
                new HudOption(
                        "button.coordinatesdisplay.biome",
                        config().renderBiome,
                        newVal -> config().renderBiome = newVal,
                        metadata.hasBiome()
                ),
                new HudOption(
                        "button.coordinatesdisplay.dimension",
                        config().renderDimension,
                        newVal -> config().renderDimension = newVal,
                        metadata.hasDimension()
                )
        );

        // mc version
        this.addConfigLine(new HudOption(
                "button.coordinatesdisplay.mcversion",
                config().renderMCVersion,
                newVal -> config().renderMCVersion = newVal,
                metadata.hasMCVersion()
        ));

        // day counter
        addConfigLine(new HudOption(
                "button.coordinatesdisplay.day",
                config().renderDay,
                newVal -> config().renderDay = newVal,
                metadata.hasDay()
        ));

        // time counter
        addConfigLine(
                new HudOption(
                        "button.coordinatesdisplay.time",
                        config().renderTime,
                        newVal -> config().renderTime = newVal,
                        metadata.hasDay()
                ),
                new HudOption(
                        "button.coordinatesdisplay.24hour",
                        config().militaryTime,
                        newVal -> config().militaryTime = newVal,
                        metadata.hasDay()
                )
        );
    }

    private void initColors() {
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

        // background color
        addConfigLine(new BColorPickerButton(
                "button.coordinatesdisplay.backgroundColor",
                this,
                true,
                config().backgroundColor,
                newVal -> config().backgroundColor = newVal
        ));
    }

    public static class HudOption extends BBooleanButton {
        public HudOption(String key, Boolean value, Consumer<Boolean> function, boolean configEnabled) {
            super(key, value, function);

            this.active = configEnabled;

            if (!configEnabled) {
                setTooltip(Tooltip.create(Component.translatable("message.coordintatesdisplay.disabled")));
            }
        }
    }
}
