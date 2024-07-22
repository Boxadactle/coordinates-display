package dev.boxadactle.coordinatesdisplay;

import dev.boxadactle.boxlib.util.WorldUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.awt.*;

public class WorldColors {

    public static int getBiomeColor(ResourceLocation key, Biome biome) {
        if (biome == null) {
            return 0x24BC07;
        }

        String biomeName = key.getPath().toLowerCase();

        if (biomeName.contains("end")) return 0xC5BE8B;
        if (biomeName.contains("ocean") || biomeName.contains("river") || biomeName.contains("swamp")) return biome.getWaterColor();
        if (biomeName.contains("nether")) return new Color(biome.getFogColor()).brighter().brighter().getRGB();
        if (biomeName.contains("icy")) return 0x84ecf0;
        if (biomeName.contains("beach")) return 0xfade55;
        else return biome.getFoliageColor();
    }

    public static int getDimensionColor(String name, int defaultColor) {
        return switch (name.toLowerCase()) {
            case "overworld" -> 0x00ff00;
            case "nether" -> 0xff0000;
            case "end" -> 0xC5BE8B;
            default -> {
                if (name.startsWith("The ")) {
                    yield getDimensionColor(name.substring(4), defaultColor);
                } else {
                    yield defaultColor;
                }
            }
        };
    }

}
