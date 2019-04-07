package com.opengg.wars;

import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.world.WorldEngine;
import com.opengg.wars.components.Building;
import com.opengg.wars.components.Unit;
import com.opengg.wars.components.UnitProducer;
import com.opengg.wars.game.Empire;

public class CommandParser {
    public static void initialize(){
        CommandManager.registerCommandUser("unit_move", CommandParser::parseUnitMove);
        CommandManager.registerCommandUser("dropoff_set", CommandParser::parseDropoffPointSet);
        CommandManager.registerCommandUser("building_create", CommandParser::parseBuildingCreate);
        CommandManager.registerCommandUser("unit_order", CommandParser::parseUnitOrder);
    }

    public static void parseUnitMove(Command unit){
        var unitId = Integer.parseInt(unit.args.get(0));
        var unitComp = (Unit)WorldEngine.getCurrent().find(unitId);
        var newLoc = Vector2f.parseVector2f(unit.args.get(1));
        if(SimonWars.map[(int) newLoc.x] [(int) newLoc.y])
            unitComp.calculateAndUsePath(newLoc);
    }

    public static void parseDropoffPointSet(Command point){
        var prodId = Integer.parseInt(point.args.get(0));
        var prodComp = (UnitProducer)WorldEngine.getCurrent().find(prodId);
        var newLoc = Vector2f.parseVector2f(point.args.get(1));
        if(SimonWars.map[(int) newLoc.x] [(int) newLoc.y])
            prodComp.setDropoffPoint(newLoc);
    }

    public static void parseBuildingCreate(Command buildingC){
        var building = Building.BType.valueOf(buildingC.args.get(0));
        var type = Empire.Side.valueOf(buildingC.args.get(2));
        System.out.println(type);
        var loc = Vector2f.parseVector2f(buildingC.args.get(1));

        var buildingComp = Building.create(building, type);
        buildingComp.setPositionOffset(new Vector3f(loc.x, 0, loc.y));
        WorldEngine.getCurrent().attach(buildingComp);
        System.out.println(buildingComp.getSide());
    }

    public static void parseUnitOrder(Command order){
        var building = (UnitProducer)WorldEngine.getCurrent().find(Integer.parseInt(order.args.get(1)));
        var unit = Unit.UType.valueOf(order.args.get(0));

        building.spawnUnit(unit);
    }
}
