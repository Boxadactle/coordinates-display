package dev.boxadactle.coordinatesdisplay.util.hud;

import dev.boxadactle.boxlib.math.Rect;
import dev.boxadactle.boxlib.math.Vec2;
import dev.boxadactle.boxlib.math.Vec3;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.util.HudRenderer;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.apache.commons.compress.utils.Lists;

import java.text.DecimalFormat;
import java.util.List;

public class MaxRenderer extends HudRenderer.Renderer {

    public MaxRenderer() {
        super("hud.coordinatesdisplay.max.");
    }

    @Override
    protected Rect<Integer> renderOverlay(DrawContext drawContext, int x, int y, Position pos) {
        DecimalFormat a = new DecimalFormat(config().decimalRounding ? "0.00" : "0");

        List<Text> toRender = Lists.newArrayList();

        Vec3<Double> b = pos.position.getPlayerPos();
        Vec2<Integer> c = pos.position.getChunkPos();
        Vec3<Integer> d = pos.position.getBlockPos();
        Vec3<Integer> e = pos.position.getBlockPosInChunk();
        Text xyz = definition(translation("xyz", value(a.format(b.getX())), value(a.format(b.getY())), value(a.format(b.getZ()))));
        Text block = definition(translation("block", value(Integer.toString(d.getX())), value(Integer.toString(d.getY())), value(Integer.toString(d.getZ())), value(Integer.toString(e.getX())), value(Integer.toString(e.getY())), value(Integer.toString(e.getZ()))));
        Text targeted = definition(translation("block.targeted", value(pos.block.getBlockX()), value(pos.block.getBlockY()), value(pos.block.getBlockZ())));
        Text chunk = definition(translation("chunk", value(Integer.toString(c.getX())), value(Integer.toString(pos.position.getChunkY())), value(Integer.toString(c.getY()))));

        Text g = definition(translation(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw())));
        Text direction = definition(translation("direction", g, value(a.format(pos.headRot.wrapYaw())), value(a.format(pos.headRot.wrapPitch()))));

        Text biome = definition(translation("biome", value(pos.world.getBiome(false))));

        Text version = definition(translation("version", value(ClientUtils.getGameVersion())));

        String h = pos.world.getDimension(true);
        String i = pos.world.getDimension(false);
        Text dimension = definition(translation("dimension", value(h), value(ModUtil.getNamespace(i))));


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
            RenderUtils.drawSquare(drawContext, x, y, width, height, config().backgroundColor);
        }

        for (int j = 0; j < toRender.size(); j++) {
            drawInfo(drawContext, toRender.get(j), x + config().padding, y + config().padding + (GuiUtils.getTextRenderer().fontHeight + 2) * j, GuiUtils.WHITE);
        }

        return new Rect<>(x, y, width, height);
    }

    private int calculateWidth(List<Text> texts) {
        return config().padding * 2 + GuiUtils.getLongestLength(texts.toArray(new Text[0]));
    }

    private int calculateHeight(List<Text> texts) {
        return config().padding * 2 + 11 * texts.size();
    }

}
