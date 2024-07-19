package dev.boxadactle.coordinatesdisplay.registry;

import dev.boxadactle.boxlib.core.BoxLib;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.hud.renderer.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public enum DisplayMode {
    DEFAULT(DefaultRenderer.class),
    MINIMUM(MinRenderer.class),
    MAXIMUM(MaxRenderer.class),
    LINE(LineRenderer.class),
    NETHER_OVERWORLD(NetherOverworldRenderer.class),
    HOTBAR(HotbarRenderer.class),
//    SPAWNPOINT(SpawnpointRenderer.class), spawnpoint cannot be accessed in 1.16.5
    DIRECTION(DirectionRenderer.class),
    CHUNK(ChunkRenderer.class);

    final HudRenderer renderer;
    final dev.boxadactle.coordinatesdisplay.hud.DisplayMode metadata;

    DisplayMode(Class<? extends HudRenderer> renderer) {
        this.renderer = BoxLib.initializeClass(renderer);

        dev.boxadactle.coordinatesdisplay.hud.DisplayMode m = renderer.getAnnotation(dev.boxadactle.coordinatesdisplay.hud.DisplayMode.class);
        if (m != null) {
            metadata = m;
        } else {
            throw new IllegalStateException("Attempting to register Hud renderer without DisplayMode annotation!");
        }
    }

    public dev.boxadactle.coordinatesdisplay.hud.HudRenderer getRenderer() {
        return renderer;
    }

    public dev.boxadactle.coordinatesdisplay.hud.DisplayMode getMetadata() {
        return metadata;
    }

    public Component getComponent() {
        return new TranslatableComponent(renderer.getNameKey());
    }

    public String getName() {
        return GuiUtils.getTranslatable(renderer.getTranslationKey());
    }

    public String getId() {
        return metadata.value();
    }

    public static DisplayMode previousMode(DisplayMode mode) {
        DisplayMode[] values = mode.getClass().getEnumConstants();
        int currentIndex = 0;

        for (DisplayMode value : values) {
            if (value == mode) {
                break;
            }
            currentIndex++;
        }

        if (currentIndex <= 0) {
            return values[values.length - 1];
        } else {
            return values[currentIndex - 1];
        }
    }

    public static DisplayMode nextMode(DisplayMode mode) {
        DisplayMode[] values = mode.getClass().getEnumConstants();
        int currentIndex = 0;

        for (DisplayMode value : values) {
            if (value == mode) {
                break;
            }
            currentIndex++;
        }

        currentIndex++;

        if (currentIndex < values.length) {
            return values[currentIndex];
        } else {
            return values[0];
        }
    }

}
