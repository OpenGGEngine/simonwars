package com.opengg.wars.components;

import com.opengg.core.math.Tuple;
import com.opengg.core.util.GGInputStream;
import com.opengg.core.util.GGOutputStream;
import com.opengg.wars.game.Empire;
import com.opengg.wars.game.GameResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResourceProducer extends Building{
    public List<Tuple<List<Tuple<GameResource, Integer>>, Tuple<GameResource, Integer>>> products = new ArrayList<>();


    boolean outSet = false;
    boolean inSet = false;

    public ResourceProducer(){

    }

    public ResourceProducer(Empire.Side side) {
        super(side);
    }

    public void addOutput(GameResource out, int amount, Tuple<GameResource, Integer>... inputs){
        products.add(Tuple.of(List.of(inputs), Tuple.of(out, amount)));
    }


    @Override
    public void update(float delta){

    }

    @Override
    public void serialize(GGOutputStream out) throws IOException {
        super.serialize(out);
        out.write(products.size());

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
