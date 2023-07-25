package dev.boxadactle.coordinatesdisplay.gui;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.gui.config.*;
import dev.boxadactle.coordinatesdisplay.util.ModConstants;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import dev.boxadactle.boxlib.gui.BConfigButton;
import dev.boxadactle.boxlib.gui.BConfigScreen;
import dev.boxadactle.boxlib.gui.widget.*;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends BConfigScreen {

    public ConfigScreen(Screen parent) {
        super(parent);
    }

    @Override
    protected Text getName() {
        return Text.translatable("screen.coordinatesdisplay.config", ModConstants.VERSION_STRING);
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.addDrawableChild(new ButtonWidget.Builder(GuiUtils.CANCEL, button -> {
                    ClientUtils.setScreen(parent);
                    CoordinatesDisplay.CONFIG.reload();
                })
                .dimensions(startX, startY, getButtonWidth(ButtonType.SMALL), getButtonHeight())
                .build()
        );

        this.setSaveButton(createHalfSaveButton(startX, startY, b -> {
            ClientUtils.setScreen(parent);
            CoordinatesDisplay.saveConfig();
        }));
    }

    @Override
    protected void initConfigButtons() {

        // visual settings
        this.addConfigOption(new BConfigScreenButton(
                Text.translatable("button.coordinatesdisplay.visualconfig"),
                this,
                VisualScreen::new
        ));

        // render settings
        this.addConfigOption(new BConfigScreenButton(
                Text.translatable("button.coordinatesdisplay.renderconfig"),
                this,
                RenderScreen::new
        ));

        // color settings
        this.addConfigOption(new BConfigScreenButton(
                Text.translatable("button.coordinatesdisplay.colorconfig"),
                this,
                ColorScreen::new
        ));

        // death pos settings
        this.addConfigOption(new BConfigScreenButton(
                Text.translatable("button.coordinatesdisplay.deathpos"),
                this,
                DeathPosScreen::new
        ));

        // text settings
        this.addConfigOption(new BConfigScreenButton(
                Text.translatable("button.coordinatesdisplay.text"),
                this,
                TextScreen::new
        ));

        this.addConfigOption(new BSpacingEntry());

        this.addConfigOption(new BCustomButton(Text.translatable("button.coordinatesdisplay.configfile")) {
            @Override
            protected void buttonClicked(BConfigButton<?> button) {
                button.active = false;
                if (ModUtil.openConfigFile()) {
                    button.setMessage(Text.translatable("button.coordinatesdisplay.configfilesuccess"));
                } else {
                    button.setMessage(Text.translatable("button.coordinatesdisplay.configfilefail"));
                }
            }
        });

        this.addConfigOption(new BCustomButton(Text.translatable("button.coordinatesdisplay.resetdefault")) {
            @Override
            protected void buttonClicked(BConfigButton<?> button) {
                Screen configScreen = ClientUtils.getCurrentScreen();
                ClientUtils.setScreen(new ConfirmScreen(doIt -> {
                    if (doIt) {
                        CoordinatesDisplay.CONFIG.resetConfig();
                        ClientUtils.setScreen(new ConfigScreen(parent));
                    } else {
                        ClientUtils.setScreen(configScreen);
                    }
                },
                        Text.translatable("screen.coordinatesdisplay.confirmreset"),
                        Text.translatable("message.coordinatesdisplay.confirmreset")
                ));
            }
        });

        this.addConfigOption(new BLinkButton(Text.translatable("button.coordinatesdisplay.wiki"), ModConstants.WIKI));

    }
}
