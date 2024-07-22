package dev.boxadactle.coordinatesdisplay.hud.visibility;

import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.hud.HudVisibility;
import dev.boxadactle.coordinatesdisplay.hud.HudVisibilityFilter;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@HudVisibility("own_map")
public class OwnMapVisibility implements HudVisibilityFilter {
    @Override
    public boolean isVisible() {
        Inventory inventory = WorldUtils.getPlayer().getInventory();

        return inventory.contains(new ItemStack(Items.MAP)) || inventory.contains(new ItemStack(Items.FILLED_MAP));
    }
}
