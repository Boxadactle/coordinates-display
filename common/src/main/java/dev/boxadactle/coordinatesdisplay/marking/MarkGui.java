package dev.boxadactle.coordinatesdisplay.marking;

import com.google.common.collect.ImmutableList;
import dev.boxadactle.boxlib.gui.config.BConfigList;
import dev.boxadactle.boxlib.gui.config.BOptionEntry;
import dev.boxadactle.boxlib.gui.config.BOptionHelper;
import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BCustomButton;
import dev.boxadactle.boxlib.gui.config.widget.label.BCenteredLabel;
import dev.boxadactle.boxlib.math.geometry.Vec3;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.position.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

import java.util.List;

public class MarkGui extends BOptionScreen {
    int x;
    int y;
    int z;

    Position pos;

    BCustomButton b;
    BCustomButton s;

    public MarkGui() {
        super(null, Component.translatable("screen.coordinatesdisplay.mark"));

        pos = Position.of(WorldUtils.getPlayer());

        x = pos.position.getBlockPos().getX();
        y = pos.position.getBlockPos().getY();
        z = pos.position.getBlockPos().getZ();
    }

    @Override
    protected void initFooter(LinearLayout linearLayout) {
        linearLayout.addChild(createDoneButton(lastScreen));
    }

    @Override
    public void tick() {
        b.active = CoordinatesDisplay.MARK_POS != null;
        s.active = CoordinatesDisplay.MARK_POS != null;
    }

    @Override
    protected void addOptions() {
        addConfigLine(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.markPos")));
        addConfigLine(new TripleEntry(
                new BCenteredLabel(Component.literal("X: ")),
                new BCenteredLabel(Component.literal("Y: ")),
                new BCenteredLabel(Component.literal("Z: "))
        ));
        addConfigLine(new TripleEntry(
                new PlaceholderIntegerField(v -> x = v, x),
                new PlaceholderIntegerField(v -> y = v, y),
                new PlaceholderIntegerField(v -> z = v, z)
        ));

        b = BCustomButton.create(Component.translatable("button.coordinatesdisplay.clearMarked"), () -> {
            CoordinatesDisplay.MARK_POS = null;
        });

        addConfigLine(b, BCustomButton.create(Component.translatable("button.coordinatesdisplay.mark"), () -> {
            CoordinatesDisplay.MARK_POS = new Vec3<>(x, y, z);
            CoordinatesDisplay.LOGGER.player.info(I18n.get("command.coordinatesdisplay.mark", x, y, z));
        }));

        addConfigLine(new BSpacingEntry());

        s = addConfigLine(BCustomButton.create(Component.translatable("button.coordinatesdisplay.shareMark"), () -> {
            ClientUtils.confirm(
                    Component.translatable("message.coordinatesdisplay.mark.shared.confirm"),
                    Component.translatable("message.coordinatesdisplay.mark.shared.confirm2"),
                    () -> {
                        CoordinatesDisplay.LOGGER.player.publicChat(MarkSerializer.serialize(CoordinatesDisplay.MARK_POS));
                        ClientUtils.setScreen(null);
                    },
                    () -> ClientUtils.setScreen(null)
            );
        }));
    }

    public static class TripleEntry extends BConfigList.ConfigEntry {
        BOptionEntry<?> widget1;
        BOptionEntry<?> widget2;
        BOptionEntry<?> widget3;

        public TripleEntry(BOptionEntry<?> widget1, BOptionEntry<?> widget2, BOptionEntry<?> widget3) {
            this.widget1 = widget1;
            this.widget2 = widget2;
            this.widget3 = widget3;
        }

        public List<? extends AbstractWidget> getWidgets() {
            return ImmutableList.of((AbstractWidget)this.widget1, (AbstractWidget)this.widget2, (AbstractWidget)this.widget3);
        }

        public boolean isInvalid() {
            return this.widget1.isInvalid() || this.widget2.isInvalid();
        }

        @Override
        public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hovered, float v) {
            AbstractWidget w1 = (AbstractWidget)this.widget1;
            AbstractWidget w2 = (AbstractWidget)this.widget2;
            AbstractWidget w3 = (AbstractWidget)this.widget3;
            int p1 = BOptionHelper.padding() / 2;
            int p2 = BOptionHelper.padding() / 2;
            w1.setX(getX());
            w1.setY(getY());
            w1.setWidth(getWidth() / 3 - p1);
            w2.setX(getX() + getWidth() / 3 + p2);
            w2.setY(getY());
            w2.setWidth(getWidth() / 3 - p2);
            w3.setX(getX() + 2 * (getWidth() / 3) + p1);
            w3.setY(getY());
            w3.setWidth(getWidth() / 3 - p1);
            w1.render(guiGraphics, mouseX, mouseY, v);
            w2.render(guiGraphics, mouseX, mouseY, v);
            w3.render(guiGraphics, mouseX, mouseY, v);
        }
    }
}
