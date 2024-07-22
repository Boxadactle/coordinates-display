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

    default Component definition(GlobalTexts t, Object ...args) {
        return definition(t.get(args));
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

    default Component value(GlobalTexts t, Object ...args) {
        return value(t.get(args));
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

    default Triplet<Component, Component, Component> createXYZ(String x, String y, String z) {
        return new Triplet<>(
                definition(GlobalTexts.X, value(x)),
                definition(GlobalTexts.Y, value(y)),
                definition(GlobalTexts.Z, value(z))
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

    enum GlobalTexts {
        X("hud.coordinatesdisplay.x"),
        Y("hud.coordinatesdisplay.y"),
        Z("hud.coordinatesdisplay.z"),
        XYZ("hud.coordinatesdisplay.xyz"),
        CHUNK_X("hud.coordinatesdisplay.chunk_x"),
        CHUNK_Y("hud.coordinatesdisplay.chunk_y"),
        CHUNK_Z("hud.coordinatesdisplay.chunk_z"),
        FACING("hud.corodinatesdisplay.facing"),
        BIOME("hud.coordinatesdisplay.biome"),
        DIMENSION("hud.coordinatesdisplay.dimension");

        final String key;

        GlobalTexts(String key) {
            this.key = key;
        }

        public Component get(Object ...args) {
            return Component.translatable(key, args);
        }
    }

}
