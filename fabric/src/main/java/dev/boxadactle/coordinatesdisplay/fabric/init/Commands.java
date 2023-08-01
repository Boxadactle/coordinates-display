package dev.boxadactle.coordinatesdisplay.fabric.init;

import dev.boxadactle.boxlib.fabric.command.BCommandManager;
import dev.boxadactle.coordinatesdisplay.fabric.command.*;

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
