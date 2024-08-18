package dev.boxadactle.coordinatesdisplay;

import dev.boxadactle.boxlib.command.BCommandManager;
import dev.boxadactle.boxlib.config.BConfigClass;
import dev.boxadactle.boxlib.config.BConfigHandler;
import dev.boxadactle.boxlib.util.ModLogger;
import dev.boxadactle.coordinatesdisplay.command.CoordinatesCommand;
import dev.boxadactle.coordinatesdisplay.hud.Hud;

public class CoordinatesDisplay {

	public static final String MOD_NAME = "CoordinatesDisplay";

	public static final String MOD_ID = "coordinatesdisplay";

	public static final String VERSION = "8.1.1";

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

	static {
		LOGGER.info("Initializing " + MOD_NAME + " v" + VERSION);

		// register commands
		BCommandManager.register(CoordinatesCommand.create());
	}

	public static void init() {
		// register config
		CONFIG = BConfigHandler.registerConfig(ModConfig.class);

		// initialize HUD
		HUD = new Hud();
	}

	public static ModConfig getConfig() {
		return CONFIG.get();
	}

}
