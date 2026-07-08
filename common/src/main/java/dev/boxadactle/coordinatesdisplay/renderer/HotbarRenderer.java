package dev.boxadactle.coordinatesdisplay.renderer;

import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.layouts.component.CenteredParagraphComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.coordinatesdisplay.*;
import dev.boxadactle.coordinatesdisplay.mixin.OverlayMessageTimeAccessor;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import oshi.util.tuples.Triplet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@HudDisplayMode(
        value = "hotbar",
        ignoreTranslations = true,
        positionModifier = HotbarRenderer.HotbarPosition.class,
        allowMove = false,
        hasBackground = false,
        hasChunkData = false,
        hasDirectionInt = false,
        hasMCVersion = false,
        hasDimension = false
)
public class HotbarRenderer implements HudRenderer {

    @Override
    public RenderingLayout renderOverlay(int x, int y, Position pos) {
        if (((OverlayMessageTimeAccessor) ClientUtils.getClient().gui).getOverlayMessageTime() > 0) {
            return new ColumnLayout(0, 0, 0);
        }

        List<Component> components = new ArrayList<>();

        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);

        if (config().renderXYZ) components.add(definition(GlobalTexts.XYZ,
                value(player.getA()),
                value(player.getB()),
                value(player.getC())
        ));

        if (config().renderDirection) components.add(definition(GlobalTexts.FACING, value(resolveDirection(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw())))));

        if (config().renderBiome) {
            Identifier bKey = pos.world.getBiomeKey();
            Biome b = pos.world.getBiome();
            components.add(ModUtil.getBiomeComponent(bKey, b, config().biomeColors, config().dataColor));
        }

        if (config().renderDay) {
            components.add(definition(GlobalTexts.DAY, value(Long.toString(pos.world.getDay()))));
        }

        if (config().renderTime) {
            String formatted = config().militaryTime ? DateUtil.formatMinecraftDayTime24(pos.world.getTime()) : DateUtil.formatMinecraftDayTime(pos.world.getTime());
            Component time = definition(GlobalTexts.TIME, value(formatted));

            components.add(time);
        }

        MutableComponent all = Component.empty();

        Iterator<Component> it = components.iterator();
        while (it.hasNext()) {
            all.append(it.next());
            if (it.hasNext()) all.append(Component.literal(" / "));
        }

        ColumnLayout hud = new ColumnLayout(x, y, 0);
        hud.addComponent(new CenteredParagraphComponent(0, definition(all)));

        return hud;
    }

    public static class HotbarPosition implements HudPositionModifier.BasicPositionModifier {
        @Override
        public Rect<Integer> getPosition(Rect<Integer> rect, Dimension<Integer> ignored, Hud.RenderType type) {
            return switch (type) {
                case SCREEN -> rect;
                case HUD -> {
                    int j = ClientUtils.getClient().getWindow().getGuiScaledWidth() / 2;
                    int k = ClientUtils.getClient().getWindow().getGuiScaledHeight() - 68 - 4;

                    Rect<Integer> r = rect.clone();

                    r.setX(j - rect.getWidth() / 2);
                    r.setY(k);

                    yield r;
                }
            };
        }
    }
}
