package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.math.mathutils.NumberFormatter;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import oshi.util.tuples.Triplet;

public class DefaultRenderer implements HudRenderer {
    @Override
    public String getTranslationKey() {
        return "hud.coordinatesdisplay.";
    }

    private int calculateWidth(int p, int tp, Component xtext, Component ytext, Component ztext, Component chunkx, Component chunkz, Component direction, Component biome, Component version) {
        int a = GuiUtils.getLongestLength(xtext, ytext, ztext);
        int b = GuiUtils.getLongestLength(chunkx, chunkz);
        int c = a + (CoordinatesDisplay.CONFIG.get().renderChunkData ? b + tp : 0);

        int d = 0;
        if (CoordinatesDisplay.CONFIG.get().renderDirection) {
            if (GuiUtils.getLongestLength(direction) > d) d = GuiUtils.getLongestLength(direction);
        }
        if (CoordinatesDisplay.CONFIG.get().renderBiome) {
            if (GuiUtils.getLongestLength(biome) > d) d = GuiUtils.getLongestLength(biome);
        }
        if (CoordinatesDisplay.CONFIG.get().renderMCVersion) {
            if (GuiUtils.getLongestLength(version) > d) d = GuiUtils.getLongestLength(version);
        }

        return p + Math.max(c, d) + p;
    }

    private int calculateHeight(int th, int p, int tp) {
        int a = th * 3;

        int b = 0;
        if (CoordinatesDisplay.CONFIG.get().renderDirection) {
            b += th;
        }
        if (CoordinatesDisplay.CONFIG.get().renderBiome) {
            b += th;
        }
        if (CoordinatesDisplay.CONFIG.get().renderMCVersion) {
            b += th;
        }

        boolean c = (CoordinatesDisplay.CONFIG.get().renderDirection || CoordinatesDisplay.CONFIG.get().renderBiome || CoordinatesDisplay.CONFIG.get().renderMCVersion);

        return p + a + (c ? tp : 0) + b + p;
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
                definition(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw()), new Object()),
                value(GuiUtils.parentheses(Component.literal(formatter.formatDecimal(pos.headRot.wrapYaw()))))
        );

        String biomestring = pos.world.getBiome(true);
        Component biome = definition(
                "biome",
                GuiUtils.colorize(
                        Component.literal(biomestring),
                        CoordinatesDisplay.CONFIG.get().biomeColors ?
                                CoordinatesDisplay.BiomeColors.getBiomeColor(biomestring, CoordinatesDisplay.CONFIG.get().dataColor) :
                                CoordinatesDisplay.CONFIG.get().dataColor
                )
        );

        Component mcversion = definition("version", value(ClientUtils.getGameVersion()));

        int p = config().padding;
        int tp = config().textPadding;
        int th = GuiUtils.getTextRenderer().lineHeight;

        int w = calculateWidth(p, tp, xtext, ytext, ztext, chunkx, chunkz, direction, biome, mcversion);
        int h = calculateHeight(th, p, tp);

        if (config().renderBackground) {
            RenderUtils.drawSquare(guiGraphics, x, y, w, h, config().backgroundColor);
        }

        // required
        drawInfo(guiGraphics, xtext, x + p, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(guiGraphics, ytext, x + p, y + p + th, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(guiGraphics, ztext, x + p, y + p + (th * 2), CoordinatesDisplay.CONFIG.get().definitionColor);

        if (config().renderChunkData) {
            drawInfo(guiGraphics, chunkx, x + p + GuiUtils.getLongestLength(xtext, ytext, ztext) + tp, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
            drawInfo(guiGraphics, chunkz, x + p + GuiUtils.getLongestLength(xtext, ytext, ztext) + tp, y + p + (th), CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        if (config().renderDirection) {
            drawInfo(guiGraphics, direction, x + p, y + p + (th * 3) + tp, CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        if (config().renderBiome) {
            drawInfo(guiGraphics, biome, x + p, y + p + (th * 3) + tp + (CoordinatesDisplay.CONFIG.get().renderDirection ? th : 0), CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        if (config().renderMCVersion) {
            drawInfo(guiGraphics, mcversion, x + p, y + p + (th * 3) + tp + (CoordinatesDisplay.CONFIG.get().renderDirection ? th : 0) + (CoordinatesDisplay.CONFIG.get().renderBiome ? th : 0), CoordinatesDisplay.CONFIG.get().dataColor);
        }


        return new Rect<>(x, y, w, h);
    }
}
