package me.boxadactle.coordinatesdisplay;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.boxadactle.coordinatesdisplay.gui.ConfigScreenMain;

public class CoordinatesDisplayModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigScreenMain::new;
    }
}