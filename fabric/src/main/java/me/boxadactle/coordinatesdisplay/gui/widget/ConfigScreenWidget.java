package me.boxadactle.coordinatesdisplay.gui.widget;

import me.boxadactle.coordinatesdisplay.gui.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class ConfigScreenWidget extends ButtonWidget {

    ConfigScreen.Redirector<ConfigScreen> redirector;
    Screen parent;

    public ConfigScreenWidget(ConfigScreen.Redirector<ConfigScreen> redirector, int x, int y, int width, int height, String translationKey, Screen parent) {
        super(x, y, width, height, Text.translatable(translationKey), (button) -> {}, Supplier::get);

        this.redirector = redirector;
        this.parent = parent;
    }

    public void onPress() {
        super.onPress();

        MinecraftClient.getInstance().setScreen(redirector.create(parent));
    }

}
