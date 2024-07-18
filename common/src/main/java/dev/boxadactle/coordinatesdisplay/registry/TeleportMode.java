package dev.boxadactle.coordinatesdisplay.registry;

import dev.boxadactle.coordinatesdisplay.ModUtil;
import dev.boxadactle.coordinatesdisplay.position.Position;

import java.util.function.Function;

public enum TeleportMode {

    EXECUTE(ModUtil::toExecuteCommand),
    TP(ModUtil::toTeleportCommand),
    BARITONE(ModUtil::toBaritoneCommand);

    final Function<Position, String> converter;

    TeleportMode(Function<Position, String> converter) {
        this.converter = converter;
    }

    public String toCommand(Position pos) {
        return converter.apply(pos);
    }

}
