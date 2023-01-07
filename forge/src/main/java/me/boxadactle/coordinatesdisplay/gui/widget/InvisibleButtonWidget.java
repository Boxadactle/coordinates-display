//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.boxadactle.coordinatesdisplay.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InvisibleButtonWidget extends PlainTextButton {
    public InvisibleButtonWidget(int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height, Component.translatable(""), onPress, Minecraft.getInstance().font);
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {}
}