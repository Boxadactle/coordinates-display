package dev.boxadactle.coordinatesdisplay.hud.renderer;

import dev.boxadactle.boxlib.layouts.component.LayoutContainerComponent;
import dev.boxadactle.boxlib.layouts.component.ParagraphComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.math.geometry.Rect;
import dev.boxadactle.boxlib.math.geometry.Vec2;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.hud.DisplayMode;
import dev.boxadactle.coordinatesdisplay.hud.HudRenderer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ChunkPos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@DisplayMode(
        value = "chunk",
        hasXYZ = false,
        hasBiome = false,
        hasDimension = false,
        hasChunkData = false,
        hasDirection = false,
        hasDirectionInt = false,
        hasMCVersion = false
)
public class ChunkRenderer implements HudRenderer {

    @Override
    public Rect<Integer> renderOverlay(GuiGraphics guiGraphics, int x, int y, Position pos) {
        RowLayout hud = new RowLayout(0, 0, config().textPadding);

        {
            ColumnLayout left = new ColumnLayout(0, 0, config().textPadding);

            // position
            Vec2<Integer> chunkPos = pos.position.getChunkPos();
            Component position = definition(translation("position"));
            Component chunkX = definition(translation("position.x", value(Integer.toString(chunkPos.getX()))));
            Component chunkY = definition(translation("position.y", value(Integer.toString(pos.position.getChunkY()))));
            Component chunkZ = definition(translation("position.z", value(Integer.toString(chunkPos.getY()))));

            left.addComponent(new ParagraphComponent(2, position, chunkX, chunkY, chunkZ));

            // region
            ChunkPos chunkPos1 = new ChunkPos(ModUtil.toBlockPos(pos.position.getBlockPos()));
            Component region = definition(translation("region"));
            Component rFile = value(String.format("r.%d.%d.mca", chunkPos1.getRegionX(), chunkPos1.getRegionZ()));
            Component localRegion = value(String.format("%d %d", chunkPos1.getRegionLocalX(), chunkPos1.getRegionLocalZ()));

            left.addComponent(new ParagraphComponent(2, region, rFile, localRegion));

            hud.addComponent(new LayoutContainerComponent(left));
        }

        {
            ColumnLayout right = new ColumnLayout(0, 0, config().textPadding);

            // Chunks[C]
            Component chunks = definition(translation("chunks"));

            String var1 = ClientUtils.getClient().level != null ? ClientUtils.getClient().level.gatherChunkSourceStats() : "Chunks[C] W: 0/0";

            Pattern chunksPattern = Pattern.compile("Chunks\\[C] W: (\\d+), (\\d+) E:");
            Matcher chunksMatcher = chunksPattern.matcher(var1);
            boolean var3 = chunksMatcher.find();
            Component cached = definition(translation("chunks.cached", value(var3 ? chunksMatcher.group(1) : "0")));
            Component memory = definition(translation("chunks.memory", value(var3 ? chunksMatcher.group(2) : "0")));

            right.addComponent(new ParagraphComponent(2, chunks, cached, memory));

            // C
            Component c = definition(translation("c"));

            String var2 = ClientUtils.getClient().level != null ? ClientUtils.getClient().levelRenderer.getSectionStatistics() : "C: 0/0";

            // why is this field private mojang
            Pattern cPattern = Pattern.compile("C: (\\d+)/(\\d+) \\(s\\)");
            Matcher cMatcher = cPattern.matcher(var2);
            boolean var4 = cMatcher.find();
            Component rendered = definition(translation("c.rendered", value(var4 ? cMatcher.group(1) : "0")));
            Component loaded = definition(translation("c.loaded", value(var4 ? cMatcher.group(2) : "0")));

            right.addComponent(new ParagraphComponent(2, c, rendered, loaded));

            hud.addComponent(new LayoutContainerComponent(right));
        }

        return renderHud(guiGraphics, new PaddingLayout(x, y, config().padding, hud));
    }

}
