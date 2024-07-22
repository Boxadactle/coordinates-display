package dev.boxadactle.coordinatesdisplay.hud;

public interface HudVisibilityFilter {

    boolean isVisible();

    default String getNameKey() {
        HudVisibility metadata = this.getClass().getAnnotation(HudVisibility.class);
        if (metadata != null) {
            if (!metadata.translationKey().isEmpty()) {
                return metadata.translationKey();
            } else {
                return "hud.coordinatesdisplay.filter." + metadata.value();
            }
        } else {
            throw new RuntimeException("Cannot use hud text helpers without specifying a translation key!");
        }
    }

    default String getTranslationKey() {
        return getNameKey() + ".";
    }

}
