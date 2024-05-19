package dev.boxadactle.coordinatesdisplay.neoforge.init;

import dev.boxadactle.boxlib.neoforge.command.BCommandManager;
import dev.boxadactle.coordinatesdisplay.neoforge.command.*;

public class Commands {
    public static void register() {
        BCommandManager.registerCommand("coordinates", list -> {
            list.add(ConfigCommand::new);
            list.add(CornerCommand::new);
            list.add(GuiCommand::new);
            list.add(HelpCommand::new);
            list.add(ModeCommand::new);
            list.add(MoveHudCommand::new);
            list.add(PositionCommand::new);
            list.add(ToggleCommand::new);
        });
    }
}
