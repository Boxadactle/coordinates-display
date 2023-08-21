package dev.boxadactle.coordinatesdisplay.config;

import dev.boxadactle.boxlib.base.BoxLib;
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

    public boolean visible = true;
    public int decimalPlaces = 0;
    public RenderMode renderMode = RenderMode.DEFAULT;
    public StartCorner startCorner = StartCorner.TOP_LEFT;
    public int hudX = 0;
    public int hudY = 0;
    public float hudScale = 1.0f;
    public boolean hudTextShadow = true;

    public boolean renderBackground = true;
    public boolean renderChunkData = true;
    public boolean renderDirection = true;
    public boolean renderDirectionInt = true;
    public boolean renderBiome = true;
    public boolean biomeColors = true;
    public boolean renderMCVersion = true;

    public int definitionColor = 0x55FF55;
    public int dataColor = 0xFFFFFF;
    public int deathPosColor = 0x55FFFF;
    public int backgroundColor = 0x405c5c5c;

    public boolean displayPosOnDeathScreen = true;
    public boolean showDeathPosInChat = true;

    public int padding = 5;
    public int textPadding = 10;

    public String posChatMessage = "{x} {y} {z}";
    public String copyPosMessage = "{x}, {y}, {z}";
    public boolean shouldRoundWhenCopying = true;
    public TeleportMode teleportMode = TeleportMode.EXECUTE;




    public enum RenderMode {
        DEFAULT(DefaultRenderer.class),
        MINIMUM(MinRenderer.class),
        MAXIMUM(MaxRenderer.class),
        LINE(LineRenderer.class),
        NETHER_OVERWORLD(NetherOverworldRenderer.class);

        final HudRenderer renderer;

        RenderMode(Class<? extends HudRenderer> renderer) {
            this.renderer = BoxLib.initializeClass(renderer);
        }

        public HudRenderer getRenderer() {
            return renderer;
        }
    }

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