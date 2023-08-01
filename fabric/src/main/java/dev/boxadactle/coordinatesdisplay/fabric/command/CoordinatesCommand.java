package dev.boxadactle.coordinatesdisplay.fabric.command;

import dev.boxadactle.boxlib.fabric.command.BClientCommand;

public abstract class CoordinatesCommand extends BClientCommand {

    @Override
    public String getCommandName() {
        return "coordinates";
    }
}
