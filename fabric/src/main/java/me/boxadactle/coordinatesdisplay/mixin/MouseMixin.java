package me.boxadactle.coordinatesdisplay.mixin;

import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(
            method = {"method_1611([ZLnet/minecraft/client/gui/screen/Screen;DDI)V"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/Screen;mouseClicked(DDI)Z"
            )}
    )
    private static void mouseClick(boolean[] bls, Screen screen, double d, double e, int i, CallbackInfo ci) {
        ModUtil.isMousePressed = true;
    }

    @Inject(
            method = {"method_1605([ZLnet/minecraft/client/gui/screen/Screen;DDI)V"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/Screen;mouseReleased(DDI)Z"
            )}
    )
    private static void mouseReleased(boolean[] bls, Screen screen, double d, double e, int i, CallbackInfo ci) {
        ModUtil.isMousePressed = false;
    }

}
