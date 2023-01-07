package me.boxadactle.coordinatesdisplay.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
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

                Vec3i pos = new Vec3i(camera.getX(), camera.getY(), camera.getZ());
                ChunkPos chunkPos = new ChunkPos(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
                Holder<Biome> biome = this.minecraft.level.getBiome(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
                float cameraYaw = camera.getYHeadRot();

                CoordinatesDisplay.OVERLAY.render(matrices, pos, chunkPos, cameraYaw, biome, CoordinatesDisplay.CONFIG.get().hudX, CoordinatesDisplay.CONFIG.get().hudY);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

}
