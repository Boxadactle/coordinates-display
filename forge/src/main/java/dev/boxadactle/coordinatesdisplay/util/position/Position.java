package dev.boxadactle.coordinatesdisplay.util.position;

import dev.boxadactle.boxlib.math.Vec2;
import dev.boxadactle.boxlib.math.Vec3;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class Position {

    PlayerPos<Double, Integer> playerPos;
    PlayerHeadRot<Float, Float> playerHeadRot;
    Holder<Biome> biomeHolder;

    public static Position of(Entity camera) {
        net.minecraft.world.phys.Vec3 vec3 = new net.minecraft.world.phys.Vec3(camera.getX(), camera.getY(), camera.getZ());
        Vec3i vec3i = ModUtil.doubleVecToIntVec(vec3);
        ChunkPos pos = new ChunkPos(new BlockPos(vec3i));

        float cameraYaw = camera.getYHeadRot();
        float cameraPitch = camera.getXRot();

        return new Position(
                camera.getX(),
                camera.getY(),
                camera.getZ(),
                pos.x,
                pos.z,
                cameraYaw,
                cameraPitch,
                Minecraft.getInstance().level.getBiome(new BlockPos(vec3i))
        );
    }

    public static Position of(double x, double y, double z, int chunkX, int chunkY, float cameraYaw, float cameraPitch, @Nullable Holder<Biome> biomeHolder) {
        return new Position(x, y, z, chunkX, chunkY, cameraYaw, cameraPitch, biomeHolder);
    }

    protected Position(double x, double y, double z, int chunkX, int chunkY, float cameraYaw, float cameraPitch, @Nullable Holder<Biome> biomeHolder) {
        playerPos = new PlayerPos<>(x, y, z, chunkX, chunkY);
        playerHeadRot = new PlayerHeadRot<>(cameraYaw, cameraPitch);
        this.biomeHolder = biomeHolder;
    }

    public Vec3<Double> getPlayerVector() {
        return playerPos.getPlayerPos();
    }

    public Vec2<Integer> getChunkVector() {
        return playerPos.getChunkPos();
    }

    public double getPitch(boolean wrap) {
        return wrap ? playerHeadRot.wrapPitch() : (double) playerHeadRot.getPitch();
    }

    public double getYaw(boolean wrap) {
        return wrap ? playerHeadRot.wrapYaw() : (double) playerHeadRot.getYaw();
    }

    public String getBiome() {
        return ModUtil.printBiome(this.biomeHolder);
    }

}
