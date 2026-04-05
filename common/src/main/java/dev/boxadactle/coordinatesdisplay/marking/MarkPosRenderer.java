package dev.boxadactle.coordinatesdisplay.marking;

import dev.boxadactle.boxlib.math.geometry.Box;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.rendering.Renderer3D;
import dev.boxadactle.boxlib.rendering.renderers.BoxRenderer;
import dev.boxadactle.boxlib.rendering.renderers.TextRenderer;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModConfig;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.network.chat.Component;
import net.minecraft.util.debug.DebugValueAccess;

public class MarkPosRenderer extends Renderer3D<MarkPosRenderer> {
    public MarkPosRenderer() {
        super(false);
    }

    @Override
    public void render(double v, double v1, double v2, DebugValueAccess debugValueAccess, Frustum frustum, float v3) {
        if (CoordinatesDisplay.MARK_POS != null) {
            Vec3<Double> markPos = new Vec3<>(
                    CoordinatesDisplay.MARK_POS.x.doubleValue(),
                    CoordinatesDisplay.MARK_POS.y.doubleValue(),
                    CoordinatesDisplay.MARK_POS.z.doubleValue()
            );

            ModConfig config = CoordinatesDisplay.getConfig();

            if (config.renderMarkBeacon) {
                BoxRenderer beacon = new BoxRenderer(false)
                        .setCube(new Box<>(
                                markPos.x + 0.25,
                                -100.0,
                                markPos.z + 0.25,
                                0.5,
                                500.0,
                                0.5
                        ))
                        .setColor(GuiUtils.applyAlpha(config.markBeaconColor, 0.5f))

                        .setXRay(true);
                beacon.render(v, v1, v2, debugValueAccess, frustum, v3);
            }

            if (config.renderMarkOutline) {
                BoxRenderer block = new BoxRenderer(false)
                        .setCube(ModUtil.toBlockPos(CoordinatesDisplay.MARK_POS))
                        .setColor(GuiUtils.applyAlpha(config.markOutlineColor, 0.8f))
                        .setOutline(true)
                        .setOutlineWidth(4.0f)
                        .setXRay(true);
                block.render(v, v1, v2, debugValueAccess, frustum, v3);
            }

            if (config.renderMarkText) {
                TextRenderer p = new TextRenderer(false)
                        .setPos(new Vec3<>(
                                markPos.x + 0.5,
                                markPos.y + 0.5,
                                markPos.z + 0.5
                        ))
                        .setCentered(true)
                        .setXRay(true)
                        .setText(Component.literal(CoordinatesDisplay.getConfig().markPosText))
                        .setColor(config.markTextColor)
                        .setSize(ModUtil.calculatePointDistance3d(
                                markPos,
                                new Vec3<>(v, v1, v2)
                        ) * 0.02f);
                p.render(v, v1, v2, debugValueAccess, frustum, v3);
            }
        }
    }
}
