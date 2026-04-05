package dev.boxadactle.coordinatesdisplay.gui;

import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BBooleanButton;
import dev.boxadactle.boxlib.gui.config.widget.button.BColorPickerButton;
import dev.boxadactle.boxlib.gui.config.widget.field.BStringField;
import dev.boxadactle.boxlib.gui.config.widget.label.BCenteredLabel;
import dev.boxadactle.boxlib.gui.config.widget.label.BLabel;
import dev.boxadactle.boxlib.gui.config.widget.slider.BFloatSlider;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WorldRenderingScreen extends BOptionScreen implements HudHelper {
    public WorldRenderingScreen(Screen parent) {
        super(parent, Component.translatable("screen.coordinatesdisplay.worldrendering", CoordinatesDisplay.VERSION_STRING));
    }

    @Override
    protected void initFooter(LinearLayout linearLayout) {
        setSaveButton(linearLayout.addChild(createBackButton(lastScreen)));
    }

    @Override
    protected void addOptions() {
        // 3d compass
        addConfigLine(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.compass")));

        addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.3dcompass",
                CoordinatesDisplay.getConfig().render3dCompass,
                (val) -> CoordinatesDisplay.getConfig().render3dCompass = val
        ));

        addConfigLine(new BFloatSlider(
                "button.coordinatesdisplay.3dcompasssize",
                0.5f, 3.0f,
                CoordinatesDisplay.getConfig().compassScale,
                2,
                (val) -> CoordinatesDisplay.getConfig().compassScale = val
        ));

        addConfigLine(new BSpacingEntry());
        addConfigLine(new BCenteredLabel(Component.translatable("label.coordinatesdisplay.marking")));

        // mark pos message
        addConfigLine(new BLabel(Component.translatable("label.coordinatesdisplay.markPosMessage")));
        this.addConfigLine(new BStringField(
                config().markPosText,
                newVal -> config().markPosText = newVal
        ));

        addConfigLine(new BLabel(Component.translatable("label.coordinatesdisplay.colors")));

        // beacon color
        addConfigLine(new BColorPickerButton(
                "button.coordinatesdisplay.markBeaconColor",
                this,
                true,
                config().markBeaconColor,
                n -> config().markBeaconColor = n
        ));

        // outline color
        addConfigLine(new BColorPickerButton(
                "button.coordinatesdisplay.markOutlineColor",
                this,
                true,
                config().markOutlineColor,
                n -> config().markOutlineColor = n
        ));

        // text color
        addConfigLine(new BColorPickerButton(
                "button.coordinatesdisplay.markTextColor",
                this,
                false,
                config().markTextColor,
                n -> config().markTextColor = n
        ));

        addConfigLine(new BLabel(Component.translatable("button.coordinatesdisplay.renderconfig")));

        // render beacon
        addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.renderMarkBeacon",
                config().renderMarkBeacon,
                b -> config().renderMarkBeacon = b
        ));

        // render outline
        addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.renderMarkOutline",
                config().renderMarkOutline,
                b -> config().renderMarkOutline = b
        ));

        // render text
        addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.renderMarkText",
                config().renderMarkText,
                b -> config().renderMarkText = b
        ));
    }
}
