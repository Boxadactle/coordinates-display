package dev.boxadactle.coordinatesdisplay.position;

import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.clock.ClockManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.timeline.Timelines;

public class PlayerWorldData {

    Identifier dimension;

    Holder<Biome> biome;

    long day;

    long time;

    public PlayerWorldData(BlockPos player) {
        if (WorldUtils.getWorld() != null) {
            dimension = WorldUtils.getPlayer().level().dimension().identifier();

            biome = WorldUtils.getWorld().getBiome(player);

            Level world = WorldUtils.getWorld();
            ClockManager clockManager = world.clockManager();
            world.registryAccess().get(Timelines.OVERWORLD_DAY)
                    .ifPresentOrElse(
                            (timeline) -> day = (timeline.value()).getPeriodCount(clockManager),
                            () -> day = -1
                    );

            time = WorldUtils.getWorld().getGameTime() % 24000L;
        } else {
            CoordinatesDisplay.LOGGER.warn("Client world is null! Resorting to default values.");

            dimension = Identifier.withDefaultNamespace("overworld");

            day = Math.round(Math.random() * 1000);

            time = Math.round(Math.random() * 24000L);
        }
    }

    public long getDay() {
        return day;
    }

    public long getTime() {
        return time;
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

    public Identifier getBiomeKey() {
        Identifier def = Identifier.withDefaultNamespace("plains");
        if (biome == null) {
            return def;
        }
        return biome.unwrap().map(ResourceKey::identifier, (biome) -> def);
    }
}
