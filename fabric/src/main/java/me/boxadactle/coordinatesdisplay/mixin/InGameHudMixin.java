package me.boxadactle.coordinatesdisplay.mixin;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.init.Keybinds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow @Final private MinecraftClient client;

    // need to render the overlay in the render method
    @Inject(at = @At("RETURN"), method = "render")
    private void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (!this.client.options.hudHidden && CoordinatesDisplay.CONFIG.visible && !this.client.options.debugEnabled && CoordinatesDisplay.shouldRenderOnHud) {
            try {
                Entity camera = this.client.getCameraEntity();

                if (camera == null) return;

                Vec3d pos = camera.getPos();
                ChunkPos chunkPos = new ChunkPos(new BlockPos(pos));
                Biome biome = this.client.world.getBiome(camera.getBlockPos());
                float cameraYaw = camera.getYaw(tickDelta);

                CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, biome, CoordinatesDisplay.CONFIG.hudX, CoordinatesDisplay.CONFIG.hudY);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        Vec3d pos = this.client.getCameraEntity().getPos();
        int x = (int) Math.round(pos.getX());
        int y = (int) Math.round(pos.getY());
        int z = (int) Math.round(pos.getZ());
        Keybinds.checkBindings(x, y, z);
    }
}
