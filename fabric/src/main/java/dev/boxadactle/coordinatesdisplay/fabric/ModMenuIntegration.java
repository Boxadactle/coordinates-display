package dev.boxadactle.coordinatesdisplay.fabric;

import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.config.screen.ConfigScreen;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Function;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return ConfigScreen::new;
    }

    @Override
    public String getModId() {
        return CoordinatesDisplay.MOD_ID;
    }
}
