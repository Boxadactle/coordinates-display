package dev.boxadactle.coordinatesdisplay.gui.config;

import dev.boxadactle.boxlib.gui.widget.BToggleButton;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.coordinatesdisplay.gui.HudHelper;
import dev.boxadactle.boxlib.ModConstants;
import dev.boxadactle.boxlib.gui.BConfigScreen;
import dev.boxadactle.boxlib.gui.widget.BBooleanButton;
import dev.boxadactle.boxlib.gui.widget.BLabel;
import dev.boxadactle.boxlib.gui.widget.BStringField;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.util.ModConfig;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

import java.util.Arrays;

public class TextScreen extends BConfigScreen implements HudHelper {

    public TextScreen(Screen parent) {
        super(parent);
    }

    @Override
    protected Text getName() {
        return Text.translatable("screen.coordinatesdisplay.text", CoordinatesDisplay.getModConstants().getString());
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.setSaveButton(createBackButton(startX, startY, parent));

        this.setWiki(Text.translatable("button.coordinatesdisplay.wiki"), ModUtil.CONFIG_WIKI_TEXTS);
    }

    @Override
    protected void initConfigButtons() {

        // coordinates chat message
        this.addConfigOption(new BLabel(Text.translatable("label.coordinatesdisplay.posChatMessage")));
        this.addConfigOption(new BStringField(
                config().posChatMessage,
                newVal -> config().posChatMessage = newVal
        ));

        // copy pos message
        this.addConfigOption(new BLabel(Text.translatable("label.coordinatesdisplay.copyPosMessage")));
        this.addConfigOption(new BStringField(
                config().copyPosMessage,
                newVal -> config().copyPosMessage = newVal
        ));

        // round when copying
        this.addConfigOption(new BBooleanButton(
                "button.coordinatesdisplay.roundwhencopying",
                config().shouldRoundWhenCopying,
                newVal -> config().shouldRoundWhenCopying = newVal
        ));

        // teleport mode
        this.addConfigOption(new BToggleButton<>(
                "button.coordinatesdisplay.tpmode",
                config().teleportMode,
                Arrays.stream(ModConfig.TeleportMode.values()).toList(),
                newVal -> config().teleportMode = newVal
        ) {
            @Override
            public ModConfig.TeleportMode to(Text input) {
                String a = ((TranslatableTextContent)input.getContent()).getKey().substring("button.coordinatesdisplay.tpmode.".length()).toUpperCase();

                return ModConfig.TeleportMode.valueOf(a);
            }

            @Override
            public Text from(ModConfig.TeleportMode input) {
                return GuiUtils.colorize(Text.translatable("button.coordinatesdisplay.tpmode." + input.name().toLowerCase()), GuiUtils.AQUA);
            }
        });

    }
}
