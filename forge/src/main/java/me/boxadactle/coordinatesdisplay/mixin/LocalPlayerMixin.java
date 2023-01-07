package me.boxadactle.coordinatesdisplay.mixin;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.stats.StatsCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Shadow @Final protected Minecraft minecraft;

    @Inject(at = @At("RETURN"), method = "respawn")
    private void respawn(CallbackInfo ci) {
        if (CoordinatesDisplay.CONFIG.get().showDeathPosInChat) {
            int x = (int) Math.round(minecraft.player.getX());
            int y = (int) Math.round(minecraft.player.getY());
            int z = (int) Math.round(minecraft.player.getZ());

            CoordinatesDisplay.LOGGER.player.chat(ModUtil.makeDeathPositionTextComponent(x, y, z));
        }
    }
}