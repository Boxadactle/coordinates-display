package dev.boxadactle.coordinatesdisplay.position;

import dev.boxadactle.boxlib.math.geometry.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;

public class Position {

    public PlayerPos position;
    public PlayerHeadRot headRot;
    public PlayerWorldData world;
    public PlayerTargetBlock block;

    public static Position of(Player player) {
        Vec3<Double> a = new Vec3<>(player.getX(), player.getY(), player.getZ());
        BlockPos b = new BlockPos(player.getBlockX(), player.getBlockY(), player.getBlockZ());

        return new Position(
                a, new ChunkPos(b), b,
                player.getYHeadRot(), player.getXRot(),
                new PlayerTargetBlock(player)
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
        position = new PlayerPos(player.getX(), player.getY(), player.getZ(), chunkPos, playerPos);
        headRot = new PlayerHeadRot(yaw, pitch);
        world = new PlayerWorldData(playerPos);
        block = playerTargetBlock;
    }

}
