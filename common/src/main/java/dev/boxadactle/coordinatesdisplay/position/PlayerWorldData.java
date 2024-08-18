package dev.boxadactle.coordinatesdisplay.position;

import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class PlayerWorldData {

    ResourceLocation dimension;

    Holder<Biome> biome;

    public PlayerWorldData(BlockPos player) {
        if (WorldUtils.getWorld() != null) {
            dimension = WorldUtils.getPlayer().level.dimension().location();

            biome = WorldUtils.getWorld().getBiome(player);
        } else {
            CoordinatesDisplay.LOGGER.warn("Client world is null! Resorting to default values.");

            dimension = new ResourceLocation("minecraft", "overworld");
        }
    }

    private String formatName(String orig) {
        StringBuilder name = new StringBuilder();

        for (String word : orig.split("_")) {
            name.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
        }

        return name.toString().trim();
    }

    public String getDimension(boolean formatted) {
        return formatted ? formatName(dimension.getPath()) : dimension.toString();
    }

    public Biome getBiome() {
        if (biome != null) {
            return biome.value();
        } else {
            return null;
        }
    }

    public ResourceLocation getBiomeKey() {
        ResourceLocation def = new ResourceLocation("minecraft", "plains");
        if (biome == null) {
            return def;
        }
        return biome.unwrap().map(ResourceKey::location, (biome) -> def);
    }
}
