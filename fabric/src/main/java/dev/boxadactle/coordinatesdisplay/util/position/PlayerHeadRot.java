package dev.boxadactle.coordinatesdisplay.util.position;

import net.minecraft.util.math.MathHelper;

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
        return MathHelper.wrapDegrees(pitch);
    }

    public double wrapYaw() {
        return MathHelper.wrapDegrees(yaw);
    }
}
