package dev.boxadactle.coordinatesdisplay;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.rendering.Renderer3D;
import dev.boxadactle.boxlib.rendering.renderers.TextRenderer;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.network.chat.Component;
import net.minecraft.util.debug.DebugValueAccess;

public class CompassRenderer3D extends Renderer3D<CompassRenderer3D> {
    public CompassRenderer3D() {
        super(false);
    }

    @Override
    public void render(double v, double v1, double v2, DebugValueAccess debugValueAccess, Frustum frustum, float v3) {
        if (WorldUtils.getCamera() != null && CoordinatesDisplay.getConfig().render3dCompass) {
            float size = 0.15f * CoordinatesDisplay.getConfig().compassScale;

            Camera camera = ClientUtils.getClient().gameRenderer.getMainCamera();
            net.minecraft.world.phys.Vec3 cameraPos = camera.entity().getPosition(v3);

            TextRenderer north = new TextRenderer(false)
                    .setPos(new Vec3<>(cameraPos.x, cameraPos.y + 1.0, cameraPos.z - 10.0))
                    .setText(Component.literal("N"))
                    .setSize(size)
                    .setColor(GuiUtils.RED)
                    .setCentered(true)
                    .setXRay(true);
            north.render(v, v1, v2, debugValueAccess, frustum, v3);

            TextRenderer east = new TextRenderer(false)
                    .setPos(new Vec3<>(cameraPos.x + 10.0, cameraPos.y + 1.0, cameraPos.z))
                    .setText(Component.literal("E"))
                    .setSize(size)
                    .setColor(GuiUtils.GREEN)
                    .setCentered(true)
                    .setXRay(true);
            east.render(v, v1, v2, debugValueAccess, frustum, v3);

            TextRenderer south = new TextRenderer(false)
                    .setPos(new Vec3<>(cameraPos.x, cameraPos.y + 1.0, cameraPos.z + 10.0))
                    .setText(Component.literal("S"))
                    .setSize(size)
                    .setColor(GuiUtils.YELLOW)
                    .setCentered(true)
                    .setXRay(true);
            south.render(v, v1, v2, debugValueAccess, frustum, v3);

            TextRenderer west = new TextRenderer(false)
                    .setPos(new Vec3<>(cameraPos.x - 10.0, cameraPos.y + 1.0, cameraPos.z))
                    .setText(Component.literal("W"))
                    .setSize(size)
                    .setColor(GuiUtils.WHITE)
                    .setCentered(true)
                    .setXRay(true);
            west.render(v, v1, v2, debugValueAccess, frustum, v3);
        }
    }
}
