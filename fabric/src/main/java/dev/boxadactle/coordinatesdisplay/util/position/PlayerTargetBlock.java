package dev.boxadactle.coordinatesdisplay.util.position;

import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class PlayerTargetBlock {

    BlockPos blockPos;
    String blockName;

    public PlayerTargetBlock(Entity camera) {
        HitResult blockHit = camera.raycast(20.0, 0.0F, false);

        BlockPos targeted = null;
        String block = "?";

        if (blockHit.getType().equals(HitResult.Type.BLOCK)) {
            targeted = ((BlockHitResult)blockHit).getBlockPos();
            BlockState blockState = WorldUtils.getWorld().getBlockState(targeted);
            block = String.valueOf(Registries.BLOCK.getId(blockState.getBlock()));
        }

        blockPos = targeted;
        blockName = block;
    }

    public PlayerTargetBlock(BlockPos blockPos, String blockName) {
        this.blockPos = blockPos;
        this.blockName = blockName;
    }

    public String getBlockX() {
        return blockPos != null ? Integer.toString(blockPos.getX()) : "?";
    }

    public String getBlockY() {
        return blockPos != null ? Integer.toString(blockPos.getY()) : "?";
    }

    public String getBlockZ() {
        return blockPos != null ? Integer.toString(blockPos.getZ()) : "?";
    }

    public String getBlockName() {
        return blockName;
    }

}
