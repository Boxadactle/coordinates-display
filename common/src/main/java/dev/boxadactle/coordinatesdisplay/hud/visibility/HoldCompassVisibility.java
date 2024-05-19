package dev.boxadactle.coordinatesdisplay.hud.visibility;

import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.coordinatesdisplay.hud.HudVisibility;
import dev.boxadactle.coordinatesdisplay.hud.HudVisibilityFilter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@HudVisibility("hold_compass")
public class HoldCompassVisibility implements HudVisibilityFilter {
    @Override
    public boolean isVisible() {
        ItemStack p = WorldUtils.getPlayer().getInventory().getSelected();

        return p.is(Items.COMPASS);
    }
}
