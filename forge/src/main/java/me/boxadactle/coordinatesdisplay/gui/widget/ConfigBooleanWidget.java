package me.boxadactle.coordinatesdisplay.gui.widget;

import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigBooleanWidget extends Button {

    private final Consumer<Boolean> action;
    private boolean currentValue;
    private final String translationKey;

    public ConfigBooleanWidget(int x, int y, int width, int height, String translationKey, boolean value, Consumer<Boolean> action) {
        super(x, y, width, height, Component.translatable(translationKey, (value ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {}, Supplier::get);

        this.action = action;
        this.currentValue = value;
        this.translationKey = translationKey;
    }

    public void onPress() {
        super.onPress();

        currentValue = !currentValue;
        this.action.accept(currentValue);
        super.setMessage(Component.translatable(translationKey, (currentValue ? ModUtil.TRUE : ModUtil.FALSE)));

    }
}
