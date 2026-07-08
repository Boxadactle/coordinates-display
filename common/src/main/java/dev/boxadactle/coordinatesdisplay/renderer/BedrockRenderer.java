package dev.boxadactle.coordinatesdisplay.renderer;

import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.layouts.component.CenteredParagraphComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.coordinatesdisplay.*;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import oshi.util.tuples.Triplet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@HudDisplayMode(
        value = "bedrock",
        ignoreTranslations = true,
        positionModifier = BedrockRenderer.BedrockPosition.class,
        allowMove = false,
        hasBackground = false,
        hasChunkData = false,
        hasDirectionInt = false,
        hasMCVersion = false,
        hasDimension = false,
        hasBiome = false,
        hasDirection = false,
        hasTime = false
)
public class BedrockRenderer implements HudRenderer {
    @Override
    public RenderingLayout renderOverlay(int x, int y, Position pos) {
        List<Component> components = new ArrayList<>();

        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);

        if (config().renderXYZ) components.add(definition("position",
                value(player.getA()),
                value(player.getB()),
                value(player.getC())
        ));

        if (config().renderDay) components.add(definition(GlobalTexts.DAY, value(Long.toString(pos.world.getDay()))));

        MutableComponent all = Component.empty();

        Iterator<Component> it = components.iterator();
        while (it.hasNext()) {
            all.append(it.next());
            if (it.hasNext()) all.append(Component.literal(" / "));
        }

        ColumnLayout hud = new ColumnLayout(0, 0, 0);
        hud.addComponent(new CenteredParagraphComponent(0, definition(all)));

        return new PaddingLayout(x, y, 4, hud);
    }

    public static class BedrockPosition implements HudPositionModifier.BasicPositionModifier {
        @Override
        public Rect<Integer> getPosition(Rect<Integer> rect, Dimension<Integer> ignored, Hud.RenderType type) {
            return switch (type) {
                case SCREEN -> rect;
                case HUD -> {
                    Rect<Integer> r = rect.clone();

                    r.setX(0);
                    r.setY(60);

                    yield r;
                }
            };
        }
    }
}
