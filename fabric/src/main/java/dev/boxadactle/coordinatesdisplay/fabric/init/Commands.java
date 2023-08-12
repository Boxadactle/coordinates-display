package dev.boxadactle.coordinatesdisplay.fabric.init;

import dev.boxadactle.boxlib.fabric.command.BCommandManager;
import dev.boxadactle.coordinatesdisplay.fabric.command.*;
import net.minecraft.network.chat.Component;

import java.util.List;

public class Commands {

    public static void register() {
        BCommandManager.registerCommand("coordinates", list -> {
            list.add(ConfigCommand::new);
            list.add(GuiCommand::new);
            list.add(HelpCommand::new);
            list.add(ModeCommand::new);
            list.add(MoveHudCommand::new);
            list.add(PositionCommand::new);
            list.add(ToggleCommand::new);
        });
    }

}
