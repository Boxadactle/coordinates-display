package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.math.mathutils.NumberFormatter;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.CoordinatesHuds;
import dev.boxadactle.coordinatesdisplay.hud.RendererMetadata;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import oshi.util.tuples.Triplet;

@RendererMetadata(value = "default")
public class DefaultRenderer implements HudRenderer {

    private int calculateWidth(int p, int tp, Component xtext, Component ytext, Component ztext, Component chunkx, Component chunkz, Component direction, Component biomeDimension, Component version) {
        int a = GuiUtils.getLongestLength(xtext, ytext, ztext);
        int b = GuiUtils.getLongestLength(chunkx, chunkz);
        int c = (config().renderXYZ ? a : 0) +
                (config().renderChunkData ? b : 0) +
                (config().renderXYZ && config().renderChunkData ? tp : 0);

        int d = GuiUtils.getLongestLength(
                (config().renderDirection ? direction : Component.empty()),
                (config().renderBiome || config().renderDimension ? biomeDimension : Component.empty()),
                (config().renderMCVersion ? version : Component.empty())
        );

        return p + Math.max(c, d) + p;
    }

    private int calculateHeight(int th, int p, int tp) {
        int a = config().renderXYZ ? th * 3 : (config().renderChunkData ? th * 2 : 0);

        int b = 0;
        if (config().renderDirection) {
            b += th;
        }
        if (config().renderBiome || config().renderDimension) {
            b += th;
        }
        if (config().renderMCVersion) {
            b += th;
        }

        boolean c = (config().renderXYZ || config().renderChunkData);
        boolean d = (config().renderDirection || config().renderBiome || config().renderMCVersion);

        return p + a + (c || d ? tp : 0) + b + p;
    }

    @Override
    public Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos) {
        NumberFormatter<Double> formatter = genFormatter();
        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);
        Vec2<Integer> chunkPos = pos.position.getChunkPos();

        Component xtext = definition("x", value(player.getA()));
        Component ytext = definition("y", value(player.getB()));
        Component ztext = definition("z", value(player.getC()));

        Component chunkx = definition("chunk.x", value(chunkPos.getX().toString()));
        Component chunkz = definition("chunk.z", value(chunkPos.getY().toString()));

        Component direction = translation(
                "direction",
                definition(resolveDirection(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw()))),
                config().renderDirectionInt ?
                        value(GuiUtils.parentheses(Component.literal(formatter.formatDecimal(pos.headRot.wrapYaw()))))
                        : Component.empty()
        );

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

        Component mcversion = definition("version", value(ClientUtils.getGameVersion()));

        int p = config().padding;
        int tp = config().textPadding;
        int th = GuiUtils.getTextHeight();

        int w = calculateWidth(p, tp, xtext, ytext, ztext, chunkx, chunkz, direction, biomeDimension, mcversion);
        int h = calculateHeight(th, p, tp);

        if (config().renderBackground) {
            RenderUtils.drawSquare(guiGraphics, x, y, w, h, config().backgroundColor);
        }

        if (config().renderXYZ) {
            drawInfo(guiGraphics, xtext, x + p, y + p, config().definitionColor);
            drawInfo(guiGraphics, ytext, x + p, y + p + th, config().definitionColor);
            drawInfo(guiGraphics, ztext, x + p, y + p + (th * 2), config().definitionColor);
        }

        if (config().renderChunkData) {
            drawInfo(guiGraphics, chunkx, x + p + (config().renderXYZ ? GuiUtils.getLongestLength(xtext, ytext, ztext) + tp : 0), y + p, config().definitionColor);
            drawInfo(guiGraphics, chunkz, x + p + (config().renderXYZ ? GuiUtils.getLongestLength(xtext, ytext, ztext) + tp : 0), y + p + th, config().definitionColor);
        }

        int offset = (config().renderXYZ ? th * 3 + tp :
                        (config().renderChunkData ? th * 2 + tp : 0)
                ) + p;

        if (config().renderDirection) {
            drawInfo(guiGraphics, direction, x + p, y + offset, config().definitionColor);
        }

        if (config().renderBiome || config().renderDimension) {
            drawInfo(guiGraphics, biomeDimension, x + p, y + offset + (config().renderDirection ? th : 0), config().definitionColor);
        }

        if (config().renderMCVersion) {
            drawInfo(guiGraphics, mcversion, x + p, y + offset + (config().renderDirection ? th : 0) + (config().renderBiome || config().renderDimension ? th : 0), config().dataColor);
        }


        return new Rect<>(x, y, w, h);
    }
}
