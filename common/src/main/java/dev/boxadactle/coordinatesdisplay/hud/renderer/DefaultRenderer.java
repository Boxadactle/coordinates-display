package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.layouts.component.LayoutContainerComponent;
import dev.boxadactle.boxlib.layouts.component.ParagraphComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.math.mathutils.NumberFormatter;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.DisplayMode;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import oshi.util.tuples.Triplet;

@DisplayMode(value = "default")
public class DefaultRenderer implements HudRenderer {

    @Override
    public Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos) {
        NumberFormatter<Double> formatter = genFormatter();
        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);
        Vec2<Integer> chunkPos = pos.position.getChunkPos();

        // rendering

        int p = config().padding;
        int tp = config().textPadding;

        ColumnLayout hud = new ColumnLayout(0, 0, tp);
        RowLayout row1 = new RowLayout(0, 0, tp);
        ParagraphComponent row2 = new ParagraphComponent(0);

        if (config().renderXYZ) {
            Component xtext = definition("x", value(player.getA()));
            Component ytext = definition("y", value(player.getB()));
            Component ztext = definition("z", value(player.getC()));

            row1.addComponent(new ParagraphComponent(
                    0,
                    xtext,
                    ytext,
                    ztext
            ));
        }

        if (config().renderChunkData) {
            Component chunkx = definition("chunk.x", value(chunkPos.getX().toString()));
            Component chunkz = definition("chunk.z", value(chunkPos.getY().toString()));

            row1.addComponent(new ParagraphComponent(
                    0,
                    chunkx,
                    chunkz
            ));
        }

        if (config().renderDirection) {
            Component direction = translation(
                    "direction",
                    definition(resolveDirection(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw()))),
                    config().renderDirectionInt ?
                            value(GuiUtils.parentheses(Component.literal(formatter.formatDecimal(pos.headRot.wrapYaw()))))
                            : Component.empty()
            );

            row2.add(direction);
        }

        if (config().renderBiome || config().renderDimension) {
            String biomestring = pos.world.getBiome(true);
            Component coloredBiomestring = GuiUtils.colorize(
                    Component.literal(biomestring),
                    config().biomeColors ?
                            CoordinatesDisplay.BiomeColors.getBiomeColor(biomestring, config().dataColor) :
                            config().dataColor
            );
            Component biome = definition(
                    "biome",
                    coloredBiomestring
            );

            String dimensionstring = pos.world.getDimension(true);
            Component coloredDimensionstring = GuiUtils.colorize(
                    Component.literal(dimensionstring),
                    config().dimensionColors ?
                            CoordinatesDisplay.BiomeColors.getDimensionColor(dimensionstring, config().dataColor) :
                            config().dataColor
            );
            Component dimension = definition(
                    "dimension",
                    coloredDimensionstring
            );

            Component biomeDimension =
                    (config().renderDimension ? (config().renderBiome ? coloredDimensionstring : dimension).copy() : Component.empty())
                            .append(config().renderDimension && config().renderBiome ? definition(": ") : Component.empty())
                            .append(config().renderBiome ? (config().renderDimension ? coloredBiomestring : biome) : Component.empty());

            row2.add(biomeDimension);
        }

        if (config().renderMCVersion) {
            Component mcversion = definition("version", value(ClientUtils.getGameVersion()));

            row2.add(mcversion);
        }

        hud.addComponent(new LayoutContainerComponent(row1));
        hud.addComponent(row2);

        PaddingLayout hudRenderer = new PaddingLayout(x, y, p, hud);

        return renderHud(guiGraphics, hudRenderer);
    }
}
