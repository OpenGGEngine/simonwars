package com.opengg.wars.components;

import com.opengg.core.GGInfo;
import com.opengg.core.engine.OpenGG;
import com.opengg.core.math.Tuple;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.util.GGInputStream;
import com.opengg.core.util.GGOutputStream;
import com.opengg.core.world.WorldEngine;
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

    public void spawnUnit(Unit.UType type){
        if(dropoffPoint == null) dropoffPoint = this.getPosition().add(new Vector3f(5,0,0)).xz();
        var data = unitCreations.stream().filter(c -> c.y == type).findFirst().get();
        var canMake = data.x.stream()
                .allMatch(p -> Empire.get(this.side).getAvailable(p.x) > p.y);
        canMake = canMake && Empire.get(this.side).populations - Empire.get(this.side).occupiedSlots > 0;
        if(canMake) {
            data.x.forEach(p -> Empire.get(this.side).use(p.x, p.y));
            Empire.get(this.side).populations--;
            unitQueue.add(type);
        }
    }

    @Override
    public void update(float delta)  {
        if(GGInfo.isServer() || SimonWars.offline) {
            Empire.get(side).use(GameResource.ENTERTAINMENT,2);
            if(!unitQueue.isEmpty()){
                progress += delta * Empire.get(side).getAvailable(GameResource.ENTERTAINMENT) > 60?15: 10;
                if(progress > 100){
                    progress = 0;
                    var unitType = unitQueue.get(0);
                    unitQueue.remove(0);

                    var unit = Unit.spawn(unitType, this.side);
                    unit.setPositionOffset(this.getPositionOffset().add(new Vector3f(3,0,0)));
                    unit.calculateAndUsePath(dropoffPoint);
                    OpenGG.asyncExec(() -> WorldEngine.getCurrent().attach(unit));
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
        var size = in.readInt();
        for(int i = 0; i < size; i++){
            var create = in.readString();
            var priceAmount = in.readInt();
            var amount = new ArrayList<Tuple<GameResource, Integer>>();
            for(int j = 0; j < priceAmount; j++){
                amount.add(Tuple.of(GameResource.valueOf(in.readString()), in.readInt()));
            }

            unitCreations.add(Tuple.of(amount, Unit.UType.valueOf(create)));
        }
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
