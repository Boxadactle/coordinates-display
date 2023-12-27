package dev.boxadactle.coordinatesdisplay.config.screen;

import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BBooleanButton;
import dev.boxadactle.boxlib.gui.config.widget.label.BCenteredLabel;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.HudHelper;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.CoordinatesHuds;
import dev.boxadactle.coordinatesdisplay.hud.RendererMetadata;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class RenderScreen extends BOptionScreen implements HudHelper {

    Position pos;

    public RenderScreen(Screen parent) {
        super(parent);

        pos = this.generatePositionData();
    }

    @Override
    protected Component getName() {
        return Component.translatable("screen.coordinatesdisplay.render", CoordinatesDisplay.VERSION_STRING);
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.setSaveButton(createBackButton(startX, startY, parent));

        this.setWiki(Component.translatable("button.coordinatesdisplay.wiki"), CoordinatesDisplay.WIKI_RENDER);
    }

    @Override
    protected void initConfigButtons() {
        RendererMetadata metadata = CoordinatesHuds.getRenderer(config().renderMode).getMetadata();

        // background
        this.addConfigLine(new HudOption(
                "button.coordinatesdisplay.background",
                config().renderBackground,
                newVal -> config().renderBackground = newVal,
                metadata.hasBackground()
        ));

        // XYZ
        this.addConfigLine(new HudOption(
                "button.coordinatesdisplay.xyz",
                config().renderXYZ,
                newVal -> config().renderXYZ = newVal,
                metadata.hasXYZ()
        ));

        // chunk pos
        this.addConfigLine(new HudOption(
                "button.coordinatesdisplay.chunkpos",
                config().renderChunkData,
                newVal -> config().renderChunkData = newVal,
                metadata.hasChunkData()
        ));

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

        // biome
        this.addConfigLine(new HudOption(
                "button.coordinatesdisplay.biome",
                config().renderBiome,
                newVal -> config().renderBiome = newVal,
                metadata.hasBiome()
        ));

        this.addConfigLine(
                // mc version
                new HudOption(
                        "button.coordinatesdisplay.mcversion",
                        config().renderMCVersion,
                        newVal -> config().renderMCVersion = newVal,
                        metadata.hasMCVersion()
                ),
                new HudOption(
                        "button.coordinatesdisplay.dimension",
                        config().renderDimension,
                        newVal -> config().renderDimension = newVal,
                        metadata.hasDimension()
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

    }

    public static class HudOption extends BBooleanButton {
        public HudOption(String key, Boolean value, Consumer<Boolean> function, boolean configEnabled) {
            super(key, value, function);

            this.active = configEnabled;

            if (!configEnabled) {
                this.setTooltip(Tooltip.create(Component.translatable("message.coordintatesdisplay.disabled")));
            }
        }
    }
}
