package me.boxadactle.coordinatesdisplay.util;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.SharedConstants;

public class ModVersion {

    static String currentVer;

    public ModVersion() {
        currentVer = "2.2.0";
    }

    public String getVersion() {
        return currentVer;
    }

    public String toString() {
        return CoordinatesDisplay.MOD_ID + " v" + this.getVersion();
    }

    public String getMCVersion() {
        return SharedConstants.getCurrentVersion().getName();
    }
}