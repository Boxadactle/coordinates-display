package dev.boxadactle.coordinatesdisplay.hud.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.layouts.component.CenteredParagraphComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.math.geometry.Dimension;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.HudPositionModifier;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.hud.HudDisplayMode;
import dev.boxadactle.coordinatesdisplay.hud.Triplet;
import dev.boxadactle.coordinatesdisplay.mixin.OverlayMessageTimeAccessor;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;

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
        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);

        Component xyz = definition("xyz",
                value(player.getA()),
                value(player.getB()),
                value(player.getC())
        );

        Component direction = definition("direction", resolveDirection(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw())));

        Holder<Biome> b = pos.world.getBiome();
        Component biome = ModUtil.getBiomeComponent(b, config().biomeColors, config().dataColor);

        Component all = translation("all", xyz, direction, biome);

        ColumnLayout hud = new ColumnLayout(0, 0, 0);
        hud.addComponent(new CenteredParagraphComponent(0, all));

        return hud;
    }

    public static class HotbarPosition implements HudPositionModifier.BasicPositionModifier {
        @Override
        public Rect<Integer> getPosition(Rect<Integer> rect, Dimension<Integer> ignored) {
            int j = ClientUtils.getClient().getWindow().getGuiScaledWidth() / 2;
            int k = ClientUtils.getClient().getWindow().getGuiScaledHeight() - 68 - 4;

            if (ClientUtils.getClient().level != null) {
                Rect<Integer> r = rect.clone();

                r.setX(j - rect.getWidth() / 2);
                r.setY(k);

                return r;
            } else {
                return rect;
            }
        }
    }
}
