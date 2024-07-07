package dev.boxadactle.coordinatesdisplay.config;

import dev.boxadactle.boxlib.core.BoxLib;
import dev.boxadactle.boxlib.config.BConfig;
import dev.boxadactle.boxlib.config.BConfigFile;
import dev.boxadactle.coordinatesdisplay.hud.HudPositionModifier;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.hud.renderer.*;
import dev.boxadactle.coordinatesdisplay.hud.modifier.*;
import dev.boxadactle.coordinatesdisplay.position.Position;
import dev.boxadactle.coordinatesdisplay.ModUtil;

import java.util.function.Function;

@BConfigFile("coordinates-display")
public class ModConfig implements BConfig {

    public boolean enabled = true;

    public int decimalPlaces = 0;
    public String renderMode = "default";
    public StartCorner startCorner = StartCorner.TOP_LEFT;
    public String visibilityFilter = "always";
    public int hudX = 0;
    public int hudY = 0;
    public float hudScale = 1.0f;
    public boolean hudTextShadow = true;

    public boolean renderBackground = true;
    public boolean renderXYZ = true;
    public boolean renderChunkData = true;
    public boolean renderDirection = true;
    public boolean renderDirectionInt = true;
    public boolean renderBiome = true;
    public boolean biomeColors = true;
    public boolean dimensionColors = true;
    public boolean renderMCVersion = true;
    public boolean renderDimension = true;

    public int definitionColor = 0x55FF55;
    public int dataColor = 0xFFFFFF;
    public int deathPosColor = 0x55FFFF;
    public int backgroundColor = 0x405c5c5c;

    public boolean displayPosOnDeathScreen = true;
    public boolean showDeathPosInChat = true;

    public int padding = 5;
    public int textPadding = 5;

    public String posChatMessage = "{x} {y} {z}";
    public String copyPosMessage = "{x}, {y}, {z}";
    public boolean shouldRoundWhenCopying = true;
    public TeleportMode teleportMode = TeleportMode.EXECUTE;

    public enum TeleportMode {
        EXECUTE(ModUtil::toExecuteCommand),
        TP(ModUtil::toTeleportCommand),
        BARITONE(ModUtil::toBaritoneCommand);

        final Function<Position, String> converter;

        TeleportMode(Function<Position, String> converter) {
            this.converter = converter;
        }

        public String toCommand(Position pos) {
            return converter.apply(pos);
        }
    }

    public enum StartCorner {
        TOP_LEFT(TopLeftModifier.class),
        TOP_RIGHT(TopRightModifier.class),
        BOTTOM_LEFT(BottomLeftModifier.class),
        BOTTOM_RIGHT(BottomRightModifier.class);

        final HudPositionModifier modifier;

        StartCorner(Class<? extends HudPositionModifier> modifier) {
            this.modifier = BoxLib.initializeClass(modifier);
        }

        public HudPositionModifier getModifier() {
            return modifier;
        }
    }

}