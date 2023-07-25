package dev.boxadactle.coordinatesdisplay.util.position;

import dev.boxadactle.boxlib.math.Vec2;
import dev.boxadactle.boxlib.math.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;

public class PlayerPos {

    Vec3<Double> playerPos;

    Vec2<Integer> chunkPos;

    Vec3<Integer> blockPos;

    int chunkY;

    public PlayerPos(double x, double y, double z, ChunkPos chunkPos, BlockPos pos) {

        playerPos = new Vec3<>(x, y, z);

        this.chunkPos = new Vec2<>(chunkPos.x, chunkPos.z);

        blockPos = new Vec3<>(pos.getX(), pos.getY(), pos.getZ());

        chunkY = SectionPos.blockToSectionCoord(blockPos.getY());
    }

    public Vec3<Double> getPlayerPos() {
        return playerPos;
    }

    public Vec2<Integer> getChunkPos() {
        return chunkPos;
    }

    public int getChunkY() {
        return chunkY;
    }

    public Vec3<Integer> getBlockPos() {
        return blockPos;
    }

    public Vec3<Integer> getBlockPosInChunk() {
        return new Vec3<>(blockPos.getX() & 15, blockPos.getY() & 15, blockPos.getZ() & 15);
    }

}

