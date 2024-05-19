package dev.boxadactle.coordinatesdisplay.hud;

import dev.boxadactle.boxlib.core.BoxLib;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Locale;

public class CoordinatesHuds {

    public static final HashMap<String, RegisteredRenderer> registeredOverlays = new HashMap<>();

    public static final HashMap<String, RegisteredVisibilityFilter> registeredVisibilityFilters = new HashMap<>();

    public static RegisteredRenderer register(Class<? extends HudRenderer> renderer) {
        RegisteredRenderer overlay = new RegisteredRenderer(renderer);

        if (registeredOverlays.containsKey(overlay.getId())) {
            throw new IllegalStateException("Attempting to register renderer with duplicate id: " + overlay.getId());
        }

        registeredOverlays.put(overlay.getId(), overlay);
        CoordinatesDisplay.LOGGER.info("Registered renderer: " + overlay.getId());
        return overlay;
    }

    public static RegisteredVisibilityFilter registerVisibilityFilter(Class<? extends HudVisibilityFilter> filter) {
        RegisteredVisibilityFilter visibilityFilter = new RegisteredVisibilityFilter(filter);

        if (registeredVisibilityFilters.containsKey(visibilityFilter.getId())) {
            throw new IllegalStateException("Attempting to register visibility filter with duplicate id: " + visibilityFilter.getId());
        }

        registeredVisibilityFilters.put(visibilityFilter.getId(), visibilityFilter);
        CoordinatesDisplay.LOGGER.info("Registered visibility filter: " + visibilityFilter.getId());
        return visibilityFilter;
    }

    public static RegisteredRenderer getRenderer(String id) {
        String idLower = id.toLowerCase(Locale.ROOT);
        RegisteredRenderer r = registeredOverlays.get(idLower);
        if (r != null) return r;
        throw new UnknownRendererException(idLower);
    }

    public static RegisteredVisibilityFilter getVisibilityFilter(String id) {
        String idLower = id.toLowerCase(Locale.ROOT);
        RegisteredVisibilityFilter r = registeredVisibilityFilters.get(idLower);
        if (r != null) return r;
        throw new UnknownVisibilityFilterException(idLower);
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

    public static RegisteredVisibilityFilter nextVisibilityFilter(String value) {
        if (registeredVisibilityFilters.isEmpty()) {
            throw new IllegalStateException("Attempting to get next visibility filter when there are no registered visibility filters!");
        }

        RegisteredVisibilityFilter filter = getVisibilityFilter(value);
        if (filter == null) {
            return registeredVisibilityFilters.values().iterator().next();
        }

        int index = 0;
        for (RegisteredVisibilityFilter r : registeredVisibilityFilters.values()) {
            if (r == filter) {
                break;
            }
            index++;
        }

        if (index == registeredVisibilityFilters.size() - 1) {
            return registeredVisibilityFilters.values().iterator().next();
        } else {
            return registeredVisibilityFilters.values().toArray(new RegisteredVisibilityFilter[0])[index + 1];
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

    public static RegisteredVisibilityFilter previousVisibilityFilter(String value) {
        if (registeredVisibilityFilters.isEmpty()) {
            throw new IllegalStateException("Attempting to get previous visibility filter when there are no registered visibility filters!");
        }

        RegisteredVisibilityFilter filter = getVisibilityFilter(value);
        if (filter == null) {
            return registeredVisibilityFilters.values().iterator().next();
        }

        int index = 0;
        for (RegisteredVisibilityFilter r : registeredVisibilityFilters.values()) {
            if (r == filter) {
                break;
            }
            index++;
        }

        if (index == 0) {
            return registeredVisibilityFilters.values().toArray(new RegisteredVisibilityFilter[0])[registeredVisibilityFilters.size() - 1];
        } else {
            return registeredVisibilityFilters.values().toArray(new RegisteredVisibilityFilter[0])[index - 1];
        }
    }

    public static class RegisteredRenderer {
        HudRenderer renderer;
        DisplayMode metadata;

        public RegisteredRenderer(Class<? extends HudRenderer> renderer) {
            this.renderer = BoxLib.initializeClass(renderer);

            DisplayMode m = renderer.getAnnotation(DisplayMode.class);
            if (m != null) {
                metadata = m;
            } else {
                throw new IllegalStateException("Attempting to register Hud renderer without Hud.Renderer annotation!");
            }
        }

        public HudRenderer getRenderer() {
            return renderer;
        }

        public DisplayMode getMetadata() {
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

    public static class RegisteredVisibilityFilter {
        HudVisibilityFilter filter;
        HudVisibility metadata;

        public RegisteredVisibilityFilter(Class<? extends HudVisibilityFilter> filter) {
            this.filter = BoxLib.initializeClass(filter);

            HudVisibility m = filter.getAnnotation(HudVisibility.class);
            if (m != null) {
                metadata = m;
            } else {
                throw new IllegalStateException("Attempting to register Hud visibility filter without Hud.VisibilityFilter annotation!");
            }
        }

        public HudVisibilityFilter getFilter() {
            return filter;
        }

        public HudVisibility getMetadata() {
            return metadata;
        }

        public Component getComponent() {
            return Component.translatable(filter.getNameKey());
        }

        public String getName() {
            return GuiUtils.getTranslatable(filter.getTranslationKey());
        }

        public String getId() {
            return metadata.value();
        }

    }

}
