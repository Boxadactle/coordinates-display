package dev.boxadactle.coordinatesdisplay.hud.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.layouts.component.LayoutContainerComponent;
import dev.boxadactle.boxlib.layouts.component.ParagraphComponent;
import dev.boxadactle.boxlib.layouts.component.TextComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.math.mathutils.NumberFormatter;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.hud.HudDisplayMode;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.hud.Triplet;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Objects;

@HudDisplayMode(
        value = "nether_overworld",
        hasXYZ = false,
        hasChunkData = false,
        hasDirection = false,
        hasDirectionInt = false,
        hasBiome = false,
        hasMCVersion = false,
        hasDimension = false
)
public class NetherOverworldRenderer implements HudRenderer {

    @Override
    public RenderingLayout renderOverlay(int x, int y, Position pos) {
        try {
            ColumnLayout hud = new ColumnLayout(0, 0, config().textPadding);

            Component overworld = definition(translation("overworld"));
            Component nether = definition(translation("nether"));

            NumberFormatter<Double> formatter = genFormatter();

            Component[][] coords = Objects.requireNonNull(createXYZs(Dimension.toDimension(pos.world.getDimension(false)), formatter, pos));

            RowLayout coordsLayout = new RowLayout(0, 0, config().textPadding);

            // overworld
            ParagraphComponent overworldCoords = new ParagraphComponent(1);
            overworldCoords.add(overworld);
            overworldCoords.getComponent().addAll(List.of(coords[0]));

            // nether
            ParagraphComponent netherCoords = new ParagraphComponent(1);
            netherCoords.add(nether);
            netherCoords.getComponent().addAll(List.of(coords[1]));

            // add to layout
            coordsLayout.addComponent(overworldCoords);
            coordsLayout.addComponent(netherCoords);

            // dimension
            Component dimensionText = value(pos.world.getDimension(true));
            TextComponent dimensionComponent = new TextComponent(dimensionText);

            // add both to layout
            hud.addComponent(new LayoutContainerComponent(coordsLayout));
            hud.addComponent(dimensionComponent);

            return new PaddingLayout(x, y, config().padding, hud);
        } catch (NullPointerException ignored) {
            Component error = GuiUtils.colorize(translation("error"), GuiUtils.RED);
            Component dimensionText = definition(
                    GlobalTexts.DIMENSION,
                    value(pos.world.getDimension(true))
            );

            ColumnLayout hud = new ColumnLayout(0, 0, config().textPadding);

            hud.addComponent(new TextComponent(error));
            hud.addComponent(new TextComponent(dimensionText));

            return new PaddingLayout(x, y, config().padding, hud);
        }
    }

    private Component[] createXYZComponents(String x, String y, String z) {
        return new Component[] {
                definition(GlobalTexts.X, value(x)),
                definition(GlobalTexts.Y, value(y)),
                definition(GlobalTexts.Z, value(z))
        };
    }

    private Component[][] createXYZs(Dimension type, NumberFormatter<Double> d, Position pos) {
        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);

        if (Objects.requireNonNull(type) == Dimension.OVERWORLD) {
            return new Component[][] {
                createXYZComponents(
                        player.getA(),
                        player.getB(),
                        player.getC()
                ),
                createXYZComponents(
                        d.formatDecimal(pos.position.getPlayerPos().getX() / 8),
                        "-",
                        d.formatDecimal(pos.position.getPlayerPos().getZ() / 8)
                )
            };
        } else if (Objects.requireNonNull(type) == Dimension.NETHER) {
            return new Component[][] {
                    createXYZComponents(
                            d.formatDecimal(pos.position.getPlayerPos().getX() * 8),
                            "-",
                            d.formatDecimal(pos.position.getPlayerPos().getZ() * 8)
                    ),
                    createXYZComponents(
                            player.getA(),
                            player.getB(),
                            player.getC()
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
