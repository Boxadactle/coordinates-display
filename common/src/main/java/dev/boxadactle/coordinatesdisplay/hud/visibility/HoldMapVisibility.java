package dev.boxadactle.coordinatesdisplay.hud.visibility;

import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.hud.HudVisibility;
import dev.boxadactle.coordinatesdisplay.hud.HudVisibilityFilter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@HudVisibility("hold_map")
public class HoldMapVisibility implements HudVisibilityFilter {
    @Override
    public boolean isVisible() {
        ItemStack p = WorldUtils.getPlayer().getInventory().getSelected();

        return p.getItem() == Items.MAP || p.getItem() == Items.FILLED_MAP;
    }
}
