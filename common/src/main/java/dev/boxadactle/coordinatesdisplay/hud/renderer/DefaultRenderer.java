package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.layouts.component.LayoutContainerComponent;
import dev.boxadactle.boxlib.layouts.component.ParagraphComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.math.mathutils.NumberFormatter;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.HudDisplayMode;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.hud.Triplet;
import dev.boxadactle.coordinatesdisplay.position.Position;
import dev.boxadactle.coordinatesdisplay.WorldColors;
import net.minecraft.network.chat.Component;

@HudDisplayMode("default")
public class DefaultRenderer implements HudRenderer {

    @Override
    public RenderingLayout renderOverlay(int x, int y, Position pos) {
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
            Component xtext = definition(GlobalTexts.X, value(player.getA()));
            Component ytext = definition(GlobalTexts.Y, value(player.getB()));
            Component ztext = definition(GlobalTexts.Z, value(player.getC()));

            row1.addComponent(new ParagraphComponent(
                    0,
                    xtext,
                    ytext,
                    ztext
            ));
        }

        if (config().renderChunkData) {
            Component chunkx = definition(GlobalTexts.CHUNK_X, value(chunkPos.getX().toString()));
            Component chunkz = definition(GlobalTexts.CHUNK_Z, value(chunkPos.getY().toString()));

            row1.addComponent(new ParagraphComponent(
                    0,
                    chunkx,
                    chunkz
            ));
        }

        if (config().renderDirection) {
            Component direction = Component.empty()
                    .append(definition(resolveDirection(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw()))))
                    .append(" ")
                    .append(config().renderDirectionInt ?
                            value("(" + formatter.formatDecimal(pos.headRot.wrapYaw()) + ")")
                            : Component.empty()
                    );

            row2.add(direction);
        }

        if (config().renderBiome || config().renderDimension) {
            Component biomeString = ModUtil.getBiomeComponent(pos.world.getBiomeKey(), pos.world.getBiome(), config().biomeColors, config().dataColor);
            Component biome = definition(GlobalTexts.BIOME, biomeString);

            String dimensionstring = pos.world.getDimension(true);
            Component coloredDimensionstring = GuiUtils.colorize(
                    Component.literal(dimensionstring),
                    config().biomeColors ?
                            WorldColors.getDimensionColor(dimensionstring, config().definitionColor) :
                            config().definitionColor
            );
            Component dimension = definition(
                    GlobalTexts.DIMENSION,
                    coloredDimensionstring
            );

            Component biomeDimension =
                    (config().renderDimension ? (config().renderBiome ? coloredDimensionstring : dimension).copy() : Component.empty())
                            .append(config().renderDimension && config().renderBiome ? definition(": ") : Component.empty())
                            .append(config().renderBiome ? (config().renderDimension ? biomeString : biome) : Component.empty());

            row2.add(biomeDimension);
        }

        if (config().renderMCVersion) {
            Component mcversion = definition("version", value(ClientUtils.getGameVersion()));

            row2.add(mcversion);
        }

        hud.addComponent(new LayoutContainerComponent(row1));
        hud.addComponent(row2);

        return new PaddingLayout(x, y, p, hud);
    }
}
