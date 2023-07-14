package dev.boxadactle.coordinatesdisplay.util.position;

import dev.boxadactle.boxlib.math.Vec2;
import dev.boxadactle.boxlib.math.Vec3;

public class PlayerPos<P extends Number, C extends Number> {

    Vec3<P> playerPos;

    Vec2<C> chunkPos;

    public PlayerPos(P x, P y, P z, C chunkX, C chunkY) {

        playerPos = new Vec3<>(x, y, z);

        this.chunkPos = new Vec2<>(chunkX, chunkY);

    }

    public Vec3<P> getPlayerPos() {
        return playerPos;
    }

    public Vec2<C> getChunkPos() {
        return chunkPos;
    }

    public P getX() {
        return playerPos.getX();
    }

    public P getY() {
        return playerPos.getY();
    }

    public P getZ() {
        return playerPos.getZ();
    }

    public C getChunkX() {
        return chunkPos.getX();
    }

    public C getChunkY() {
        return chunkPos.getY();
    }
}

