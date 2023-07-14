package dev.boxadactle.coordinatesdisplay.command;

import dev.boxadactle.boxlib.command.BClientCommand;

public abstract class CoordinatesCommand extends BClientCommand {
    @Override
    public String getCommandName() {
        return "coordinates";
    }
}
