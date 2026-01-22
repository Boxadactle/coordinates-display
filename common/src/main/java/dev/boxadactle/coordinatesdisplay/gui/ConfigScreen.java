package dev.boxadactle.coordinatesdisplay.gui;

import dev.boxadactle.boxlib.gui.config.BOptionButton;
import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.*;
import dev.boxadactle.boxlib.gui.config.widget.slider.BFloatSlider;
import dev.boxadactle.boxlib.gui.config.widget.slider.BIntegerSlider;
import dev.boxadactle.boxlib.prompt.Prompts;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.ModConfig;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends BOptionScreen {

    public ConfigScreen(Screen parent) {
        super(parent, Component.translatable("screen.coordinatesdisplay.config", CoordinatesDisplay.VERSION_STRING));

        try {
            ModConfig.checkValidity(CoordinatesDisplay.CONFIG.get());

            CoordinatesDisplay.CONFIG.cacheConfig();
        } catch (NullPointerException e) {
            Prompts.alert(this, Component.translatable("message.coordinatesdisplay.configError"));

            CoordinatesDisplay.CONFIG.resetConfig();
            CoordinatesDisplay.CONFIG.save();
            CoordinatesDisplay.CONFIG.cacheConfig();

            CoordinatesDisplay.LOGGER.printStackTrace(e);
        }
    }

    @Override
    protected void initFooter(LinearLayout layout) {
        layout.addChild(createCancelButton(b -> {
            ClientUtils.setScreen(lastScreen);
            CoordinatesDisplay.CONFIG.restoreCache();
        }));

        this.setSaveButton(layout.addChild(createSaveButton(b -> {
            ClientUtils.setScreen(lastScreen);
            CoordinatesDisplay.CONFIG.save();
        })));
    }

    @Override
    protected void addOptions() {

        // enabled
        this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.enabled",
                CoordinatesDisplay.getConfig().enabled,
                (val) -> CoordinatesDisplay.getConfig().enabled = val
        ));

        // visual settings
        this.addConfigLine(new BScreenButton(
                Component.translatable("button.coordinatesdisplay.visualconfig"),
                this,
                VisualScreen::new
        ));

        // render settings
        this.addConfigLine(new BScreenButton(
                Component.translatable("button.coordinatesdisplay.renderconfig"),
                this,
                RenderScreen::new
        ));

        // color settings
        this.addConfigLine(new BScreenButton(
                Component.translatable("button.coordinatesdisplay.colorconfig"),
                this,
                ColorScreen::new
        ));

        // death pos settings
        this.addConfigLine(new BScreenButton(
                Component.translatable("button.coordinatesdisplay.deathpos"),
                this,
                DeathPosScreen::new
        ));

        // text settings
        this.addConfigLine(new BScreenButton(
                Component.translatable("button.coordinatesdisplay.text"),
                this,
                TextScreen::new
        ));

        // 3d compass
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

        this.addConfigLine(new BSpacingEntry());

        this.addConfigLine(new BCustomButton(Component.translatable("button.coordinatesdisplay.configfile")) {
            @Override
            protected void buttonClicked(BOptionButton<?> button) {
                button.active = false;
                if (ModUtil.openConfigFile()) {
                    button.setMessage(Component.translatable("button.coordinatesdisplay.configfilesuccess"));
                } else {
                    button.setMessage(Component.translatable("button.coordinatesdisplay.configfilefail"));
                }
            }
        });

        this.addConfigLine(new BCustomButton(Component.translatable("button.coordinatesdisplay.resetdefault")) {
            @Override
            protected void buttonClicked(BOptionButton<?> button) {
                Screen configScreen = ClientUtils.getCurrentScreen();
                ClientUtils.setScreen(new ConfirmScreen(doIt -> {
                    if (doIt) {
                        CoordinatesDisplay.CONFIG.resetConfig();
                        ClientUtils.setScreen(new ConfigScreen(parent));
                    } else {
                        ClientUtils.setScreen(configScreen);
                    }
                },
                        Component.translatable("screen.coordinatesdisplay.confirmreset"),
                        Component.translatable("message.coordinatesdisplay.confirmreset")
                ));
            }
        });

        this.addConfigLine(new BLinkButton(Component.translatable("button.coordinatesdisplay.wiki"), CoordinatesDisplay.WIKI));

    }
}
