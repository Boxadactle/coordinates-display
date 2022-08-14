//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.boxadactle.coordinatesdisplay.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InvisibleButtonWidget extends Button {
    public InvisibleButtonWidget(int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height, new TextComponent(""), onPress);
    }

    public InvisibleButtonWidget(int x, int y, int width, int height, OnPress onPress, OnTooltip tooltipSupplier) {
        super(x, y, width, height, new TextComponent(""), onPress, tooltipSupplier);
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {}
}