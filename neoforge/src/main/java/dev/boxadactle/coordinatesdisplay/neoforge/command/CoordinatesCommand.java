package dev.boxadactle.coordinatesdisplay.neoforge.command;

import dev.boxadactle.boxlib.neoforge.command.BClientCommand;

public abstract class CoordinatesCommand extends BClientCommand {

    @Override
    public String getCommandName() {
        return "coordinates";
    }
}
