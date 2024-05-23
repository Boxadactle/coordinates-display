package dev.boxadactle.coordinatesdisplay.neoforge;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public class ModUtilImpl {

    public static String getBlockName(Block block) {
        return String.valueOf(BuiltInRegistries.BLOCK.getId(block));
    }

}
