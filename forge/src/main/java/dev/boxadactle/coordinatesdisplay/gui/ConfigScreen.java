package dev.boxadactle.coordinatesdisplay.gui;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.gui.config.*;
import dev.boxadactle.boxlib.gui.BConfigButton;
import dev.boxadactle.boxlib.gui.BConfigScreen;
import dev.boxadactle.boxlib.gui.widget.BConfigScreenButton;
import dev.boxadactle.boxlib.gui.widget.BCustomButton;
import dev.boxadactle.boxlib.gui.widget.BLinkButton;
import dev.boxadactle.boxlib.gui.widget.BSpacingEntry;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.gui.config.*;
import dev.boxadactle.coordinatesdisplay.util.ModConfig;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends BConfigScreen {

    public ConfigHolder<ModConfig> original;

    public ConfigScreen(Screen parent) {
        super(parent);

        original = CoordinatesDisplay.CONFIG;
    }

    @Override
    protected Component getName() {
        return Component.translatable("screen.coordinatesdisplay.config", CoordinatesDisplay.getModConstants().getString());
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.addRenderableWidget(new Button.Builder(GuiUtils.CANCEL, button -> {
                    ClientUtils.setScreen(parent);
                    CoordinatesDisplay.CONFIG = original;
                })
                        .bounds(startX, startY, getButtonWidth(ButtonType.SMALL), getButtonHeight())
                        .build()
        );

        this.setSaveButton(createHalfSaveButton(startX, startY, b -> {
            ClientUtils.setScreen(parent);
            CoordinatesDisplay.CONFIG.save();
        }));
    }

    @Override
    protected void initConfigButtons() {

        // visual settings
        this.addConfigOption(new BConfigScreenButton(
                Component.translatable("button.coordinatesdisplay.visualconfig"),
                this,
                VisualScreen::new
        ));

        // render settings
        this.addConfigOption(new BConfigScreenButton(
                Component.translatable("button.coordinatesdisplay.renderconfig"),
                this,
                RenderScreen::new
        ));

        // color settings
        this.addConfigOption(new BConfigScreenButton(
                Component.translatable("button.coordinatesdisplay.colorconfig"),
                this,
                ColorScreen::new
        ));

        // death pos settings
        this.addConfigOption(new BConfigScreenButton(
                Component.translatable("button.coordinatesdisplay.deathpos"),
                this,
                DeathPosScreen::new
        ));

        // text settings
        this.addConfigOption(new BConfigScreenButton(
                Component.translatable("button.coordinatesdisplay.text"),
                this,
                TextScreen::new
        ));

        this.addConfigOption(new BSpacingEntry());

        this.addConfigOption(new BCustomButton(Component.translatable("button.coordinatesdisplay.configfile")) {
            @Override
            protected void buttonClicked(BConfigButton<?> button) {
                button.active = false;
                if (ModUtil.openConfigFile()) {
                    button.setMessage(Component.translatable("button.coordinatesdisplay.configfilesuccess"));
                } else {
                    button.setMessage(Component.translatable("button.coordinatesdisplay.configfilefail"));
                }
            }
        });

        this.addConfigOption(new BCustomButton(Component.translatable("button.coordinatesdisplay.resetdefault")) {
            @Override
            protected void buttonClicked(BConfigButton<?> button) {
                Screen configScreen = ClientUtils.getCurrentScreen();
                ClientUtils.setScreen(new ConfirmScreen(doIt -> {
                    if (doIt) {
                        ModUtil.resetConfig();
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

        this.addConfigOption(new BLinkButton(Component.translatable("button.coordinatesdisplay.wiki"), ModUtil.CONFIG_WIKI));

    }
}
