package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.hud.DisplayMode;
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
@DisplayMode(
        value = "spawnpoint",
        hasXYZ = false,
        hasChunkData = false,
        hasDirection = false,
        hasDirectionInt = false,
        hasBiome = false,
        hasMCVersion = false,
        hasDimension = false
)
public class SpawnpointRenderer implements HudRenderer {

    public static ResourceLocation SOUTH = new ResourceLocation("textures/item/compass_00.png");
    public static ResourceLocation WEST = new ResourceLocation("textures/item/compass_07.png");
    public static ResourceLocation NORTH = new ResourceLocation("textures/item/compass_16.png");
    public static ResourceLocation EAST = new ResourceLocation("textures/item/compass_25.png");
    
    // unfortunately, I don't think you can access the player's
    // spawnpoint unless your mod is server-side
    private BlockPos resolveWorldSpawn() {
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
    
    private int calculateWidth(
            Component playerLabel,
            Triplet<Component, Component, Component> player,
            Component spawnpointLabel,
            Triplet<Component, Component, Component> spawnpointXYZ
    ) {
        int p = CoordinatesDisplay.getConfig().padding;
        int tp = CoordinatesDisplay.getConfig().textPadding;
        
        int pWidth = GuiUtils.getLongestLength(playerLabel, player.getA(), player.getB(), player.getC());
        int sWidth = GuiUtils.getLongestLength(spawnpointLabel, spawnpointXYZ.getA(), spawnpointXYZ.getB(), spawnpointXYZ.getC());
        
        return (p * 2) + pWidth + tp + sWidth;
    }
    
    private int calculateHeight() {
        int p = CoordinatesDisplay.getConfig().padding;
        int tp = CoordinatesDisplay.getConfig().textPadding;
        
        // we have 4 components (x, y, z, label)
        int pHeight = GuiUtils.getTextHeight() * 4;
        int rHeight = GuiUtils.getTextHeight() * 4;
        
        return (p * 2) + pHeight + tp + rHeight;
    }

    private double calculateRelativeDirection(Vec3<Integer> pos1, Vec3<Integer> pos2, double yaw) {
        int x = pos2.getX() - pos1.getX();
        int z = pos2.getZ() - pos1.getZ();

        double startDirection = Math.toDegrees(Math.atan2(z, x)) + 180;
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
        return new ResourceLocation("minecraft", texture);
    }

    @Override
    public Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos) {
        BlockPos spawnpoint = resolveWorldSpawn();

        // player
        Component playerLabel = definition("player", "");
        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);
        Triplet<Component, Component, Component> xyz = createXYZ(player.getA(), player.getB(), player.getC());

        // spawnpoint
        Component spawnpointLabel = definition("worldSpawn", "");
        Triplet<Component, Component, Component> spawnpointXYZ = createXYZ(spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ());

        // relative position
        Component relativeLabel = definition("relative", "");
        Tuple<Vec3<Double>, Vec3<Integer>> relativePos = createRelativePosition(
                pos.position.getPlayerPos(),
                pos.position.getBlockPos(),
                new BlockPos(spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ())
        );
        Triplet<String, String, String> relative = this.roundPosition(relativePos.getA(), relativePos.getB(), CoordinatesDisplay.getConfig().decimalPlaces);
        Triplet<Component, Component, Component> relativeXYZ = createXYZ(relative.getA(), relative.getB(), relative.getC());

        int w = calculateWidth(playerLabel, xyz, spawnpointLabel, spawnpointXYZ);
        int h = calculateHeight();
        
        // render
        if (config().renderBackground) {
            RenderUtils.drawSquare(guiGraphics, x, y, w, h, CoordinatesDisplay.getConfig().backgroundColor);
        }

        int p = CoordinatesDisplay.getConfig().padding;
        int th = GuiUtils.getTextRenderer().lineHeight;
        int tp = CoordinatesDisplay.getConfig().textPadding;
        
        // XYZ
        drawInfo(guiGraphics, playerLabel, x + p, y + p, CoordinatesDisplay.getConfig().definitionColor);
        drawInfo(guiGraphics, xyz.getA(), x + p, y + p + th, CoordinatesDisplay.getConfig().definitionColor);
        drawInfo(guiGraphics, xyz.getB(), x + p, y + p + (th * 2), CoordinatesDisplay.getConfig().definitionColor);
        drawInfo(guiGraphics, xyz.getC(), x + p, y + p + (th * 3), CoordinatesDisplay.getConfig().definitionColor);

        // spawnpoint
        int sStart = p + GuiUtils.getLongestLength(playerLabel, xyz.getA(), xyz.getB(), xyz.getC()) + tp;
        drawInfo(guiGraphics, spawnpointLabel, x + sStart, y + p, CoordinatesDisplay.getConfig().definitionColor);
        drawInfo(guiGraphics, spawnpointXYZ.getA(), x + sStart, y + p + th, CoordinatesDisplay.getConfig().definitionColor);
        drawInfo(guiGraphics, spawnpointXYZ.getB(), x + sStart, y + p + (th * 2), CoordinatesDisplay.getConfig().definitionColor);
        drawInfo(guiGraphics, spawnpointXYZ.getC(), x + sStart, y + p + (th * 3), CoordinatesDisplay.getConfig().definitionColor);

        // relative
        int rStart = p + GuiUtils.getTextHeight() * 4 + tp;
        drawInfo(guiGraphics, relativeLabel, x + p, y + rStart, CoordinatesDisplay.getConfig().definitionColor);
        drawInfo(guiGraphics, relativeXYZ.getC(), x + p, y + rStart + (th * 3), CoordinatesDisplay.getConfig().definitionColor);
        drawInfo(guiGraphics, relativeXYZ.getA(), x + p, y + rStart + th, CoordinatesDisplay.getConfig().definitionColor);
        drawInfo(guiGraphics, relativeXYZ.getB(), x + p, y + rStart + (th * 2), CoordinatesDisplay.getConfig().definitionColor);

        // compass
        double degrees = calculateRelativeDirection(pos.position.getBlockPos(), new Vec3<>(spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ()), pos.headRot.wrapYaw());
        ResourceLocation compassTexture = resolveCompassTexture(degrees);
        int size = 32;
        guiGraphics.blit(
                compassTexture,
                x + w - p - size,
                y + h - p - size,
                size, size,
                size, size,
                size, size
        );

        return new Rect<>(x, y, w, h);
    }
}
