package com.opengg.wars;

import com.opengg.core.math.FastMath;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.world.WorldEngine;
import com.opengg.wars.components.Building;
import com.opengg.wars.components.GameObject;
import com.opengg.wars.components.Unit;
import com.opengg.wars.components.UnitProducer;
import com.opengg.wars.game.Empire;

public class CommandParser {
    public static void initialize(){
        CommandManager.registerCommandUser("unit_move", CommandParser::parseUnitMove);
        CommandManager.registerCommandUser("dropoff_set", CommandParser::parseDropoffPointSet);
        CommandManager.registerCommandUser("building_create", CommandParser::parseBuildingCreate);
        CommandManager.registerCommandUser("unit_order", CommandParser::parseUnitOrder);
        CommandManager.registerCommandUser("unit_attack", CommandParser::parseUnitAttack);
    }

    public static void parseUnitMove(Command unit){
        var unitId = Integer.parseInt(unit.args.get(0));
        var unitComp = (Unit)WorldEngine.getCurrent().find(unitId);
        var newLoc = Vector2f.parseVector2f(unit.args.get(1));
        unitComp.target = null;
        if(SimonWars.map[FastMath.clamp((int) newLoc.x,0,SimonWars.map[0].length-1)] [FastMath.clamp((int) newLoc.y,0,SimonWars.map.length-1)]);
            unitComp.calculateAndUsePath(newLoc);
    }

    public static void parseUnitAttack(Command unit){
        var unitId = Integer.parseInt(unit.args.get(0));
        var unitComp = (Unit)WorldEngine.getCurrent().find(unitId);
        var targetId = Integer.parseInt(unit.args.get(1));
        var target = (GameObject)WorldEngine.getCurrent().find(targetId);
        unitComp.target = target;
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
        var loc = Vector2f.parseVector2f(buildingC.args.get(1));

        var buildingComp = Building.create(building, type);
        buildingComp.setPositionOffset(new Vector3f(loc.x, 0, loc.y));
        WorldEngine.getCurrent().attach(buildingComp);
    }

    public static void parseUnitOrder(Command order){
        var building = (UnitProducer)WorldEngine.getCurrent().find(Integer.parseInt(order.args.get(1)));
        var unit = Unit.UType.valueOf(order.args.get(0));

        building.spawnUnit(unit);
    }
}
