package me.boxadactle.coordinatesdisplay.gui.widget;

import me.boxadactle.coordinatesdisplay.gui.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public class ConfigScreenWidget extends Button {

    ConfigScreen.Redirector<ConfigScreen> redirector;
    Screen parent;

    public ConfigScreenWidget(ConfigScreen.Redirector<ConfigScreen> redirector, int x, int y, int width, int height, String translationKey, Screen parent) {
        super(x, y, width, height, Component.translatable(translationKey), (button) -> {}, Supplier::get);

        this.redirector = redirector;
        this.parent = parent;
    }

    public void onPress() {
        super.onPress();

        Minecraft.getInstance().setScreen(redirector.create(parent));
    }
}
