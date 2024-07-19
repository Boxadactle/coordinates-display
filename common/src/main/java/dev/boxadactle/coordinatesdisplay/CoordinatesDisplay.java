package dev.boxadactle.coordinatesdisplay;

import com.mojang.blaze3d.platform.InputConstants;
import dev.boxadactle.boxlib.config.BConfigClass;
import dev.boxadactle.boxlib.config.BConfigHandler;
import dev.boxadactle.boxlib.scheduling.Scheduling;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.ModLogger;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.registry.DisplayMode;
import dev.boxadactle.coordinatesdisplay.config.screen.HudPositionScreen;
import dev.boxadactle.coordinatesdisplay.hud.Hud;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.level.biome.Biome;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class CoordinatesDisplay {

	public static final String MOD_NAME = "CoordinatesDisplay";

	public static final String MOD_ID = "coordinatesdisplay";

	public static final String VERSION = "2.4.0";

	public static final String VERSION_STRING = MOD_NAME + " v" + VERSION;


	// wiki pages
	public static final String WIKI = "https://boxadactle.dev/wiki/coordinates-display/";
	public static String WIKI_VISUAL = WIKI + "#visual";
	public static String WIKI_RENDER = WIKI + "#rendering";
	public static String WIKI_COLOR = WIKI + "#color";
	public static String WIKI_DEATHPOS = WIKI + "#deathpos";
	public static String WIKI_TEXTS = WIKI + "#text";


	public static final ModLogger LOGGER = new ModLogger(MOD_NAME);

	public static boolean shouldHudRender = true;

	public static BConfigClass<ModConfig> CONFIG;

	public static Hud HUD;

	public static void init() {
		LOGGER.info("Initializing " + MOD_NAME + " v" + VERSION);

		LOGGER.info("Loading config file");
		CONFIG = BConfigHandler.registerConfig(ModConfig.class);

		LOGGER.info("Initializing hud");
		HUD = new Hud();
	}

	public static ModConfig getConfig() {
		return CONFIG.get();
	}

	public static class BiomeColors {

		public static int getBiomeColor(Biome biome) {
			return switch (biome.getBiomeCategory()) {
				case THEEND -> 0xC5BE8B;
				case OCEAN, RIVER, SWAMP -> biome.getWaterColor();
				case NETHER -> new Color(biome.getFogColor()).brighter().brighter().getRGB();
				case ICY -> 0x84ecf0;
				case BEACH -> 0xfade55;
				default -> biome.getFoliageColor();
			};
		}

		public static int getDimensionColor(String name, int defaultColor) {
			return switch (name.toLowerCase()) {
				case "overworld" -> 0x00ff00;
				case "nether" -> 0xff0000;
				case "end" -> 0xC5BE8B;
				default -> {
					if (name.contains("The ")) {
						yield getDimensionColor(name.substring(4), defaultColor);
					} else {
						yield defaultColor;
					}
				}
			};
		}

	}

	public static class Bindings {
		public static final KeyMapping hudEnabled = new KeyMapping("key.coordinatesdisplay.hudenabled", GLFW.GLFW_KEY_H, "category.coordinatesdisplay");

		public static final KeyMapping coordinatesGUIKeybind = new KeyMapping("key.coordinatesdisplay.coordinatesgui", GLFW.GLFW_KEY_C, "category.coordinatesdisplay");

		public static final KeyMapping copyLocation = new KeyMapping("key.coordinatesdisplay.copypos", -1, "category.coordinatesdisplay");
		public static final KeyMapping sendLocation = new KeyMapping("key.coordinatesdisplay.sendpos", -1, "category.coordinatesdisplay");
		public static final KeyMapping copyPosTp = new KeyMapping("key.coordinatesdisplay.copypostp", -1, "category.coordinatesdisplay");

		public static final KeyMapping changeHudPosition = new KeyMapping("key.coordinatesdisplay.changeHudPos", GLFW.GLFW_KEY_F9, "category.coordinatesdisplay");
		public static final KeyMapping cycleDisplayMode = new KeyMapping("key.coordinatesdisplay.cycleDisplayMode", GLFW.GLFW_KEY_M, "category.coordinatesdisplay");

		public static void toggleHud() {
			CoordinatesDisplay.LOGGER.info("Toggling HUD visibility");
			CONFIG.get().enabled = !CONFIG.get().enabled;
			CONFIG.save();
		}

		public static void coordinatesGui() {
			Scheduling.nextTick(() -> ClientUtils.setScreen(new CoordinatesScreen(Position.of(WorldUtils.getPlayer()))));
		}

		public static void copyLocation(Position pos) {
			ClientUtils.getKeyboard().setClipboard(ModUtil.parseText(CONFIG.get().copyPosMessage, pos));
			LOGGER.player.info("Copied to clipboard!");
			LOGGER.info("Copied location to clipboard");
		}

		public static void sendLocation(Position pos) {
			LOGGER.player.publicChat(ModUtil.parseText(CONFIG.get().posChatMessage, pos));
			LOGGER.info("Sent position as chat message");
		}

		public static void copyTeleportCommand(Position pos) {
			ClientUtils.getKeyboard().setClipboard(getConfig().teleportMode.toCommand(pos));

			LOGGER.player.info("Copied position as teleport command!");
		}

		public static void openHudPositionGui() {
			Scheduling.nextTick(() -> ClientUtils.setScreen(new HudPositionScreen(null)));
		}

		public static void cycleDisplayMode() {
			if (!InputConstants.isKeyDown(ClientUtils.getWindow(), 340)) getConfig().renderMode = DisplayMode.nextMode(getConfig().renderMode);
			else getConfig().renderMode = DisplayMode.previousMode(getConfig().renderMode);

			CONFIG.save();
		}
	}

}
