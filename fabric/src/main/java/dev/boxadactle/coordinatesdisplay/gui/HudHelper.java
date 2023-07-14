package dev.boxadactle.coordinatesdisplay.gui;

import dev.boxadactle.boxlib.gui.widget.BCustomRenderingEntry;
import dev.boxadactle.boxlib.math.MathHelper;
import dev.boxadactle.boxlib.math.Vec3;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.ModConfig;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

public interface HudHelper {

    default Position generatePositionData() {
        Vec3d pos = new Vec3d(Math.random() * 1000, Math.random() * 5, Math.random() * 1000);
        ChunkPos chunkPos = new ChunkPos(new BlockPos(ModUtil.doubleVecToIntVec(pos)));
        float cameraYaw = (float) Math.random() * 180;
        float cameraPitch  = (float) Math.random() * 180;

        return Position.of(pos.x, pos.y, pos.z, chunkPos.x, chunkPos.z, cameraYaw, cameraPitch, null);

    }

    default Vec3<Integer> generateDeathposData() {
        int deathx = (int) Math.round(Math.random() * 1000);
        int deathy = (int) Math.round(Math.random() * 100);
        int deathz = (int) Math.round(Math.random() * 1000);

        return new Vec3<>(deathx, deathy, deathz);
    }

    default String generateDimensionData() {
        return (String) MathHelper.selectRandom("minecraft:overworld", "minecraft:the_nether", "minecraft:the_end");
    }

    default ModConfig config() {
        return CoordinatesDisplay.getConfig();
    }

    default BCustomRenderingEntry createHudRenderEntry(Position pos) {
        return new BCustomRenderingEntry((drawContext, x, y, width, height, mouseX, mouseY, tickDelta) -> {
            CoordinatesDisplay.OVERLAY.render(
                    drawContext,
                    (x + width / 2) - CoordinatesDisplay.OVERLAY.getWidth() / 2,
                    y + 3,
                    pos,
                    CoordinatesDisplay.getConfig().renderMode,
                    false
            );
        });
    }

}
