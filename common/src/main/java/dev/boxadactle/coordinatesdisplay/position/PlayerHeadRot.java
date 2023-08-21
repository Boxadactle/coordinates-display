package dev.boxadactle.coordinatesdisplay.position;

import net.minecraft.util.Mth;

public class PlayerHeadRot {

    float yaw;

    float pitch;

    public PlayerHeadRot(float yaw, float pitch) {

        this.yaw = yaw;
        this.pitch = pitch;

    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public double wrapPitch() {
        return Mth.wrapDegrees(pitch);
    }

    public double wrapYaw() {
        return Mth.wrapDegrees(yaw);
    }
}
