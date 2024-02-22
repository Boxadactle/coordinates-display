package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.layouts.component.LayoutContainerComponent;
import dev.boxadactle.boxlib.layouts.component.TextComponent;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.hud.DisplayMode;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import oshi.util.tuples.Triplet;

@DisplayMode(
        value = "line",
        hasChunkData = false,
        hasDirectionInt = false,
        hasBiome = false,
        hasMCVersion = false,
        hasDimension = false
)
public class LineRenderer implements HudRenderer {

    @Override
    public Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos) {
        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);

        RowLayout layout = new RowLayout(0, 0, config().textPadding);

        if (config().renderXYZ) {
            Component xtext = definition("x", value(player.getA()));
            Component ytext = definition("y", value(player.getB()));
            Component ztext = definition("z", value(player.getC()));

            RowLayout xyz = new RowLayout(0, 0, 3);
            xyz.addComponent(new TextComponent(xtext));
            xyz.addComponent(new TextComponent(ytext));
            xyz.addComponent(new TextComponent(ztext));

            layout.addComponent(new LayoutContainerComponent(xyz));
        }

        if (config().renderDirection) {
            Component direction = definition("direction", value(resolveDirection(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw()))));

            layout.addComponent(new TextComponent(direction));
        }

        int p = config().renderBackground ? config().padding : 0;
        PaddingLayout hud = new PaddingLayout(x, y, p, layout);

        return renderHud(guiGraphics, hud);
    }

    private Component next(Component t1, Component t2) {
        return t1.copy().append(t2);
    }

    private Component addTrailingSpace(Component input) {
        return input.copy().append(" ");
    }
}
