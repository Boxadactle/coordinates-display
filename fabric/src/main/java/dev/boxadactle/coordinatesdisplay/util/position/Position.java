package dev.boxadactle.coordinatesdisplay.util.position;

import dev.boxadactle.boxlib.math.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class Position {

    public PlayerPos position;
    public PlayerHeadRot headRot;
    public PlayerWorldData world;
    public PlayerTargetBlock block;

    public static Position of(Entity camera) {
        Vec3<Double> a = new Vec3<>(camera.getX(), camera.getY(), camera.getZ());

        return new Position(
                a, camera.getChunkPos(), camera.getBlockPos(),
                camera.getYaw(), camera.getPitch(),
                new PlayerTargetBlock(camera)
        );
    }

    public static Position of(
            Vec3<Double> player, ChunkPos chunkPos, BlockPos playerPos,
            float yaw, float pitch,
            BlockPos targetPos, String targetName
    ) {
        return new Position(
                player, chunkPos, playerPos,
                yaw, pitch,
                new PlayerTargetBlock(targetPos, targetName)
        );
    }

    protected Position(
            Vec3<Double> player, ChunkPos chunkPos, BlockPos playerPos,
            float yaw, float pitch,
            PlayerTargetBlock playerTargetBlock
    ) {
        position = new PlayerPos(player.getX(), player.getY(), playerPos.getZ(), chunkPos, playerPos);
        headRot = new PlayerHeadRot(yaw, pitch);
        world = new PlayerWorldData(playerPos);
        block = playerTargetBlock;
    }

}
