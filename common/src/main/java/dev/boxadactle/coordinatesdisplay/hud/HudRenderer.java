package dev.boxadactle.coordinatesdisplay.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.math.mathutils.NumberFormatter;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModConfig;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.network.chat.Component;

public interface HudRenderer {

    // HUD HELPERS

    default ModConfig config() {
        return CoordinatesDisplay.getConfig();
    }

    default void drawInfo(PoseStack poseStack, Component component, int x, int y, int color) {
        RenderUtils.drawText(poseStack, component, x, y, color);
    }

    default void drawInfo(PoseStack stack, Component component, int x, int y) {
        drawInfo(stack, component, x, y, GuiUtils.WHITE);
    }

    default void updateHudSize(Rect<Integer> newSize) {
        CoordinatesDisplay.HUD.size = newSize.clone();
    }



    // text helpers

    default String getTranslationKey() {
        return getNameKey() + ".";
    }

    default String getNameKey() {
        HudDisplayMode metadata = this.getClass().getAnnotation(HudDisplayMode.class);
        if (metadata != null) {
            if (!metadata.translationKey().isEmpty()) {
                return metadata.translationKey();
            } else {
                return "hud.coordinatesdisplay." + metadata.value();
            }
        } else {
            throw new RuntimeException("Cannot use hud text helpers without specifying a translation key!");
        }
    }

    default Component translation(String t, Object ...args) {
        return Component.translatable(getTranslationKey() + t, args);
    }

    default Component definition(Component t) {
        return GuiUtils.colorize(t, CoordinatesDisplay.getConfig().definitionColor);
    }

    default Component definition(String t) {
        return GuiUtils.colorize(Component.literal(t), CoordinatesDisplay.getConfig().definitionColor);
    }

    default Component definition(String k, Object ...args) {
        return definition(translation(k, args));
    }

    default Component value(String t) {
        return GuiUtils.colorize(Component.literal(t), CoordinatesDisplay.getConfig().dataColor);
    }

    default Component value(Component t) {
        return GuiUtils.colorize(t, CoordinatesDisplay.getConfig().dataColor);
    }

    default Component resolveDirection(String direction, boolean useShort) {
        String key = "hud.coordinatesdisplay." + direction;
        if (useShort) {
            key += ".short";
        }
        return Component.translatable(key);
    }

    default Component resolveDirection(String direction) {
        return resolveDirection(direction, false);
    }

    static Rect<Integer> renderHud(PoseStack stack, RenderingLayout hudRenderer, boolean background) {
        Rect<Integer> r = hudRenderer.calculateRect();

        if (CoordinatesDisplay.getConfig().renderBackground && background) {
            RenderUtils.drawSquare(stack, r, CoordinatesDisplay.getConfig().backgroundColor);
        }

        hudRenderer.render(stack);

        return r;
    }


    // POSITION HELPER

    default Triplet<String, String, String> roundPosition(Vec3<Double> pos, Vec3<Integer> blockPos, int decimalPlaces) {
        if (decimalPlaces == 0) {
            return new Triplet<>(
                    Integer.toString(blockPos.getX()),
                    Integer.toString(blockPos.getY()),
                    Integer.toString(blockPos.getZ())
            );
        } else {
            var n = new NumberFormatter<Double>(decimalPlaces);
            return new Triplet<>(
                    n.formatDecimal(pos.getX()),
                    n.formatDecimal(pos.getY()),
                    n.formatDecimal(pos.getZ())
            );
        }
    }

    default Component createLine(String defKey, String value) {
        return definition(
                defKey,
                value(value)
        );
    }

    default Component createLine(String defKey, Component value) {
        return definition(
                defKey,
                value
        );
    }

    default Triplet<Component, Component, Component> createXYZ(String x, String y, String z) {
        return new Triplet<>(
                createLine("x", x),
                createLine("y", y),
                createLine("z", z)
        );
    }

    default Triplet<Component, Component, Component> createXYZ(int x, int y, int z) {
        return createXYZ(Integer.toString(x), Integer.toString(y), Integer.toString(z));
    }

    default NumberFormatter<Double> genFormatter() {
        return new NumberFormatter<>(CoordinatesDisplay.getConfig().decimalPlaces);
    }


    // HUD RENDERER METHOD

    RenderingLayout renderOverlay(int x, int y, Position pos);

}
