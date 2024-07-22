package dev.boxadactle.coordinatesdisplay.position;

import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

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

            biome = BuiltinRegistries.BIOME.getHolderOrThrow(Biomes.PLAINS);
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

    public Holder<Biome> getBiome() {
        return biome;
    }

    public ResourceLocation getBiomeKey() {
        Registry<Biome> registry = WorldUtils.getWorld() != null ? WorldUtils.getWorld().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY) : BuiltinRegistries.BIOME;
        return registry.getKey(biome.value());
    }
}
