package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.mathutils.NumberFormatter;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.mixin.OverlayMessageTimeAccessor;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class HotbarRenderer extends HudRenderer {
    public HotbarRenderer() {
        super("hud.coordinatesdisplay.hotbar.");
    }

    @Override
    public boolean ignoreTranslations() {
        return true;
    }

    @Override
    protected Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos) {
        NumberFormatter<Double> formatter = new NumberFormatter<>(config().decimalPlaces);

        Component xyz = definition("xyz",
                value(formatter.formatDecimal(pos.position.getPlayerPos().getX())),
                value(formatter.formatDecimal(pos.position.getPlayerPos().getY())),
                value(formatter.formatDecimal(pos.position.getPlayerPos().getZ()))
        );

        Component direction = definition("direction", translation(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw())));

        String biomestring = pos.world.getBiome(true);
        Component biome = GuiUtils.colorize(
                Component.literal(biomestring),
                CoordinatesDisplay.CONFIG.get().biomeColors ?
                        CoordinatesDisplay.BiomeColors.getBiomeColor(biomestring, CoordinatesDisplay.CONFIG.get().dataColor) :
                        CoordinatesDisplay.CONFIG.get().dataColor
        );

        Component all = translation("all", xyz, direction, biome);
        int i = GuiUtils.getTextSize(all);

        Rect<Integer> r;

        if (ClientUtils.getClient().level != null && ClientUtils.getCurrentScreen() == null) {
            int j = guiGraphics.guiWidth() / 2;
            int k = guiGraphics.guiHeight() - 68 - 4;

            // make sure we don't render over any actionbar titles
            if (((OverlayMessageTimeAccessor)ClientUtils.getClient().gui).getOverlayMessageTime() == 0)
                drawInfo(guiGraphics, all, j - i / 2, k, GuiUtils.WHITE);

            r = new Rect<>(j - i / 2, k, i, 9);
        } else {
            drawInfo(guiGraphics, all, x, y, GuiUtils.WHITE);
            r = new Rect<>(x, y, i, 9);
        }

        return r;
    }
}
