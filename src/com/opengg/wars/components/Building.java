package com.opengg.wars.components;

import com.opengg.core.math.Tuple;
import com.opengg.core.util.GGInputStream;
import com.opengg.core.util.GGOutputStream;
import com.opengg.core.world.components.ModelComponent;
import com.opengg.wars.Models;
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
                return camp;
            case IRONMINE:
                var ironmine = new ResourceProducer(side);
                ironmine.addOutput(GameResource.IRON, 5);
                return ironmine;
            case GOLDMINE:
                var goldmine = new ResourceProducer(side);
                goldmine.addOutput(GameResource.GOLD, 2);
                return goldmine;
            case QUARRY:
                var quarry = new ResourceProducer(side);
                quarry.addOutput(GameResource.STONE, 5);
                return quarry;
            case FARM:
                var farm = new ResourceProducer(side);
                farm.addOutput(GameResource.FOOD, 5);
                return farm;
            case FACTORY:
                var factory = new ResourceProducer(side);
                factory.addOutput(GameResource.STEEL, 5, Tuple.of(GameResource.IRON, 5));
                factory.attach(new ModelComponent(Models.factory));
                return factory;
            case BARRACKS:
                var barracks = new UnitProducer();
                barracks.addUnit(Unit.UType.INFANTRY, Tuple.of(GameResource.STONE, 5), Tuple.of(GameResource.FOOD, 6));
                barracks.addUnit(Unit.UType.CAVALRY, Tuple.of(GameResource.WOOD, 8), Tuple.of(GameResource.FOOD, 12));
                barracks.addUnit(Unit.UType.ARCHER, Tuple.of(GameResource.STONE, 9), Tuple.of(GameResource.WOOD, 9));
                return barracks;
        }
        return null;
    }

    @Override
    public void serialize(GGOutputStream out) throws IOException {
        super.serialize(out);
        out.write(complete);
        out.write(menuOnClick);
    }

    @Override
    public void serializeUpdate(GGOutputStream out) throws IOException{
        super.serializeUpdate(out);
    }

    @Override
    public void deserialize(GGInputStream in) throws IOException {
        super.deserialize(in);
        complete = in.readBoolean();
        menuOnClick = in.readString();
    }

    @Override
    public void deserializeUpdate(GGInputStream in, float delta) throws IOException{
        super.deserializeUpdate(in, delta);
    }
}
