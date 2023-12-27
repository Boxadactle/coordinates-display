package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.math.mathutils.NumberFormatter;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.RendererMetadata;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@RendererMetadata("maximum")
public class MaxRenderer implements HudRenderer {

    @Override
    public Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos) {
        NumberFormatter<Double> formatter = genFormatter();

        List<Component> toRender = Lists.newArrayList();

        Vec3<Double> b = pos.position.getPlayerPos();
        Vec2<Integer> c = pos.position.getChunkPos();
        Vec3<Integer> d = pos.position.getBlockPos();
        Vec3<Integer> e = pos.position.getBlockPosInChunk();
        Component xyz = definition(translation("xyz", value(formatter.formatDecimal(b.getX())), value(formatter.formatDecimal(b.getY())), value(formatter.formatDecimal(b.getZ()))));
        Component block = definition(translation("block", value(Integer.toString(d.getX())), value(Integer.toString(d.getY())), value(Integer.toString(d.getZ())), value(Integer.toString(e.getX())), value(Integer.toString(e.getY())), value(Integer.toString(e.getZ()))));
        Component targeted = definition(translation("block.targeted", value(pos.block.getBlockX()), value(pos.block.getBlockY()), value(pos.block.getBlockZ())));
        Component chunk = definition(translation("chunk", value(Integer.toString(c.getX())), value(Integer.toString(pos.position.getChunkY())), value(Integer.toString(c.getY()))));

        Component g = definition(resolveDirection(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw())));
        Component direction = definition(translation("direction", g, value(formatter.formatDecimal(pos.headRot.wrapYaw())), value(formatter.formatDecimal(pos.headRot.wrapPitch()))));

        Component biome = definition(translation("biome", value(pos.world.getBiome(false))));

        Component version = definition(translation("version", value(ClientUtils.getGameVersion())));

        String h = pos.world.getDimension(true);
        String i = pos.world.getDimension(false);
        Component dimension = definition(translation("dimension", value(h), value(ModUtil.getNamespace(i))));


        toRender.add(xyz);
        toRender.add(block);
        toRender.add(targeted);
        if (config().renderChunkData) toRender.add(chunk);

        if (config().renderDirection) toRender.add(direction);
        if (config().renderBiome) toRender.add(biome);
        if (config().renderMCVersion) toRender.add(version);
        toRender.add(dimension);

        int width = calculateWidth(toRender);
        int height = calculateHeight(toRender);

        if (config().renderBackground) {
            RenderUtils.drawSquare(guiGraphics, x, y, width, height, config().backgroundColor);
        }

        for (int j = 0; j < toRender.size(); j++) {
            drawInfo(guiGraphics, toRender.get(j), x + config().padding, y + config().padding + 11 * j, GuiUtils.WHITE);
        }

        return new Rect<>(x, y, width, height);
    }

    private int calculateWidth(List<Component> texts) {
        return config().padding * 2 + GuiUtils.getLongestLength(texts.toArray(new Component[0]));
    }

    private int calculateHeight(List<Component> texts) {
        return config().padding * 2 + 11 * texts.size();
    }
}
