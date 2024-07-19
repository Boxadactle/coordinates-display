package dev.boxadactle.coordinatesdisplay.hud.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.hud.DisplayMode;
import dev.boxadactle.coordinatesdisplay.hud.Triplet;
import dev.boxadactle.coordinatesdisplay.mixin.OverlayMessageTimeAccessor;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.biome.Biome;

@DisplayMode(
        value = "hotbar",
        ignoreTranslations = true,
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
    public Rect<Integer> renderOverlay(PoseStack stack, int x, int y, Position pos) {
        Triplet<String, String, String> player = this.roundPosition(pos.position.getPlayerPos(), pos.position.getBlockPos(), CoordinatesDisplay.getConfig().decimalPlaces);

        Component xyz = definition("xyz",
                value(player.getA()),
                value(player.getB()),
                value(player.getC())
        );

        Component direction = definition("direction", resolveDirection(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw())));

        Biome b = pos.world.getBiome();
        Component biome = ModUtil.getBiomeComponent(b, config().biomeColors, config().dataColor);

        Component all = translation("all", xyz, direction, biome);
        int i = GuiUtils.getTextSize(all);

        Rect<Integer> r;

        if (ClientUtils.getClient().level != null) {
            int j = ClientUtils.getClient().getWindow().getGuiScaledWidth() / 2;
            int k = ClientUtils.getClient().getWindow().getGuiScaledHeight() - 68 - 4;

            // make sure we don't render over any actionbar titles
            if (((OverlayMessageTimeAccessor)ClientUtils.getClient().gui).getOverlayMessageTime() == 0)
                drawInfo(stack, all, j - i / 2, k, GuiUtils.WHITE);

            r = new Rect<>(j - i / 2, k, i, 9);
        } else {
            drawInfo(stack, all, x, y, GuiUtils.WHITE);
            r = new Rect<>(x, y, i, 9);
        }

        return r;
    }
}
