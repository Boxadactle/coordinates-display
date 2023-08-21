package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.math.mathutils.NumberFormatter;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;
import java.util.Objects;

public class NetherOverworldRenderer extends HudRenderer {

    public NetherOverworldRenderer() {
        super("hud.coordinatesdisplay.netheroverworld.");
    }

    @Override
    protected Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos) {
        try {
            Component overworld = definition(translation("overworld"));
            Component nether = definition(translation("nether"));

            NumberFormatter<Double> formatter = new NumberFormatter<>(config().decimalPlaces);

            Component[][] coords = Objects.requireNonNull(createXYZs(Dimension.toDimension(pos.world.getDimension(false)), formatter, pos.position.getPlayerPos()));

            int w = calculateWidth(coords, overworld, nether);
            int h = calculateHeight();

            if (config().renderBackground) {
                RenderUtils.drawSquare(guiGraphics, x, y, w, h, CoordinatesDisplay.CONFIG.get().backgroundColor);
            }

            // overworld coords
            {
                int a = x + config().padding;
                int b = y + config().padding;

                drawInfo(guiGraphics, overworld, a, b);
                b += 9 + config().padding;

                for (Component coord : coords[0]) {
                    drawInfo(guiGraphics, coord, a, b);
                    b += 9;
                }
            }

            int c;

            // nether coords
            {
                int a = x + config().padding + Math.max(GuiUtils.getLongestLength(coords[0]), GuiUtils.getTextRenderer().width(overworld)) + config().textPadding;
                int b = y + config().padding;

                drawInfo(guiGraphics, nether, a, b);
                b += 9 + config().padding;

                for (Component coord : coords[1]) {
                    drawInfo(guiGraphics, coord, a, b);
                    b += 9;
                }

                c = b + config().textPadding;
            }

            Component dimensionText = value(pos.world.getDimension(true));
            drawInfo(guiGraphics, dimensionText, x + config().padding, c);

            return new Rect<>(x, y, w, h);
        } catch (NullPointerException ignored) {
            Component error = GuiUtils.colorize(translation("error"), GuiUtils.RED);
            Component dimensionText = definition(translation(
                    "dimension",
                    value(pos.world.getDimension(true))
            ));

            int w = config().padding * 2 + GuiUtils.getLongestLength(error, dimensionText);
            int h = config().padding * 3 + GuiUtils.getTextRenderer().lineHeight * 2;

            if (config().renderBackground) {
                RenderUtils.drawSquare(guiGraphics, x, y, w, h, CoordinatesDisplay.CONFIG.get().backgroundColor);
            }

            int a = x + config().padding;
            int b = y + config().padding;

            drawInfo(guiGraphics, error, a, b);
            drawInfo(guiGraphics, dimensionText, a, b + 9 + config().padding);

            return new Rect<>(x, y, w, h);
        }
    }

    private int calculateWidth(Component[][] coords, Component overworld, Component nether) {
        int olength = Math.max(GuiUtils.getLongestLength(coords[0]), GuiUtils.getTextRenderer().width(overworld));
        int nlength = Math.max(GuiUtils.getLongestLength(coords[1]), GuiUtils.getTextRenderer().width(nether));

        return config().padding * 2 + olength + nlength + config().textPadding;
    }

    private int calculateHeight() {
        return config().padding * 2 + GuiUtils.getTextRenderer().lineHeight * 3 + config().textPadding + 9 * 2 + config().padding;
    }

    private Component[] createXYZ(String x, String y, String z) {
        return new Component[] {
                definition(translation("x", value(x))),
                definition(translation("y", value(y))),
                definition(translation("z", value(z)))
        };
    }

    private Component[][] createXYZs(Dimension type, NumberFormatter<Double> d, Vec3<Double> pos) {
        if (Objects.requireNonNull(type) == Dimension.OVERWORLD) {
            return new Component[][] {
                createXYZ(
                        d.formatDecimal(pos.getX()),
                        d.formatDecimal(pos.getY()),
                        d.formatDecimal(pos.getZ())
                ),
                createXYZ(
                        d.formatDecimal(pos.getX() / 8),
                        d.formatDecimal(pos.getY() / 8),
                        d.formatDecimal(pos.getZ() / 8)
                )
            };
        } else if (Objects.requireNonNull(type) == Dimension.NETHER) {
            return new Component[][] {
                    createXYZ(
                            d.formatDecimal(pos.getX() * 8),
                            d.formatDecimal(pos.getY() * 8),
                            d.formatDecimal(pos.getZ() * 8)
                    ),
                    createXYZ(
                            d.formatDecimal(pos.getX()),
                            d.formatDecimal(pos.getY()),
                            d.formatDecimal(pos.getZ())
                    )
            };
        } else {
            return null;
        }
    }

    private enum Dimension {
        OVERWORLD,
        NETHER;

        public static Dimension toDimension(String d) {
            if (d.toLowerCase().contains("overworld")) return OVERWORLD;
            if (d.toLowerCase().contains("nether")) return NETHER;
            else return null;
        }
    }

}
