package dev.boxadactle.coordinatesdisplay.fabric;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;

public class ModUtilImpl {

    public static String getBlockName(Block block) {
        return String.valueOf(Registry.BLOCK.getId(block));
    }

}
