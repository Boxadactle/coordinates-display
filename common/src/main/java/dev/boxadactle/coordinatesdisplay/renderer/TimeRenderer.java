package dev.boxadactle.coordinatesdisplay.renderer;

import dev.boxadactle.boxlib.layouts.LayoutComponent;
import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.layouts.component.TextComponent;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.DateUtil;
import dev.boxadactle.coordinatesdisplay.HudDisplayMode;
import dev.boxadactle.coordinatesdisplay.HudRenderer;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@HudDisplayMode(
        value = "time",
        hasChunkData = false,
        hasDirectionInt = false,
        hasMCVersion = false,
        hasDimension = false,
        hasBiome = false,
        hasDirection = false,
        hasTime = false
)
public class TimeRenderer implements HudRenderer {
    @Override
    public RenderingLayout renderOverlay(int x, int y, Position pos) {
        ColumnLayout columnLayout = new ColumnLayout(0, 0, 0);

        columnLayout.addComponent(new LayoutComponent<>(null) {
            @Override
            public int getWidth() {
                return 32;
            }

            @Override
            public int getHeight() {
                return 32;
            }

            @Override
            public void render(GuiGraphicsExtractor guiGraphics, int i, int i1) {
                guiGraphics.pose().pushMatrix();
                guiGraphics.pose().scale(2, 2);
                if (WorldUtils.getWorld() != null) guiGraphics.fakeItem(new ItemStack(Items.CLOCK), i / 2, i1 / 2);
                else guiGraphics.blit(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("textures/item/clock_00.png"), x, y, 0.0f, 0.0f, 15, 15, 16, 16);
                guiGraphics.pose().popMatrix();
            }
        });

        if (config().renderDay) {
            Component day = definition(GlobalTexts.DAY, value(Long.toString(pos.world.getDay())));

            columnLayout.addComponent(new TextComponent(day));
        }

        if (config().renderTime) {
            String formatted = config().militaryTime ? DateUtil.formatMinecraftDayTime24(pos.world.getTime()) : DateUtil.formatMinecraftDayTime(pos.world.getTime());
            Component time = definition(GlobalTexts.TIME, value(formatted));

            columnLayout.addComponent(new TextComponent(time));
        }

        return new PaddingLayout(x, y, config().padding, columnLayout);
    }
}
