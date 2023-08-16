package dev.boxadactle.coordinatesdisplay.util;

import dev.boxadactle.boxlib.BoxLib;
import dev.boxadactle.boxlib.config.BConfig;
import dev.boxadactle.boxlib.config.BConfigFile;
import dev.boxadactle.coordinatesdisplay.util.hud.*;
import dev.boxadactle.coordinatesdisplay.util.position.Position;

import java.util.function.Function;

@BConfigFile("coordinates-display")
public class ModConfig implements BConfig {

    public boolean visible = true;
    public boolean decimalRounding = true;
    public RenderMode renderMode = RenderMode.DEFAULT;
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

        private HudRenderer.Renderer renderer;

        RenderMode(Class<? extends HudRenderer.Renderer> renderer) {
            this.renderer = BoxLib.initializeClass(renderer);
        }

        public HudRenderer.Renderer getRenderer() {
            return renderer;
        }
    }

    public enum TeleportMode {
        EXECUTE(ModUtil::toExecuteCommand),
        TP(ModUtil::toTeleportCommand),
        BARITONE(ModUtil::toBaritoneCommand);

        Function<Position, String> converter;

        TeleportMode(Function<Position, String> converter) {
            this.converter = converter;
        }

        public String toCommand(Position pos) {
            return converter.apply(pos);
        }
    }

}