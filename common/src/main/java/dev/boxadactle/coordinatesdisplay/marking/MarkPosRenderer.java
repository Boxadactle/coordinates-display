package dev.boxadactle.coordinatesdisplay.marking;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.math.geometry.Box;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.rendering.Renderer3D;
import dev.boxadactle.boxlib.rendering.renderers.BoxRenderer;
import dev.boxadactle.boxlib.rendering.renderers.TextRenderer;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

public class MarkPosRenderer extends Renderer3D<MarkPosRenderer> {
    public MarkPosRenderer() {
        super(false);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, double v, double v1, double v2) {
        if (CoordinatesDisplay.MARK_POS != null) {
            Vec3<Double> markPos = new Vec3<>(
                    CoordinatesDisplay.MARK_POS.x.doubleValue(),
                    CoordinatesDisplay.MARK_POS.y.doubleValue(),
                    CoordinatesDisplay.MARK_POS.z.doubleValue()
            );

            BoxRenderer beacon = new BoxRenderer(false)
                    .setCube(new Box<>(
                            markPos.x + 0.25,
                            -100.0,
                            markPos.z + 0.25,
                            0.5,
                            500.0,
                            0.5
                    ))
                    .setColor(GuiUtils.RED)
                    .setAlpha(0.5f);
            beacon.render(poseStack, bufferSource, v, v1, v2);

            BoxRenderer block = new BoxRenderer(false)
                    .setCube(ModUtil.toBlockPos(CoordinatesDisplay.MARK_POS))
                    .setColor(GuiUtils.GRAY)
                    .setAlpha(0.8f);
            block.render(poseStack, bufferSource, v, v1, v2);

            TextRenderer p = new TextRenderer(false)
                    .setPos(new Vec3<>(
                            markPos.x + 0.5,
                            markPos.y + 0.5,
                            markPos.z + 0.5
                    ))
                    .setCentered(true)
                    .setXray(true)
                    .setShadow(false)
                    .setText(Component.literal(CoordinatesDisplay.getConfig().markPosText))
                    .setColor(GuiUtils.WHITE)
                    .setSize(ModUtil.calculatePointDistance3d(
                            markPos,
                            new Vec3<>(v, v1, v2)
                    ) * 0.01f);
            p.render(poseStack, bufferSource, v, v1, v2);
        }
    }
}
