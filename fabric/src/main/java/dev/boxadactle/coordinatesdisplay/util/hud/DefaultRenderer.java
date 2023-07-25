package dev.boxadactle.coordinatesdisplay.util.hud;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.HudRenderer;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import dev.boxadactle.boxlib.math.Rect;
import dev.boxadactle.boxlib.math.Vec2;
import dev.boxadactle.boxlib.math.Vec3;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.text.DecimalFormat;

public class DefaultRenderer extends HudRenderer.Renderer {

    public DefaultRenderer() {
        super("hud.coordinatesdisplay.");
    }

    private int calculateWidth(int p, int tp, Text xtext, Text ytext, Text ztext, Text chunkx, Text chunkz, Text direction, Text biome, Text version) {
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
    protected Rect<Integer> renderOverlay(DrawContext drawContext, int x, int y, Position pos) {
        DecimalFormat decimalFormat = new DecimalFormat(CoordinatesDisplay.CONFIG.get().decimalRounding ? "0.00" : "0");
        Vec3<Double> player = pos.position.getPlayerPos();
        Vec2<Integer> chunkPos = pos.position.getChunkPos();

        Text xtext = GuiUtils.colorize(translation(
                "x",
                GuiUtils.colorize(
                        Text.literal(decimalFormat.format(player.getX())),
                        config().dataColor
                )
        ), config().definitionColor);

        Text ytext = GuiUtils.colorize(translation(
                "y",
                GuiUtils.colorize(
                        Text.literal(decimalFormat.format(player.getY())),
                        config().dataColor
                )
        ), config().definitionColor);

        Text ztext = GuiUtils.colorize(translation(
                "z",
                GuiUtils.colorize(
                        Text.literal(decimalFormat.format(player.getZ())),
                        config().dataColor
                )
        ), config().definitionColor);



        Text chunkx = GuiUtils.colorize(translation(
                "chunk.x",
                GuiUtils.colorize(
                        Text.literal(Integer.toString(chunkPos.getX())),
                        config().dataColor
                )
        ), config().definitionColor);

        Text chunkz = GuiUtils.colorize(translation(
                "chunk.z",
                GuiUtils.colorize(
                        Text.literal(Integer.toString(chunkPos.getY())),
                        config().dataColor
                )
        ), config().definitionColor);



        Text direction = translation(
                "direction",
                GuiUtils.colorize(
                        translation(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw())),
                        config().definitionColor
                ),
                config().renderDirectionInt ? GuiUtils.colorize(
                        GuiUtils.parentheses(Text.literal(decimalFormat.format(pos.headRot.wrapYaw()))),
                        config().dataColor
                ) : Text.literal("")
        );

        String biomestring = pos.world.getBiome(true);
        Text biome = GuiUtils.colorize(translation(
                "biome",
                GuiUtils.colorize(
                        Text.literal(biomestring),
                        CoordinatesDisplay.CONFIG.get().biomeColors ?
                                CoordinatesDisplay.BiomeColors.getBiomeColor(biomestring, CoordinatesDisplay.CONFIG.get().dataColor) :
                                CoordinatesDisplay.CONFIG.get().dataColor
                )
        ), config().definitionColor);

        Text mcversion = GuiUtils.colorize(translation(
                "version",
                GuiUtils.colorize(
                        Text.literal(ClientUtils.getGameVersion()),
                        config().dataColor
                )
        ), config().definitionColor);

        int p = config().padding;
        int tp = config().textPadding;
        int th = GuiUtils.getTextRenderer().fontHeight;

        int w = calculateWidth(p, tp, xtext, ytext, ztext, chunkx, chunkz, direction, biome, mcversion);
        int h = calculateHeight(th, p, tp);

        if (config().renderBackground) {
            RenderUtils.drawSquare(drawContext, x, y, w, h, config().backgroundColor);
        }

        // required
        drawInfo(drawContext, xtext, x + p, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(drawContext, ytext, x + p, y + p + th, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(drawContext, ztext, x + p, y + p + (th * 2), CoordinatesDisplay.CONFIG.get().definitionColor);

        if (config().renderChunkData) {
            drawInfo(drawContext, chunkx, x + p + GuiUtils.getLongestLength(xtext, ytext, ztext) + tp, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
            drawInfo(drawContext, chunkz, x + p + GuiUtils.getLongestLength(xtext, ytext, ztext) + tp, y + p + (th), CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        if (config().renderDirection) {
            drawInfo(drawContext, direction, x + p, y + p + (th * 3) + tp, CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        if (config().renderBiome) {
            drawInfo(drawContext, biome, x + p, y + p + (th * 3) + tp + (CoordinatesDisplay.CONFIG.get().renderDirection ? th : 0), CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        if (config().renderMCVersion) {
            drawInfo(drawContext, mcversion, x + p, y + p + (th * 3) + tp + (CoordinatesDisplay.CONFIG.get().renderDirection ? th : 0) + (CoordinatesDisplay.CONFIG.get().renderBiome ? th : 0), CoordinatesDisplay.CONFIG.get().dataColor);
        }


        return new Rect<>(x, y, w, h);
    }
}
