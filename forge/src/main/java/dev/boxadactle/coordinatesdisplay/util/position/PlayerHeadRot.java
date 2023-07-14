package dev.boxadactle.coordinatesdisplay.util.position;

import net.minecraft.util.Mth;

public class PlayerHeadRot<Y extends Number, P extends Number> {

    Y yaw;

    P pitch;

    public PlayerHeadRot(Y yaw, P pitch) {

        this.yaw = yaw;
        this.pitch = pitch;

    }

    public P getPitch() {
        return pitch;
    }

    public Y getYaw() {
        return yaw;
    }

    public double wrapPitch() {
        return Mth.wrapDegrees(Double.parseDouble(String.valueOf(yaw)));
    }

    public double wrapYaw() {
        return Mth.wrapDegrees(Double.parseDouble(String.valueOf(yaw)));
    }
}
