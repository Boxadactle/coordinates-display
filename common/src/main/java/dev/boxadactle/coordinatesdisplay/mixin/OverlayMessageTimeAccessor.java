package dev.boxadactle.coordinatesdisplay.mixin;

import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Hud.class)
public interface OverlayMessageTimeAccessor {

    @Accessor("overlayMessageTime")
    int getOverlayMessageTime();

}
