package dev.boxadactle.coordinatesdisplay;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boxadactle.boxlib.command.BCommandManager;
import dev.boxadactle.boxlib.config.BConfigClass;
import dev.boxadactle.boxlib.config.BConfigHandler;
import dev.boxadactle.boxlib.rendering.RenderQueue;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.ModLogger;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.command.CoordinatesCommand;
import dev.boxadactle.coordinatesdisplay.hud.Hud;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;

public class CoordinatesDisplay {

	public static final String MOD_NAME = "CoordinatesDisplay";

	public static final String MOD_ID = "coordinatesdisplay";

	public static final String VERSION = "15.0.0";

	public static final String VERSION_STRING = MOD_NAME + " v" + VERSION;


	// wiki pages
	public static final String WIKI = "https://boxadactle.dev/wiki/coordinates-display/";
	public static String WIKI_VISUAL = WIKI + "#visual";
	public static String WIKI_RENDER = WIKI + "#rendering";
	public static String WIKI_COLOR = WIKI + "#color";
	public static String WIKI_DEATHPOS = WIKI + "#deathpos";
	public static String WIKI_TEXTS = WIKI + "#text";

	static boolean deltaError = false;

	public static final ModLogger LOGGER = new ModLogger(MOD_NAME);

	public static boolean shouldHudRender = true;

	public static BConfigClass<ModConfig> CONFIG;

	public static Hud HUD;

	static {
		LOGGER.info("Initializing " + MOD_NAME + " v" + VERSION);

		// register commands
		BCommandManager.register(CoordinatesCommand.createCommand());
	}

	public static void init() {
		// register config
		CONFIG = BConfigHandler.registerConfig(ModConfig.class);

		// initialize HUD
		HUD = new Hud();

		// register 3d compass renderer
		RenderQueue.addRenderer(new CompassRenderer3D());
	}

	public static ModConfig getConfig() {
		return CONFIG.get();
	}

	public static void renderHud(GuiGraphics graphics) {
		try {
			if (CoordinatesDisplay.HUD.shouldRender(CoordinatesDisplay.getConfig().visibilityFilter)) {
				RenderSystem.enableBlend();

				ModConfig config = CoordinatesDisplay.getConfig();

				CoordinatesDisplay.HUD.render(
						graphics,
						Hud.RenderType.HUD,
						Position.of(WorldUtils.getPlayer()),
						config.hudX,
						config.hudY,
						config.renderMode,
						config.startCorner,
						config.hudScale
				);
			}
		} catch (NullPointerException e) {
			if (deltaError) {
				throw new RuntimeException(e);
			}

			CoordinatesDisplay.LOGGER.error("Unknown error from config file");
			CoordinatesDisplay.LOGGER.printStackTrace(e);

			CoordinatesDisplay.LOGGER.player.warn(GuiUtils.getTranslatable("message.coordinatesdisplay.configError"));
			CoordinatesDisplay.CONFIG.resetConfig();

			deltaError = true;
		}
	}

}
