package com.opengg.wars.components;

import com.opengg.core.math.Tuple;
import com.opengg.wars.game.Empire;
import com.opengg.wars.game.GameResource;

import java.util.ArrayList;
import java.util.List;

public class ResourceProducer extends Building{
    List<Tuple<GameResource, Integer>> products = new ArrayList<>();
    List<Tuple<GameResource, Integer>> inputs = new ArrayList<>();

    boolean outSet = false;
    boolean inSet = false;

    public ResourceProducer(){

    }

    public ResourceProducer(Empire.Side side) {
        super(side);
    }

    public void addOutput(GameResource out, int amount){
        products.add(Tuple.of(out, amount));
    }

    public void addInput(GameResource in, int amount){
        inputs.add(Tuple.of(in, amount));
    }

    @Override
    public void update(float delta){

    }
}
