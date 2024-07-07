package dev.boxadactle.coordinatesdisplay.config.screen;

import dev.boxadactle.boxlib.gui.config.BOptionButton;
import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.*;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.config.ModConfig;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends BOptionScreen {

    public ConfigScreen(Screen parent) {
        super(parent);

        CoordinatesDisplay.CONFIG.cacheConfig();
    }

    @Override
    protected Component getName() {
        return Component.translatable("screen.coordinatesdisplay.config", CoordinatesDisplay.VERSION_STRING);
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.addRenderableWidget(createHalfCancelButton(startX, startY, b -> {
            ClientUtils.setScreen(parent);
            CoordinatesDisplay.CONFIG.restoreCache();
        }));

        this.setSaveButton(createHalfSaveButton(startX, startY, b -> {
            ClientUtils.setScreen(parent);
            CoordinatesDisplay.CONFIG.save();
        }));
    }

    @Override
    protected void initConfigButtons() {

        // enabled
        this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.enabled",
                CoordinatesDisplay.getConfig().enabled,
                (val) -> CoordinatesDisplay.getConfig().enabled = val
        ));

        // visual settings
        this.addConfigLine(new BConfigScreenButton(
                Component.translatable("button.coordinatesdisplay.visualconfig"),
                this,
                VisualScreen::new
        ));

        // render settings
        this.addConfigLine(new BConfigScreenButton(
                Component.translatable("button.coordinatesdisplay.renderconfig"),
                this,
                RenderScreen::new
        ));

        // color settings
        this.addConfigLine(new BConfigScreenButton(
                Component.translatable("button.coordinatesdisplay.colorconfig"),
                this,
                ColorScreen::new
        ));

        // death pos settings
        this.addConfigLine(new BConfigScreenButton(
                Component.translatable("button.coordinatesdisplay.deathpos"),
                this,
                DeathPosScreen::new
        ));

        // text settings
        this.addConfigLine(new BConfigScreenButton(
                Component.translatable("button.coordinatesdisplay.text"),
                this,
                TextScreen::new
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
