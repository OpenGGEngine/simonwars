package com.opengg.wars.components;

import com.opengg.core.math.Tuple;
import com.opengg.core.math.Vector3f;
import com.opengg.core.util.GGInputStream;
import com.opengg.core.util.GGOutputStream;
import com.opengg.core.world.components.ModelComponent;
import com.opengg.wars.Models;
import com.opengg.wars.SimonWars;
import com.opengg.wars.game.Empire;
import com.opengg.wars.game.GameResource;

import java.io.IOException;

public class Building extends GameObject{
    String menuOnClick;
    boolean complete = false;

    public enum BType{
        FACTORY,IRONMINE,GOLDMINE,QUARRY,CAMP,BARRACKS,FARM,TOWN
    }

    public Building(){

    }

    public Building(Empire.Side side) {
        super(side);
    }

    public static Building create(BType type, Empire.Side side) {
        switch (type){
            case CAMP:
                var camp = new ResourceProducer(side);
                camp.addOutput(GameResource.WOOD, 5);
                camp.attach(new ModelComponent(Models.forestCamp).setScaleOffset(0.009f));
                return camp;
            case IRONMINE:
                var ironmine = new ResourceProducer(side);
                ironmine.addOutput(GameResource.IRON, 5);
                ironmine.attach(new ModelComponent(Models.mine).setScaleOffset(0.1f));
                return ironmine;
            case GOLDMINE:
                var goldmine = new ResourceProducer(side);
                goldmine.addOutput(GameResource.GOLD, 2);
                goldmine.attach(new ModelComponent(Models.mine).setScaleOffset(0.1f));
                return goldmine;
            case QUARRY:
                var quarry = new ResourceProducer(side);
                quarry.addOutput(GameResource.STONE, 5);
                quarry.attach(new ModelComponent(Models.mine)).setScaleOffset(0.1f);
                return quarry;
            case FARM:
                var farm = new ResourceProducer(side);
                farm.addOutput(GameResource.FOOD, 5);
                farm.attach(new ModelComponent(Models.farm).setRotationOffset(new Vector3f(0,0,-90)));
                return farm;
            case FACTORY:
                var factory = new ResourceProducer(side);
                factory.addOutput(GameResource.STEEL, 2, Tuple.of(GameResource.IRON, 5));
                factory.addOutput(GameResource.ENTERTAINMENT,1,Tuple.of(GameResource.GOLD,5));
                factory.addOutput(GameResource.NONE,0,Tuple.of(GameResource.NONE,0));
                factory.attach(new ModelComponent(Models.factory));
                return factory;
            case BARRACKS:
                var barracks = new UnitProducer(side);
                barracks.addUnit(Unit.UType.INFANTRY, Tuple.of(GameResource.STONE, 25), Tuple.of(GameResource.FOOD, 20));
                barracks.addUnit(Unit.UType.CAVALRY, Tuple.of(GameResource.STEEL, 20), Tuple.of(GameResource.FOOD, 12));
                barracks.addUnit(Unit.UType.ARCHER, Tuple.of(GameResource.STONE, 25), Tuple.of(GameResource.WOOD, 45));
                barracks.addUnit(Unit.UType.MASTERMOLE, Tuple.of(GameResource.ENTERTAINMENT, 25), Tuple.of(GameResource.STEEL, 45));
                barracks.attach(new ModelComponent(Models.barrack).setScaleOffset(0.02f).setRotationOffset(new Vector3f(90,0,0)));
                return barracks;
            case TOWN:
                var town = new Town(side);
                town.attach(new ModelComponent(Models.house).setRotationOffset(new Vector3f(270,180,180)).setScaleOffset(0.5f));
                return town;
        }
        return null;
    }

    @Override
    public void kill() {
        super.kill();
    }

    @Override
    public void serialize(GGOutputStream out) throws IOException {
        super.serialize(out);
        out.write(complete);
    }

    @Override
    public void serializeUpdate(GGOutputStream out) throws IOException{
        super.serializeUpdate(out);
    }

    @Override
    public void deserialize(GGInputStream in) throws IOException {
        super.deserialize(in);
        complete = in.readBoolean();
    }

    @Override
    public void deserializeUpdate(GGInputStream in, float delta) throws IOException{
        super.deserializeUpdate(in, delta);
    }
}
