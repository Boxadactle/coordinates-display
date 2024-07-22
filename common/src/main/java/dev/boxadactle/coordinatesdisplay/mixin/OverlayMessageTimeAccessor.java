package dev.boxadactle.coordinatesdisplay.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface OverlayMessageTimeAccessor {

    @Accessor("overlayMessageTime")
    int getOverlayMessageTime();

}
