package dev.boxadactle.coordinatesdisplay.init;

import dev.boxadactle.boxlib.command.BCommandManager;
import dev.boxadactle.coordinatesdisplay.command.*;

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
