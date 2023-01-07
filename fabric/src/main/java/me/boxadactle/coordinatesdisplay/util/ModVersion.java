package me.boxadactle.coordinatesdisplay.util;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
public class ModVersion {

    static String thisVer = "2.1.4";

    public static String getVersion() {
        return thisVer;
    }

    public static String getString() {
        return CoordinatesDisplay.MOD_ID + " v" + getVersion();
    }
}