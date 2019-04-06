package com.opengg.wars;

import com.opengg.core.math.Vector2f;
import com.opengg.core.world.WorldEngine;
import com.opengg.wars.components.Unit;

public class CommandParser {
    public static void initialize(){
        CommandManager.registerCommandUser("unit_move", CommandParser::parseUnitMove);
    }

    public static void parseUnitMove(Command unit){
        var unitId = Integer.parseInt(unit.args.get(0));
        var unitComp = (Unit)WorldEngine.getCurrent().find(unitId);
        var newLoc = Vector2f.parseVector2f(unit.args.get(1));
        if(SimonWars.map[(int) newLoc.x] [(int) newLoc.y])
            unitComp.calculateAndUsePath(newLoc);
    }
}
