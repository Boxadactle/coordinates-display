//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.boxadactle.coordinatesdisplay.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class InvisibleButtonWidget extends ButtonWidget {
    public InvisibleButtonWidget(int x, int y, int width, int height, PressAction onPress) {
        super(x, y, width, height, Text.literal(""), onPress);
    }

    public InvisibleButtonWidget(int x, int y, int width, int height, PressAction onPress, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, Text.literal(""), onPress, tooltipSupplier);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {}
}
