package com.opengg.wars;

import com.opengg.core.math.FastMath;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.world.WorldEngine;
import com.opengg.core.world.components.ResourceUser;
import com.opengg.wars.components.*;
import com.opengg.wars.game.Empire;
import com.opengg.wars.game.GameResource;

public class CommandParser {
    public static void initialize(){
        CommandManager.registerCommandUser("unit_move", CommandParser::parseUnitMove);
        CommandManager.registerCommandUser("dropoff_set", CommandParser::parseDropoffPointSet);
        CommandManager.registerCommandUser("building_create", CommandParser::parseBuildingCreate);
        CommandManager.registerCommandUser("unit_order", CommandParser::parseUnitOrder);
        CommandManager.registerCommandUser("unit_attack", CommandParser::parseUnitAttack);
        CommandManager.registerCommandUser("factory_set", CommandParser::parseFactorySet);
    }

    public static void parseUnitMove(Command unit){
        var unitId = Integer.parseInt(unit.args.get(0));
        var unitComp = (Unit)WorldEngine.getCurrent().find(unitId);
        if(unitComp == null) return;
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
        var side = Empire.Side.valueOf(buildingC.args.get(2));
        var loc = Vector2f.parseVector2f(buildingC.args.get(1));

        var buildingComp = Building.create(building, side);
        switch (building){
            case FACTORY:
                Empire.get(side).use(GameResource.IRON,150);
                Empire.get(side).use(GameResource.WOOD,80);
                Empire.get(side).occupiedSlots++;
                break;
            case GOLDMINE:
                Empire.get(side).use(GameResource.IRON,40);
                Empire.get(side).use(GameResource.STONE,100);
                Empire.get(side).occupiedSlots++;
                break;
            case QUARRY:
                Empire.get(side).use(GameResource.WOOD,40);
                Empire.get(side).use(GameResource.IRON,10);
                Empire.get(side).occupiedSlots++;
                break;
            case TOWN:
                Empire.get(side).use(GameResource.ENTERTAINMENT,40);
                Empire.get(side).use(GameResource.WOOD,150);
                break;
            case IRONMINE:
                Empire.get(side).use(GameResource.WOOD,70);
                Empire.get(side).use(GameResource.STONE,100);
                Empire.get(side).occupiedSlots++;
                break;
            case CAMP:
                Empire.get(side).use(GameResource.FOOD,40);
                Empire.get(side).use(GameResource.WOOD,20);
                Empire.get(side).occupiedSlots++;
                break;
            case BARRACKS:
                Empire.get(side).use(GameResource.IRON,40);
                Empire.get(side).use(GameResource.FOOD,60);
                break;
            case FARM:
                Empire.get(side).use(GameResource.IRON,30);
                Empire.get(side).use(GameResource.WOOD,30);
                Empire.get(side).occupiedSlots++;
                break;
        }
        buildingComp.setPositionOffset(new Vector3f(loc.x, 0, loc.y));
        WorldEngine.getCurrent().attach(buildingComp);
    }

    public static void parseUnitOrder(Command order){
        var building = (UnitProducer)WorldEngine.getCurrent().find(Integer.parseInt(order.args.get(1)));
        var unit = Unit.UType.valueOf(order.args.get(0));

        building.spawnUnit(unit);
    }

    public static void parseFactorySet(Command order){
        var building = (ResourceProducer)WorldEngine.getCurrent().find(Integer.parseInt(order.args.get(0)));
        var temp = Integer.parseInt(order.args.get(1));
        building.selected = temp;
    }
}
