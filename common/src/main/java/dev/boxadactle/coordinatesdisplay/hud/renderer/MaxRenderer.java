package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.layouts.component.ParagraphComponent;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.math.mathutils.NumberFormatter;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.DisplayMode;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

@DisplayMode("maximum")
public class MaxRenderer implements HudRenderer {

    @Override
    public Rect<Integer> renderOverlay(int x, int y, Position pos) {
        NumberFormatter<Double> formatter = genFormatter();

        ParagraphComponent component = new ParagraphComponent(config().textPadding);

        if (config().renderXYZ) {
            Vec3<Double> b = pos.position.getPlayerPos();
            Vec3<Integer> d = pos.position.getBlockPos();
            Vec3<Integer> e = pos.position.getBlockPosInChunk();

            Component xyz = definition(translation("xyz", value(formatter.formatDecimal(b.getX())), value(formatter.formatDecimal(b.getY())), value(formatter.formatDecimal(b.getZ()))));
            Component block = definition(translation("block", value(Integer.toString(d.getX())), value(Integer.toString(d.getY())), value(Integer.toString(d.getZ())), value(Integer.toString(e.getX())), value(Integer.toString(e.getY())), value(Integer.toString(e.getZ()))));
            Component targeted = definition(translation("block.targeted", value(pos.block.getBlockX()), value(pos.block.getBlockY()), value(pos.block.getBlockZ())));

            component.add(xyz);
            component.add(block);
            component.add(targeted);
        }

        if (config().renderChunkData) {
            Vec2<Integer> c = pos.position.getChunkPos();

            Component chunk = definition(translation("chunk", value(Integer.toString(c.getX())), value(Integer.toString(pos.position.getChunkY())), value(Integer.toString(c.getY()))));
            component.add(chunk);
        }

        if (config().renderDirection) {
            Component f = definition("direction_int", value(formatter.formatDecimal(pos.headRot.wrapYaw())), value(formatter.formatDecimal(pos.headRot.wrapPitch())));
            Component g = definition(resolveDirection(ModUtil.getDirectionFromYaw(pos.headRot.wrapYaw())));
            Component direction = definition(translation(
                    "direction", g,
                    config().renderDirectionInt ? f : new TextComponent("")
            ));

            component.add(direction);
        }

        if (config().renderBiome) {
            Component biome = definition(translation("biome", value(pos.world.getBiome(false))));

            component.add(biome);
        }

        if (config().renderMCVersion) {
            Component version = definition(translation("version", value(ClientUtils.getGameVersion())));

            component.add(version);
        }

        if (config().renderDimension) {
            String h = pos.world.getDimension(true);
            String i = pos.world.getDimension(false);
            Component dimension = definition(translation("dimension", value(h), value(ModUtil.getNamespace(i))));

            component.add(dimension);
        }

        RowLayout r = new RowLayout(0, 0, 0);
        r.addComponent(component);
        return renderHud(new PaddingLayout(x, y, config().padding, r));
    }
}
