package dev.boxadactle.coordinatesdisplay.util.hud;

import dev.boxadactle.coordinatesdisplay.util.HudRenderer;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import dev.boxadactle.boxlib.math.Rect;
import dev.boxadactle.boxlib.math.Vec3;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.text.DecimalFormat;

public class MinRenderer extends HudRenderer.Renderer {

    public MinRenderer() {
        super("hud.coordinatesdisplay.min.");
    }

    private int calculateWidth(int p, int th, int dpadding, Text xtext, Text ytext, Text ztext, Text biome) {
        int a = GuiUtils.getLongestLength(xtext, ytext, ztext, biome);
        int b = GuiUtils.getTextRenderer().getWidth("NW");

        return p + a + dpadding + b + p;
    }

    private int calculateHeight(int p, int th) {
        // this might become a real method later
        return p + (th * 4) + p;
    }

    @Override
    protected Rect<Integer> renderOverlay(DrawContext drawContext, int x, int y, Position pos) {
        DecimalFormat decimalFormat = new DecimalFormat(CoordinatesDisplay.CONFIG.get().decimalRounding ? "0.00" : "0");
        Vec3<Double> player = pos.position.getPlayerPos();

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

        int p = CoordinatesDisplay.CONFIG.get().padding;
        int th = GuiUtils.getTextRenderer().fontHeight;
        int tp = CoordinatesDisplay.CONFIG.get().textPadding;

        double yaw = pos.headRot.wrapYaw();
        double pitch = pos.headRot.wrapPitch();
        Text direction = translation(ModUtil.getDirectionFromYaw(yaw));
        Text pitchText = Text.literal(pitch > 0 ? "+" : "-");
        Text directionText = Text.translatable("hud.coordinatesdisplay.min." + ModUtil.getDirectionFromYaw(yaw), direction);
        Text yawText = Text.literal(yaw > 0 ? "+" : "-");



        int w = calculateWidth(p, th, tp, xtext, ytext, ztext, biome);
        int h = calculateHeight(p, th);

        // rendering
        if (config().renderBackground) {
            RenderUtils.drawSquare(drawContext, x, y, w, h, CoordinatesDisplay.CONFIG.get().backgroundColor);
        }

        drawInfo(drawContext, xtext, x + p, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(drawContext, ytext, x + p, y + p + th, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(drawContext, ztext, x + p, y + p + (th * 2), CoordinatesDisplay.CONFIG.get().definitionColor);

        drawInfo(drawContext, biome, x + p, y + p + (th * 3), CoordinatesDisplay.CONFIG.get().definitionColor);
        {
            int dstart = (x + w) - p - GuiUtils.getTextRenderer().getWidth(directionText);
            int ypstart = (x + w) - p - GuiUtils.getTextRenderer().getWidth(yawText);

            drawInfo(drawContext, pitchText, ypstart, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
            drawInfo(drawContext, directionText, dstart, y + p + th, CoordinatesDisplay.CONFIG.get().dataColor);
            drawInfo(drawContext, yawText, ypstart, y + p + (th * 2), CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        return new Rect<>(x, y, w, h);
    }
}
