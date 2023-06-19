package me.boxadactle.coordinatesdisplay.gui.widget;

import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigBooleanWidget extends ButtonWidget {

    private final Consumer<Boolean> action;
    private boolean currentValue;
    private final String translationKey;

    public ConfigBooleanWidget(int x, int y, int width, int height, String translationKey, boolean value, Consumer<Boolean> action) {
        super(x, y, width, height, Text.translatable(translationKey, (value ? ModUtil.TRUE : ModUtil.FALSE)), (button) -> {}, Supplier::get);

        this.action = action;
        this.currentValue = value;
        this.translationKey = translationKey;
    }

    public void onPress() {
        super.onPress();

        currentValue = !currentValue;
        this.action.accept(currentValue);
        super.setMessage(Text.translatable(translationKey, (currentValue ? ModUtil.TRUE : ModUtil.FALSE)));
    }
}
