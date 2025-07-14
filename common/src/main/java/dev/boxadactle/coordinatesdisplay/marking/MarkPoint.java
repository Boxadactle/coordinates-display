package dev.boxadactle.coordinatesdisplay.marking;

import dev.boxadactle.boxlib.math.geometry.Vec3;

public record MarkPoint(int x, int y, int z) {
    public MarkPoint(Vec3<Integer> vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Vec3<Integer> toVec3() {
        return new Vec3<>(x, y, z);
    }

    public static String createCommand(Vec3<Integer> vec) {
        return "/coordinates mark set " + vec.x + " " + vec.y + " " + vec.z;
    }
}