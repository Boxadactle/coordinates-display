package dev.boxadactle.coordinatesdisplay.position;

import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class PlayerTargetBlock {

    BlockPos blockPos;
    String blockName;

    public PlayerTargetBlock(Entity camera) {
        HitResult blockHit = camera.pick(20.0, 0.0F, false);

        BlockPos targeted = null;
        String block = "?";

        if (blockHit.getType().equals(HitResult.Type.BLOCK)) {
            targeted = ((BlockHitResult)blockHit).getBlockPos();
            BlockState blockState = WorldUtils.getWorld().getBlockState(targeted);
            block = ModUtil.getBlockName(blockState.getBlock());
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
