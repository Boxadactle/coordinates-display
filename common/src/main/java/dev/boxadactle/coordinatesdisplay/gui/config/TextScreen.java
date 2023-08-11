package dev.boxadactle.coordinatesdisplay.gui.config;

import dev.boxadactle.boxlib.gui.widget.*;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.boxlib.gui.BConfigScreen;
import dev.boxadactle.coordinatesdisplay.gui.HudHelper;
import dev.boxadactle.coordinatesdisplay.util.ModConfig;
import dev.boxadactle.coordinatesdisplay.util.ModConstants;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.Arrays;

public class TextScreen extends BConfigScreen implements HudHelper {

    public TextScreen(Screen parent) {
        super(parent);
    }

    @Override
    protected Component getName() {
        return Component.translatable("screen.coordinatesdisplay.text", ModConstants.VERSION_STRING);
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.setSaveButton(createBackButton(startX, startY, parent));

        this.setWiki(Component.translatable("button.coordinatesdisplay.wiki"), ModConstants.WIKI_TEXTS);
    }

    @Override
    protected void initConfigButtons() {

        // coordinates chat message
        this.addConfigLine(new BLabel(Component.translatable("label.coordinatesdisplay.posChatMessage")));
        this.addConfigLine(new BStringField(
                config().posChatMessage,
                newVal -> config().posChatMessage = newVal
        ));

        // copy pos message
        this.addConfigLine(new BLabel(Component.translatable("label.coordinatesdisplay.copyPosMessage")));
        this.addConfigLine(new BStringField(
                config().copyPosMessage,
                newVal -> config().copyPosMessage = newVal
        ));

        // round when copying
        this.addConfigLine(new BBooleanButton(
                "button.coordinatesdisplay.roundwhencopying",
                config().shouldRoundWhenCopying,
                newVal -> config().shouldRoundWhenCopying = newVal
        ));

        // teleport mode
        this.addConfigLine(new BEnumButton<>(
                "button.coordinatesdisplay.tpmode",
                config().teleportMode,
                ModConfig.TeleportMode.class,
                newVal -> config().teleportMode = newVal,
                GuiUtils.AQUA
        ));

    }
}