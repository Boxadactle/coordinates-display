package me.boxadactle.coordinatesdisplay.mixin;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.init.Keybinds;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.registry.entry.RegistryEntry;
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
public abstract class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;

    // need to render the overlay in the render method
    @Inject(at = @At("RETURN"), method = "render")
    private void render(DrawContext drawContext, float tickDelta, CallbackInfo ci) {
        if (!this.client.options.hudHidden && CoordinatesDisplay.CONFIG.get().visible && !this.client.options.debugEnabled && CoordinatesDisplay.shouldRenderOnHud) {
            try {
                Entity camera = this.client.getCameraEntity();

                if (camera == null) return;

                Vec3d pos = camera.getPos();
                ChunkPos chunkPos = new ChunkPos(new BlockPos(ModUtil.doubleVecToIntVec(pos)));
                RegistryEntry<Biome> biome = this.client.world.getBiome(camera.getBlockPos());
                float cameraYaw = camera.getYaw(tickDelta);
                float cameraPitch = camera.getPitch(tickDelta);

                CoordinatesDisplay.OVERLAY.render(drawContext, pos, chunkPos, cameraYaw, cameraPitch, biome, CoordinatesDisplay.CONFIG.get().hudX, CoordinatesDisplay.CONFIG.get().hudY, CoordinatesDisplay.CONFIG.get().minMode, false, CoordinatesDisplay.CONFIG.get().hudScale);
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
