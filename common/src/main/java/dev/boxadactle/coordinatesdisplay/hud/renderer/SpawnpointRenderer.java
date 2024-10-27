package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.layouts.LayoutComponent;
import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.layouts.component.LayoutContainerComponent;
import dev.boxadactle.boxlib.layouts.component.ParagraphComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.hud.HudDisplayMode;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import oshi.util.tuples.Triplet;

// this is a bit of a mess, but it still works
@HudDisplayMode(
        value = "spawnpoint",
        hasChunkData = false,
        hasDirection = false,
        hasDirectionInt = false,
        hasBiome = false,
        hasMCVersion = false,
        hasDimension = false
)
public class SpawnpointRenderer implements HudRenderer {
    
    // unfortunately, I don't think you can access the player's
    // spawnpoint unless your mod is server-side
    public BlockPos resolveWorldSpawn() {
        try {
            return WorldUtils.getWorld().getSharedSpawnPos();
        } catch (Exception e) {
            return new BlockPos(0, 0, 0);
        }
    }

    private Tuple<Vec3<Double>, Vec3<Integer>> createRelativePosition(Vec3<Double> playerVec, Vec3<Integer> playerBlock, BlockPos spawn) {
        int spawnX = spawn.getX();
        int spawnY = spawn.getY();
        int spawnZ = spawn.getZ();

        Vec3<Double> relativeVec = new Vec3<>(
                playerVec.getX() - spawnX,
                playerVec.getY() - spawnY,
                playerVec.getZ() - spawnZ
        );

        Vec3<Integer> relativeBlock = new Vec3<>(
                playerBlock.getX() - spawnX,
                playerBlock.getY() - spawnY,
                playerBlock.getZ() - spawnZ
        );

        return new Tuple<>(relativeVec, relativeBlock);
    }

    @Override
    public RenderingLayout renderOverlay(int x, int y, Position pos) {
        BlockPos spawnpoint = resolveWorldSpawn();

        ColumnLayout hud = new ColumnLayout(0, 0, config().textPadding);

        RowLayout row1 = new RowLayout(0, 0, config().textPadding);
        RowLayout row2 = new RowLayout(0, 0, config().textPadding);

        // player
        if (config().renderXYZ) {
            Component playerLabel = definition("player", "");
            Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);
            Triplet<Component, Component, Component> xyz = createXYZ(player.getA(), player.getB(), player.getC());

            ParagraphComponent component = new ParagraphComponent(0,
                    playerLabel,
                    xyz.getA(),
                    xyz.getB(),
                    xyz.getC()
            );

            row1.addComponent(component);
        }

        { // spawnpoint
            Component spawnpointLabel = definition("worldSpawn", "");
            Triplet<Component, Component, Component> spawnpointXYZ = createXYZ(spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ());

            ParagraphComponent component = new ParagraphComponent(0,
                    spawnpointLabel,
                    spawnpointXYZ.getA(),
                    spawnpointXYZ.getB(),
                    spawnpointXYZ.getC()
            );

            row1.addComponent(component);
        }


        { // relative position
            Component relativeLabel = definition("relative", "");
            Tuple<Vec3<Double>, Vec3<Integer>> relativePos = createRelativePosition(
                    pos.position.getPlayerPos(),
                    pos.position.getBlockPos(),
                    new BlockPos(spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ())
            );
            Triplet<String, String, String> relative = this.roundPosition(relativePos.getA(), relativePos.getB(), CoordinatesDisplay.getConfig().decimalPlaces);
            Triplet<Component, Component, Component> relativeXYZ = createXYZ(relative.getA(), relative.getB(), relative.getC());

            ParagraphComponent component = new ParagraphComponent(0,
                    relativeLabel,
                    relativeXYZ.getA(),
                    relativeXYZ.getB(),
                    relativeXYZ.getC()
            );

            row2.addComponent(component);
        }

        { // compass
            CompassRenderer compass = new CompassRenderer(pos, spawnpoint);

            row2.addComponent(compass);
        }

        hud.addComponent(new LayoutContainerComponent(row1));
        hud.addComponent(new LayoutContainerComponent(row2));

        return new PaddingLayout(x, y, config().padding, hud);
    }

    public static class CompassRenderer extends LayoutComponent<Position> {
        BlockPos spawnpoint;

        public int size = 32;

        public CompassRenderer(Position component, BlockPos spawnpoint) {
            super(component);

            this.spawnpoint = spawnpoint;
        }

        @Override
        public int getWidth() {
            return size;
        }

        @Override
        public int getHeight() {
            return size;
        }

        public double calculateRelativeDirection(Vec3<Integer> pos1, Vec3<Integer> pos2, double yaw) {
            int x = pos2.getX() - pos1.getX();
            int z = pos2.getZ() - pos1.getZ();

            double theta = Math.atan2(z, x);
            double startDirection = Math.toDegrees(theta) + 180;
            double relativeDirection = startDirection - yaw;

            if (relativeDirection < 0) {
                relativeDirection += 360;
            }

            relativeDirection -= 180;

            // shift it back 90 degrees as 0 degrees is south
            return Mth.wrapDegrees(relativeDirection - 90);
        }

        private ResourceLocation resolveCompassTexture(double d) {
            // we need to shift the degrees by 180
            // to set 0 degrees to north rather than south
            double degrees = Mth.wrapDegrees(d + 180);

            double range360 = degrees + 180;
            double range1 = range360 / 360;

            // this copied from the minecraft compass json model
            String[] textures = {
                    "compass_16", "compass_17", "compass_18", "compass_19", "compass_20", "compass_21", "compass_22", "compass_23",
                    "compass_24", "compass_25", "compass_26", "compass_27", "compass_28", "compass_29", "compass_30", "compass_31",
                    "compass_00", "compass_01", "compass_02", "compass_03", "compass_04", "compass_05", "compass_06", "compass_07",
                    "compass_08", "compass_09", "compass_10", "compass_11", "compass_12", "compass_13", "compass_14", "compass_15",
                    "compass_16"
            };

            String texture = "textures/item/" + textures[(int) (range1 * textures.length)] + ".png";
            return ResourceLocation.withDefaultNamespace(texture);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int x, int y) {
            double degrees = calculateRelativeDirection(component.position.getBlockPos(), new Vec3<>(spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ()), component.headRot.wrapYaw());

            RenderUtils.drawTexture(resolveCompassTexture(degrees), guiGraphics, x, y, size, size, 0, 0);
        }
    }
}
