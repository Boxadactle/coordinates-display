package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.layouts.component.CenteredParagraphComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.Hud;
import dev.boxadactle.coordinatesdisplay.hud.HudPositionModifier;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.hud.HudDisplayMode;
import dev.boxadactle.coordinatesdisplay.mixin.OverlayMessageTimeAccessor;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import oshi.util.tuples.Triplet;

@HudDisplayMode(
        value = "hotbar",
        ignoreTranslations = true,
        positionModifier = HotbarRenderer.HotbarPosition.class,
        allowMove = false,
        hasBackground = false,
        hasXYZ = false,
        hasChunkData = false,
        hasDirection = false,
        hasDirectionInt = false,
        hasBiome = false,
        hasMCVersion = false,
        hasDimension = false
)
public class HotbarRenderer implements HudRenderer {

    @Override
    public RenderingLayout renderOverlay(int x, int y, Position pos) {
        if (((OverlayMessageTimeAccessor) ClientUtils.getClient().gui).getOverlayMessageTime() > 0) {
            return new ColumnLayout(0, 0, 0);
        }

        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);

        Component xyz = definition(GlobalTexts.XYZ,
                value(player.getA()),
                value(player.getB()),
                value(player.getC())
        );

        Component direction = definition(GlobalTexts.FACING, value(resolveDirection(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw()))));

        ResourceLocation bKey = pos.world.getBiomeKey();
        Biome b = pos.world.getBiome();
        Component biome = ModUtil.getBiomeComponent(bKey, b, config().biomeColors, config().dataColor);

        Component all = translation("all", xyz, direction, biome);

        ColumnLayout hud = new ColumnLayout(x, y, 0);
        hud.addComponent(new CenteredParagraphComponent(0, all));

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
