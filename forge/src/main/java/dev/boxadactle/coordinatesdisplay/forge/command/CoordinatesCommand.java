package dev.boxadactle.coordinatesdisplay.forge.command;

import dev.boxadactle.boxlib.forge.command.BClientCommand;

public abstract class CoordinatesCommand extends BClientCommand {

    @Override
    public String getCommandName() {
        return "coordinates";
    }
}
