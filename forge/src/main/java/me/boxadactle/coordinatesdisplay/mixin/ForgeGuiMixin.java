package me.boxadactle.coordinatesdisplay.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeGui.class)
public class ForgeGuiMixin {

    private Minecraft minecraft = Minecraft.getInstance();

    @Inject(at = @At("RETURN"), method = "render")
    private void render(PoseStack matrices, float tickDelta, CallbackInfo ci) {
        if (!this.minecraft.options.hideGui && CoordinatesDisplay.CONFIG.get().visible && !this.minecraft.options.renderDebug && CoordinatesDisplay.shouldRenderOnHud) {
            try {
                Entity camera = this.minecraft.getCameraEntity();

                if (camera == null) return;

                Vec3 pos = new Vec3(camera.getX(), camera.getY(), camera.getZ());
                ChunkPos chunkPos = new ChunkPos((int) Math.round(pos.x), (int) Math.round(pos.z));
                Holder<Biome> biome = this.minecraft.level.getBiome(new BlockPos((int) Math.round(pos.x), (int) Math.round(pos.y), (int) Math.round(pos.z)));
                float cameraYaw = camera.getYHeadRot();
                float cameraPitch = camera.getXRot();

                CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, cameraPitch, biome, CoordinatesDisplay.CONFIG.get().minMode, CoordinatesDisplay.CONFIG.get().hudX, CoordinatesDisplay.CONFIG.get().hudY);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

}
