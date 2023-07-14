package dev.boxadactle.coordinatesdisplay.gui.config;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.gui.BConfigScreen;
import dev.boxadactle.boxlib.gui.widget.*;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.gui.HudHelper;
import dev.boxadactle.coordinatesdisplay.util.ModConfig;
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
        return Component.translatable("screen.coordinatesdisplay.visual", CoordinatesDisplay.getModConstants().getString());
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.setSaveButton(createBackButton(startX, startY, parent));

        this.setWiki(Component.translatable("button.coordinatesdisplay.wiki"), ModUtil.CONFIG_WIKI_VISUAL);
    }

    @Override
    protected void initConfigButtons() {

        // visible
        this.addConfigOption(new BBooleanButton(
                "button.coordinatesdisplay.visible",
                config().visible,
                newVal -> config().visible = newVal
        ));

        // round with decimals
        this.addConfigOption(new BBooleanButton(
                "button.coordinatesdisplay.roundDecimals",
                config().decimalRounding,
                newVal -> config().decimalRounding = newVal
        ));

        // display mode
        this.addConfigOption(new BToggleButton<>(
                "button.coordinatesdisplay.displayMode",
                config().renderMode,
                Arrays.stream(ModConfig.RenderMode.values()).toList(),
                newVal -> config().renderMode = newVal
        ) {
            @Override
            public ModConfig.RenderMode to(Component input) {
                String a = ((TranslatableContents)input.getContents()).getKey().substring("button.coordinatesdisplay.displayMode.".length());

                return ModConfig.RenderMode.valueOf(a);
            }

            @Override
            public Component from(ModConfig.RenderMode input) {
                return GuiUtils.colorize(Component.translatable("button.coordinatesdisplay.displayMode." + input.name().toLowerCase()), GuiUtils.AQUA);
            }
        });


        // text shadow
        this.addConfigOption(new BBooleanButton(
                "button.coordinatesdisplay.textshadow",
                config().hudTextShadow,
                newVal -> config().hudTextShadow = newVal
        ));

        // hud position screen
        this.addConfigOption(new BConfigScreenButton(
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
        BIntegerField Componentpadding = new BIntegerField(
                config().textPadding,
                newVal -> config().textPadding = newVal
        );

        this.addConfigOption(new BWidgetContainer(
                new BLabel(Component.translatable("label.coordinatesdisplay.padding")),
                new BLabel(Component.translatable("label.coordinatesdisplay.textpadding"))
        ));

        this.addConfigOption(new BWidgetContainer(padding, Componentpadding));


        this.addConfigOption(new BSpacingEntry());

        // hud rendering
        this.addConfigOption(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.preview")));
        this.addConfigOption(this.createHudRenderEntry(pos));

        // since minecraft's scrolling panels can't handle different entry sizes
        for (int i = 0; i < 3; i++) {
            this.addConfigOption(new BSpacingEntry());
        }

    }
}
