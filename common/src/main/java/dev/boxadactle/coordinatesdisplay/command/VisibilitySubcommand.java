package dev.boxadactle.coordinatesdisplay.command;

import dev.boxadactle.boxlib.command.api.BSubcommand;
import dev.boxadactle.boxlib.command.api.subcommand.BasicSubcommand;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import dev.boxadactle.coordinatesdisplay.registry.VisibilityFilter;
import net.minecraft.client.resources.language.I18n;

public class VisibilitySubcommand {

    public static BSubcommand create() {
        VisibilityFilter[] filters = VisibilityFilter.values();

        BSubcommand subcommand = new BasicSubcommand("visibility", CoordinatesCommand::noArgs);

        for (VisibilityFilter filter : filters) {
            subcommand.registerSubcommand(new BasicSubcommand(filter.name().toLowerCase(), (context) -> {
                CoordinatesDisplay.getConfig().visibilityFilter = filter;
                CoordinatesDisplay.CONFIG.save();

                CoordinatesDisplay.LOGGER.player.info(I18n.get("button.coordinatesdisplay.visibility", filter.getName()));

                return 0;
            }));
        }

        return subcommand;
    }


}
