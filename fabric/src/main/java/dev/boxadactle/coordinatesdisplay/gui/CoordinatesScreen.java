package dev.boxadactle.coordinatesdisplay.gui;

import dev.boxadactle.boxlib.math.Vec3;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import dev.boxadactle.boxlib.gui.BConfigHelper;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.MouseUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CoordinatesScreen extends Screen implements BConfigHelper {

    Position pos;

    int buttonw = getButtonWidth(ButtonType.NORMAL);
    int buttonh = getButtonHeight();

    int p = 5;

    public CoordinatesScreen(Position pos) {
        super(Text.literal("Coordinates Screen"));

        this.pos = pos;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);

        Vec3<Double> player = pos.position.getPlayerPos();

        int x = (int)Math.round(player.getX());
        int y = (int)Math.round(player.getY());
        int z = (int)Math.round(player.getZ());

        RenderUtils.drawTextCentered(drawContext, Text.translatable("message.coordinatesdisplay.at"), this.width / 2, (this.height / 4) - 20);
        RenderUtils.drawTextCentered(drawContext, Text.translatable("message.coordinatesdisplay.location", x, y, z), this.width / 2, (this.height / 4));

        super.render(drawContext, mouseX, mouseY, delta);
    }

    private void resume() {
        ClientUtils.setScreen(null);
        MouseUtils.getMouse().lockCursor();
    }

    @Override
    public void init() {
        super.init();

        int bstart = this.height / 2 - 20;

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.copy"), button -> {
            ClientUtils.getKeyboard().setClipboard(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().copyPosMessage, pos));
            CoordinatesDisplay.LOGGER.player.info("Copied coordinates to clipboard");
            resume();
        }).dimensions(this.width / 2 - buttonw / 2, bstart, buttonw, buttonh).build());

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.send"), button -> {
            CoordinatesDisplay.LOGGER.player.publicChat(ModUtil.parseText(CoordinatesDisplay.CONFIG.get().posChatMessage, pos));
            CoordinatesDisplay.LOGGER.player.info("Put Coordinates in Chat");
            resume();
        }).dimensions(this.width / 2 - buttonw / 2, bstart + (buttonh + p), buttonw, buttonh).build());

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("button.coordinatesdisplay.copytp"), button -> {
            ClientUtils.getKeyboard().setClipboard(ModUtil.toTeleportCommand(pos.position.getPlayerPos(), WorldUtils.getCurrentDimension()));
            CoordinatesDisplay.LOGGER.player.info("Copied as TP command");
            resume();
        }).dimensions(this.width / 2 - buttonw / 2, bstart + (buttonh + p) * 2, buttonw, buttonh).build());
    }

    @Override
    public void close() {
        this.client.setScreen(null);
    }
}
