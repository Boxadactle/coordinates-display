package me.boxadactle.coordinatesdisplay.util;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;

public class ModVersion {

    static String currentVer;

    public ModVersion() {
        currentVer = "2.1.4";
    }

    public String getVersion() {
        return currentVer;
    }

    public String toString() {
        return CoordinatesDisplay.MOD_ID + " v" + this.getVersion();
    }
}