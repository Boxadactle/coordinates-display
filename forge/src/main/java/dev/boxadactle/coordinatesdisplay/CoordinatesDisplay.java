package dev.boxadactle.coordinatesdisplay;

import dev.boxadactle.boxlib.ModConstantsProvider;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.ModLogger;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.init.Commands;
import dev.boxadactle.coordinatesdisplay.util.HudRenderer;
import dev.boxadactle.coordinatesdisplay.util.ModConfig;
import dev.boxadactle.coordinatesdisplay.util.ModConstants;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.gui.ConfigScreen;
import dev.boxadactle.coordinatesdisplay.gui.CoordinatesScreen;
import dev.boxadactle.coordinatesdisplay.gui.config.HudPositionScreen;
import dev.boxadactle.coordinatesdisplay.init.OverlayRenderers;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod("coordinatesdisplay")
public class CoordinatesDisplay {

    public static ModLogger LOGGER;

    public static boolean shouldConfigGuiOpen = false;
    public static boolean shouldCoordinatesGuiOpen = false;
    public static boolean shouldHudPositionGuiOpen = false;

    public static ConfigHolder<ModConfig> CONFIG;

    public static HudRenderer OVERLAY;

    public CoordinatesDisplay() {
        ModConstantsProvider.registerProvider(ModConstants.class);

        LOGGER = new ModLogger("CoordinatesDisplay");
        LOGGER.info("Loading " + ModConstantsProvider.getProvider("coordinatesdisplay").getString());

        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class);

        // what a pain
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen)));

        LOGGER.info("Registering overlay renderers...");
        OVERLAY = new HudRenderer();
        OverlayRenderers.registerOverlayRenderers(OVERLAY);

        LOGGER.info("Registering commands...");
        Commands.register();

        ModUtil.init();

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ModConstantsProvider getModConstants() {
        return ModConstantsProvider.getProvider("coordinatesdisplay");
    }

    public static ModConfig getConfig() {
        return CONFIG.get();
    }

    public static void tick() {
        if (shouldConfigGuiOpen) {
            ClientUtils.setScreen(new ConfigScreen(null));
            shouldConfigGuiOpen = false;
        }

        if (shouldCoordinatesGuiOpen) {
            Position pos = Position.of(WorldUtils.getCamera());

            ClientUtils.setScreen(new CoordinatesScreen(pos));

            shouldCoordinatesGuiOpen = false;
        }

        if (shouldHudPositionGuiOpen) {
            ClientUtils.setScreen(new HudPositionScreen(null));
            shouldHudPositionGuiOpen = false;
        }
    }

    public static class BiomeColors {

        public static int getBiomeColor(String name, int defaultColor) {

            return switch (name) {
                case "Eroded Badlands", "Badlands" -> 0xb55a26;
                case "Bamboo Jungle" -> 0x2be625;
                case "Snowy Beach", "Snowy Plains", "Snowy Slopes", "Snowy Taiga", "Basalt Deltas" -> 0xadadad;
                case "Beach" -> 0xc5c93a;
                case "Birch Forest", "Old Growth Birch Forest" -> 0xdecc7a;
                case "Cold Ocean" -> 0x738ae6;
                case "Crimson Forest", "Nether Wastes" -> 0xad201d;
                case "Dark Forest" -> 0x452309;
                case "River", "Ocean", "Deep Cold Ocean" -> 0x161d78;
                case "Deep Dark" -> 0x03273d;
                case "Deep Frozen Ocean" -> 0x1e4054;
                case "Deep Lukewarm Ocean" -> 0x235b63;
                case "Deep Ocean" -> 0x15115c;
                case "End Barrens", "End Highlands", "End Midlands", "Small End Islands", "Desert" -> 0xb3ac30;
                case "Dripstone Caves" -> 0x665f50;
                case "Flower Forest", "Forest", "Lush Caves", "Meadow" -> 0x32701c;
                case "Frozen Ocean", "Frozen Peaks", "Frozen River", "Ice Spikes" -> 0x34c4c9;
                case "Grove", "Jagged Peaks" -> 0xacb0a7;
                case "Jungle" -> 0x85c41f;
                case "Lukewarm Ocean" -> 0x3d9ba8;
                case "Mushroom Fields" -> 0x4c4654;
                case "Old Growth Pine Taiga", "Old Growth Spruce Forest" -> 0x3b230d;
                case "Plains", "Sunflower Plains" -> 0x4dd115;
                case "Savanna", "Savanna Plateau" -> 0x5c701c;
                case "Cherry Grove" -> 0xd863e0;
                default -> defaultColor;
            };

        }

    }
}
