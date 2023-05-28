package me.boxadactle.coordinatesdisplay.util;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.SharedConstants;

public class ModVersion {

    static String thisVer = "2.3.0";

    public static String getVersion() {
        return thisVer;
    }

    public static String getString() {
        return CoordinatesDisplay.MOD_ID + " v" + getVersion();
    }

    public static String getMCVersion() {
        return SharedConstants.getCurrentVersion().getName();
    }
}