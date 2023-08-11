package dev.boxadactle.coordinatesdisplay.gui.config;

import dev.boxadactle.boxlib.gui.BConfigScreen;
import dev.boxadactle.boxlib.gui.widget.*;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.gui.HudHelper;
import dev.boxadactle.coordinatesdisplay.util.ModConfig;
import dev.boxadactle.coordinatesdisplay.util.ModConstants;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.coordinatesdisplay.util.position.Position;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.Arrays;

public class VisualScreen extends BConfigScreen implements HudHelper {

    Position pos;

    public VisualScreen(Screen parent) {
        super(parent);

        pos = this.generatePositionData();
    }

    @Override
    protected Component getName() {
        return Component.translatable("screen.coordinatesdisplay.visual", ModConstants.VERSION_STRING);
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.setSaveButton(createBackButton(startX, startY, parent));

        this.setWiki(Component.translatable("button.coordinatesdisplay.wiki"), ModConstants.WIKI_VISUAL);
    }

    @Override
    protected void initConfigButtons() {

        // visible
        this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.visible",
                config().visible,
                newVal -> config().visible = newVal
        ));

        // round with decimals
        this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.roundDecimals",
                config().decimalRounding,
                newVal -> config().decimalRounding = newVal
        ));

        // display mode
        this.addConfigLine(new BEnumButton<>(
                "button.coordinatesdisplay.displayMode",
                config().renderMode,
                ModConfig.RenderMode.class,
                newVal -> config().renderMode = newVal,
                GuiUtils.AQUA
        ));

        // text shadow
        this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.textshadow",
                config().hudTextShadow,
                newVal -> config().hudTextShadow = newVal
        ));

        // hud position screen
        this.addConfigLine(new BConfigScreenButton(
                Component.translatable("button.coordinatesdisplay.editHudPos"),
                this,
                HudPositionScreen::new
        ));

        // padding
        BIntegerField padding = new BIntegerField(
                config().padding,
                newVal -> config().padding = newVal
        );

        // text padding
        BIntegerField textpadding = new BIntegerField(
                config().textPadding,
                newVal -> config().textPadding = newVal
        );

        this.addConfigLine(
                new BLabel(Component.translatable("label.coordinatesdisplay.padding")),
                new BLabel(Component.translatable("label.coordinatesdisplay.textpadding"))
        );

        this.addConfigLine(padding, textpadding);


        this.addConfigLine(new BSpacingEntry());

        // hud rendering
        this.addConfigLine(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.preview")));
        this.addConfigLine(this.createHudRenderEntry(pos));

        // since minecraft's scrolling panels can't handle different entry sizes
        for (int i = 0; i < (ModUtil.not(config().renderMode, ModConfig.RenderMode.MAXIMUM) ? 3 : 4); i++) {
            this.addConfigLine(new BSpacingEntry());
        }

    }
}
