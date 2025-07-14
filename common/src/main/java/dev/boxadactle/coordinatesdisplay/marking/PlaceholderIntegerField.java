package dev.boxadactle.coordinatesdisplay.marking;

import dev.boxadactle.boxlib.gui.config.widget.field.BIntegerField;
import dev.boxadactle.boxlib.util.GuiUtils;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Consumer;

public class PlaceholderIntegerField extends BIntegerField {
    int placeholder;
    public PlaceholderIntegerField(Consumer<Integer> function, int placeholder) {
        super(placeholder, function);

        setValue("");
        this.placeholder = placeholder;

        function.accept(placeholder);
    }

    @Override
    public Integer to(String input) {
        if (input.isBlank()) return placeholder;

        return super.to(input);
    }

    @Override
    public void renderWidget(GuiGraphics p_93657_, int mouseX, int mouseY, float delta) {
        super.renderWidget(p_93657_, mouseX, mouseY, delta);

        if (getValue().isBlank()) {
            p_93657_.drawString(
                    GuiUtils.getTextRenderer(),
                    String.valueOf(placeholder),
                    getX() + 4,
                    getY() + 4,
                    GuiUtils.GRAY,
                    false
            );
        }
    }
}
