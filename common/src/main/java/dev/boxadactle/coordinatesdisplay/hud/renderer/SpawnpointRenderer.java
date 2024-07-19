package dev.boxadactle.coordinatesdisplay.hud.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.layouts.LayoutComponent;
import dev.boxadactle.boxlib.layouts.component.LayoutContainerComponent;
import dev.boxadactle.boxlib.layouts.component.ParagraphComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.hud.DisplayMode;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.hud.Triplet;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;

// this is a bit of a mess, but it still works
@DisplayMode(
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
            return null;
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
    public Rect<Integer> renderOverlay(PoseStack stack, int x, int y, Position pos) {
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

        return renderHud(stack, new PaddingLayout(x, y, config().padding, hud));
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
                    "item/compass_16", "item/compass_17", "item/compass_18", "item/compass_19", "item/compass_20", "item/compass_21", "item/compass_22", "item/compass_23",
                    "item/compass_24", "item/compass_25", "item/compass_26", "item/compass_27", "item/compass_28", "item/compass_29", "item/compass_30", "item/compass_31",
                    "item/compass_00", "item/compass_01", "item/compass_02", "item/compass_03", "item/compass_04", "item/compass_05", "item/compass_06", "item/compass_07",
                    "item/compass_08", "item/compass_09", "item/compass_10", "item/compass_11", "item/compass_12", "item/compass_13", "item/compass_14", "item/compass_15",
                    "item/compass_16"
            };

            String texture = "textures/" + textures[(int) (range1 * textures.length)] + ".png";
            return new ResourceLocation(texture);
        }

        @Override
        public void render(PoseStack stack, int x, int y) {
            double degrees = calculateRelativeDirection(component.position.getBlockPos(), new Vec3<>(spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ()), component.headRot.wrapYaw());
            RenderUtils.drawTexture(resolveCompassTexture(degrees),
                    stack,
                    x, y,
                    size, size,
                    0, 0
            );
        }
    }
}
