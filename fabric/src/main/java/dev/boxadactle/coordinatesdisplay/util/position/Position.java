package dev.boxadactle.coordinatesdisplay.util.position;

import dev.boxadactle.boxlib.math.Vec2;
import dev.boxadactle.boxlib.math.Vec3;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class Position {

    PlayerPos<Double, Integer> playerPos;
    PlayerHeadRot<Float, Float> playerHeadRot;
    RegistryEntry<Biome> biomeHolder;

    public static Position of(Entity camera) {
        Vec3<Double> vec3 = new Vec3<>(camera.getX(), camera.getY(), camera.getZ());
        Vec3<Integer> vec3i = ModUtil.doubleVecToIntVec(vec3);
        ChunkPos pos = new ChunkPos(new BlockPos(ModUtil.toMinecraftVector(vec3i)));

        float cameraYaw = camera.getYaw();
        float cameraPitch = camera.getPitch();

        return new Position(
                camera.getX(),
                camera.getY(),
                camera.getZ(),
                pos.x,
                pos.z,
                cameraYaw,
                cameraPitch,
                MinecraftClient.getInstance().world.getBiome(new BlockPos(ModUtil.toMinecraftVector(vec3i)))
        );
    }

    public static Position of(double x, double y, double z, int chunkX, int chunkY, float cameraYaw, float cameraPitch, @Nullable RegistryEntry<Biome> biomeHolder) {
        return new Position(x, y, z, chunkX, chunkY, cameraYaw, cameraPitch, biomeHolder);
    }

    protected Position(double x, double y, double z, int chunkX, int chunkY, float cameraYaw, float cameraPitch, @Nullable RegistryEntry<Biome> biomeHolder) {
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
        return ModUtil.getBiomeString(this.biomeHolder);
    }

}
