package me.boxadactle.coordinatesdisplay.mixin;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModVersion;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
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

    private ModVersion version;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void constructor(Minecraft p_108621_, ClientLevel p_108622_, ClientPacketListener p_108623_, StatsCounter p_108624_, ClientRecipeBook p_108625_, boolean p_108626_, boolean p_108627_, CallbackInfo ci) {
        version = ModVersion.getVersion();
    }

    @Inject(at = @At("RETURN"), method = "respawn")
    private void respawn(CallbackInfo ci) {
        if (CoordinatesDisplay.CONFIG.get().showDeathPosInChat) {
            int x = (int) Math.round(minecraft.player.getX());
            int y = (int) Math.round(minecraft.player.getY());
            int z = (int) Math.round(minecraft.player.getZ());

            CoordinatesDisplay.LOGGER.sendChatMessage(ModUtils.makeDeathPositionTextComponent(x, y, z));
        }
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void tick(CallbackInfo ci) {
        if (!CoordinatesDisplay.hasPlayerSeenUpdateMessage && !version.isMostRecent()) {
            CoordinatesDisplay.LOGGER.sendChatMessage(version.getUpdateText());
            CoordinatesDisplay.hasPlayerSeenUpdateMessage = true;
        }
    }
}