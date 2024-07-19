package dev.boxadactle.coordinatesdisplay.position;

import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class PlayerWorldData {

    ResourceLocation dimension;

    Biome biome;

    public PlayerWorldData(BlockPos player) {
        if (WorldUtils.getWorld() != null) {
            dimension = WorldUtils.getPlayer().level.dimension().location();

            biome = WorldUtils.getWorld().getBiome(player);
        } else {
            CoordinatesDisplay.LOGGER.warn("Client world is null! Resorting to default values.");

            dimension = new ResourceLocation("minecraft", "overworld");

            biome = Biomes.PLAINS;
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
        return biome;
    }

    public ResourceLocation getBiomeKey() {
        Registry<Biome> registry = WorldUtils.getWorld() != null ? WorldUtils.getWorld().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY) : BuiltinRegistries.BIOME;
        return registry.getKey(biome);
    }
}
