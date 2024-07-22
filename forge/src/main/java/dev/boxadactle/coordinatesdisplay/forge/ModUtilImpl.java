package dev.boxadactle.coordinatesdisplay.forge;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class ModUtilImpl {

    public static String getBlockName(Block block) {
        return String.valueOf(ForgeRegistries.BLOCKS.getKey(block));
    }

}
