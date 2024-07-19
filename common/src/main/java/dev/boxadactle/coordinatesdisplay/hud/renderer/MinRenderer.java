package dev.boxadactle.coordinatesdisplay.hud.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.layouts.component.LayoutContainerComponent;
import dev.boxadactle.boxlib.layouts.component.LeftParagraphComponent;
import dev.boxadactle.boxlib.layouts.component.ParagraphComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.DisplayMode;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.hud.Triplet;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

@DisplayMode(
        value = "minimum",
        hasXYZ = false,
        hasChunkData = false,
        hasDirectionInt = false,
        hasMCVersion = false,
        hasDimension = false
)
public class MinRenderer implements HudRenderer {

    private Component[] createDirectionComponents(double yaw) {
        // compiled using the debug screen
        String[][] directions = {
                // X   Z
                { " ", "+" },
                { "-", "+" },
                { "-", " " },
                { "-", "-" },
                { " ", "-" },
                { "+", "-" },
                { "+", " " },
                { "+", "+" }
        };

        String[] direction = directions[(int) Math.round(yaw / 45.0F) & 7];

        return new Component[] {
                new TextComponent(direction[0]),
                resolveDirection(ModUtil.getDirectionFromYaw(yaw), true),
                new TextComponent(direction[1])
        };
    }

    @Override
    public Rect<Integer> renderOverlay(PoseStack stack, int x, int y, Position pos) {
        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);

        RowLayout layout = new RowLayout(0, 0, config().textPadding);

        ColumnLayout row = new ColumnLayout(0, 0, config().textPadding / 2);

        { // xyz
            Component xtext = createLine("x", player.getA());
            Component ytext = createLine("y", player.getB());
            Component ztext = createLine("z", player.getC());

            ParagraphComponent paragraph = new ParagraphComponent(1, xtext, ytext, ztext);
            row.addComponent(paragraph);
        }

        // biome
        if (config().renderBiome) {
            Component biome = definition(
                    "biome",
                    ModUtil.getBiomeComponent(pos.world.getBiome(), config().biomeColors, config().dataColor)
            );

            row.addComponent(new dev.boxadactle.boxlib.layouts.component.TextComponent(biome));
        }

        layout.addComponent(new LayoutContainerComponent(row));

        // direction
        if (config().renderDirection) {
            Component[] directionTexts = createDirectionComponents(pos.headRot.wrapYaw());
            Component xDirection = definition(directionTexts[0]);
            Component directionText = value(directionTexts[1]);
            Component zDirection = definition(directionTexts[2]);

            layout.addComponent(new LeftParagraphComponent(1, xDirection, directionText, zDirection));
        }

        return renderHud(stack, new PaddingLayout(x, y, config().padding, layout));
    }
}
