package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.layouts.component.LayoutContainerComponent;
import dev.boxadactle.boxlib.layouts.component.ParagraphComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.math.mathutils.NumberFormatter;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.DisplayMode;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.hud.Triplet;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

@DisplayMode(
        value = "direction",
        hasChunkData = false,
        hasBiome = false,
        hasMCVersion = false,
        hasDimension = false
)
public class DirectionRenderer implements HudRenderer {

    private enum Direction {
        POSITIVE_Z(new TranslatableComponent("hud.coordinatesdisplay.direction.positive", "Z")), // south
        NEGATIVE_X(new TranslatableComponent("hud.coordinatesdisplay.direction.negative", "X")), // east
        NEGATIVE_Z(new TranslatableComponent("hud.coordinatesdisplay.direction.negative", "Z")), // north
        POSITIVE_X(new TranslatableComponent("hud.coordinatesdisplay.direction.positive", "X")), // west

        POSITIVE_Y(new TranslatableComponent("hud.coordinatesdisplay.direction.positive", "Y")), // up
        NEGATIVE_Y(new TranslatableComponent("hud.coordinatesdisplay.direction.negative", "Y")); // down

        public final Component component;

        Direction(Component component) {
            this.component = component;
        }

        public static Direction fromYaw(double yaw) {
            return Direction.values()[(int) Math.round(yaw / 90.0F) & 3];
        }

        public static Direction fromPitch(double pitch) {
            return pitch > 0 ? POSITIVE_Y : NEGATIVE_Y;
        }
    }

    // reused from MinRenderer
    private String[] createYawComponents(double yaw) {
        // compiled using the debug screen
        String[][] directions = {
                // X   Z
                { "_", "+" },
                { "-", "+" },
                { "-", "_" },
                { "-", "-" },
                { "_", "-" },
                { "+", "-" },
                { "+", "_" },
                { "+", "+" }
        };

        return directions[(int) Math.round(yaw / 45.0F) & 7];
    }

    @Override
    public Rect<Integer> renderOverlay(int x, int y, Position pos) {
        NumberFormatter<Double> formatter = genFormatter();
        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);

        ColumnLayout hud = new ColumnLayout(0, 0, config().textPadding);
        RowLayout row = new RowLayout(0, 0, config().textPadding * 2);

        if (config().renderXYZ) {
            Component xtext = definition("x", value(player.getA()));
            Component ytext = definition("y", value(player.getB()));
            Component ztext = definition("z", value(player.getC()));

            row.addComponent(new ParagraphComponent(
                    0,
                    xtext,
                    ytext,
                    ztext
            ));
        }

        // we just reuse the compass renderer from the spawnpoint renderer
        SpawnpointRenderer.CompassRenderer compassRenderer = new SpawnpointRenderer.CompassRenderer(pos, new BlockPos(0, 0, 0));
        compassRenderer.size = 28;

        RowLayout r = new RowLayout(0, 0, 0);
        r.addComponent(compassRenderer);
        row.addComponent(new LayoutContainerComponent(new PaddingLayout(0, 0, 4, r)));

        hud.addComponent(new LayoutContainerComponent(row));

        // direction
        ParagraphComponent direction = new ParagraphComponent(0);

        double yaw = pos.headRot.wrapYaw();

        if (config().renderDirection) {
            String[] components = createYawComponents(yaw);
            Component intText = definition(
                    new TextComponent(
                            components[0] + " (" +
                                    formatter.formatDecimal(compassRenderer.calculateRelativeDirection(pos.position.getBlockPos(), new Vec3<>(0, 0, 0), yaw)) + "°) " +
                                    components[1]
                    )
            );

            String dir = ModUtil.getDirectionFromYaw(yaw);

            Component directionText = definition(
                    "direction",
                    value(resolveDirection(dir)),
                    value(resolveDirection(dir, true))
            );

            direction.add(intText);
            direction.add(directionText);
        }

        if (config().renderDirectionInt) {
            Direction directionFromYaw = Direction.fromYaw(yaw);
            Component yawText = definition(
                    "yaw",
                    value(formatter.formatDecimal(yaw)),
                    value(directionFromYaw.component)
            );

            Direction pitchFromYaw = Direction.fromPitch(pos.headRot.wrapPitch());
            Component pitchText = definition(
                    "pitch",
                    value(formatter.formatDecimal(pos.headRot.wrapPitch())),
                    value(pitchFromYaw.component)
            );

            direction.add(yawText);
            direction.add(pitchText);
        }

        hud.addComponent(direction);

        return renderHud(new PaddingLayout(x, y, config().padding, hud));
    }
}
