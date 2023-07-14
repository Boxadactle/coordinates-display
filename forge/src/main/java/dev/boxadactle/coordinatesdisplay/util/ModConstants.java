package dev.boxadactle.coordinatesdisplay.util;

import dev.boxadactle.boxlib.ModConstantsProvider;

public class ModConstants extends ModConstantsProvider {
    @Override
    public String getName() {
        return "CoordinatesDisplay";
    }

    @Override
    public String getModId() {
        return "coordinatesdisplay";
    }

    @Override
    public String getVersion() {
        return "3.0.0";
    }

    @Override
    public String[] getAuthors() {
        return new String[] { "Boxadactle" };
    }

    @Override
    public String getWiki() {
        return "https://boxadactle.dev/wiki/coordinates-display/";
    }
}
