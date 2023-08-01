package dev.boxadactle.coordinatesdisplay.forge.init;

import dev.boxadactle.boxlib.forge.command.BCommandManager;
import dev.boxadactle.coordinatesdisplay.forge.command.*;

public class Commands {

    public static void register() {
        BCommandManager.registerCommand("coordinates", list -> {
            list.add(GuiCommand::new);
            list.add(PositionCommand::new);
            list.add(ToggleCommand::new);
            list.add(ConfigCommand::new);
            list.add(MoveHudCommand::new);
            list.add(ModeCommand::new);
        });
    }

}
