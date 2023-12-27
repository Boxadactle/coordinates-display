package dev.boxadactle.coordinatesdisplay.hud;

import dev.boxadactle.boxlib.core.BoxLib;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Locale;

public class CoordinatesHuds {

    public static final HashMap<String, RegisteredRenderer> registeredOverlays = new HashMap<>();

    public static RegisteredRenderer register(Class<? extends HudRenderer> renderer) {
        RegisteredRenderer overlay = new RegisteredRenderer(renderer);
        registeredOverlays.put(overlay.getId(), overlay);
        CoordinatesDisplay.LOGGER.info("Registered renderer: " + overlay.getId());
        return overlay;
    }

    public static RegisteredRenderer getRenderer(String id) {
        String idLower = id.toLowerCase(Locale.ROOT);
        RegisteredRenderer r = registeredOverlays.get(idLower);
        if (r != null) return r;
        throw new UnknownRendererException(idLower);
    }

    public static RegisteredRenderer nextRenderer(String value) {
        if (registeredOverlays.isEmpty()) {
            throw new IllegalStateException("Attempting to get next renderer when there are no registered renderers!");
        }

        RegisteredRenderer renderer = getRenderer(value);
        if (renderer == null) {
            return registeredOverlays.values().iterator().next();
        }

        int index = 0;
        for (RegisteredRenderer r : registeredOverlays.values()) {
            if (r == renderer) {
                break;
            }
            index++;
        }

        if (index == registeredOverlays.size() - 1) {
            return registeredOverlays.values().iterator().next();
        } else {
            return registeredOverlays.values().toArray(new RegisteredRenderer[0])[index + 1];
        }
    }

    public static RegisteredRenderer previousRenderer(String value) {
        if (registeredOverlays.isEmpty()) {
            throw new IllegalStateException("Attempting to get previous renderer when there are no registered renderers!");
        }

        RegisteredRenderer renderer = getRenderer(value);
        if (renderer == null) {
            return registeredOverlays.values().iterator().next();
        }

        int index = 0;
        for (RegisteredRenderer r : registeredOverlays.values()) {
            if (r == renderer) {
                break;
            }
            index++;
        }

        if (index == 0) {
            return registeredOverlays.values().toArray(new RegisteredRenderer[0])[registeredOverlays.size() - 1];
        } else {
            return registeredOverlays.values().toArray(new RegisteredRenderer[0])[index - 1];
        }
    }

    public static class RegisteredRenderer {
        HudRenderer renderer;
        RendererMetadata metadata;

        public RegisteredRenderer(Class<? extends HudRenderer> renderer) {
            this.renderer = BoxLib.initializeClass(renderer);

            RendererMetadata m = renderer.getAnnotation(RendererMetadata.class);
            if (m != null) {
                metadata = m;
            } else {
                throw new IllegalStateException("Attempting to register Hud renderer without Hud.Renderer annotation!");
            }
        }

        public HudRenderer getRenderer() {
            return renderer;
        }

        public RendererMetadata getMetadata() {
            return metadata;
        }

        public Component getComponent() {
            return Component.translatable(renderer.getNameKey());
        }

        public String getName() {
            return GuiUtils.getTranslatable(renderer.getTranslationKey());
        }

        public String getId() {
            return metadata.value();
        }
    }

}
