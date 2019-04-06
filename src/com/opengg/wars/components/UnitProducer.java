package com.opengg.wars.components;

import com.opengg.core.GGInfo;
import com.opengg.core.math.Tuple;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.util.GGInputStream;
import com.opengg.core.util.GGOutputStream;
import com.opengg.wars.SimonWars;
import com.opengg.wars.game.Empire;
import com.opengg.wars.game.GameResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UnitProducer extends Building{
    Vector2f dropoffPoint;

    public List<Tuple<List<Tuple<GameResource, Integer>>, Unit.UType>> unitCreations = new ArrayList<>();

    public List<Unit.UType> unitQueue = new ArrayList<>();

    public float progress;

    public UnitProducer(){

    }

    public UnitProducer(Empire.Side side){
        super(side);
    }

    public void addUnit(Unit.UType unit, Tuple<GameResource, Integer>... price){
        unitCreations.add(Tuple.of(List.of(price), unit));
    }

    public void setDropoffPoint(Vector2f point){
        dropoffPoint = point;
    }

    @Override
    public void update(float delta)  {
        if(GGInfo.isServer() || SimonWars.offline) {
            if(!unitQueue.isEmpty()){
                progress += delta * 5;
                if(progress > 100){
                    progress = 0;
                    var unitType = unitQueue.get(0);
                    unitQueue.remove(0);

                    var unit = Unit.spawn(unitType, this.side);
                    unit.setPositionOffset(this.getPositionOffset().add(new Vector3f(3,0,0)));
                    unit.calculateAndUsePath(dropoffPoint);
                }
            }
        }
    }

    @Override
    public void serialize(GGOutputStream out) throws IOException {
        super.serialize(out);
        out.write(unitCreations.size());
        for(var creation : unitCreations){
            out.write(creation.y.toString());
            out.write(creation.x.size());
            for(var price : creation.x){
                out.write(price.x.toString());
                out.write(price.y);
            }
        }
    }

    @Override
    public void serializeUpdate(GGOutputStream out) throws IOException{
        super.serializeUpdate(out);
        out.write(progress);
        out.write((byte)unitQueue.size());
        for(var unit : unitQueue){
            out.write((byte)unit.ordinal());
        }
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
        progress = in.readFloat();
        unitQueue.clear();
        var size = in.readByte();
        for(int i = 0; i < size; i++){
            unitQueue.add(Unit.UType.values()[in.readByte()]);
        }
    }
}
