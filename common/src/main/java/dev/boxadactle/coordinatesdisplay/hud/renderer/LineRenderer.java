package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.layouts.component.LayoutContainerComponent;
import dev.boxadactle.boxlib.layouts.component.TextComponent;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.hud.HudDisplayMode;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.network.chat.Component;
import oshi.util.tuples.Triplet;

@HudDisplayMode(
        value = "line",
        hasChunkData = false,
        hasDirectionInt = false,
        hasBiome = false,
        hasMCVersion = false,
        hasDimension = false
)
public class LineRenderer implements HudRenderer {

    @Override
    public RenderingLayout renderOverlay(int x, int y, Position pos) {
        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);

        RowLayout layout = new RowLayout(0, 0, config().textPadding);

        if (config().renderXYZ) {
            Component xtext = definition(GlobalTexts.X, value(player.getA()));
            Component ytext = definition(GlobalTexts.Y, value(player.getB()));
            Component ztext = definition(GlobalTexts.Z, value(player.getC()));

            RowLayout xyz = new RowLayout(0, 0, 3);
            xyz.addComponent(new TextComponent(xtext));
            xyz.addComponent(new TextComponent(ytext));
            xyz.addComponent(new TextComponent(ztext));

            layout.addComponent(new LayoutContainerComponent(xyz));
        }

        if (config().renderDirection) {
            Component direction = definition(GlobalTexts.FACING, value(resolveDirection(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw()))));

            layout.addComponent(new TextComponent(direction));
        }

        int p = config().renderBackground ? config().padding : 0;

        return new PaddingLayout(x, y, p, layout);
    }
}
