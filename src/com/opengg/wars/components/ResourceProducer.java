package com.opengg.wars.components;

import com.opengg.core.math.Tuple;
import com.opengg.wars.game.Empire;
import com.opengg.wars.game.GameResource;

import java.util.ArrayList;
import java.util.List;

public class ResourceProducer extends Building{
    GameResource output;
    int amount;

    List<Tuple<GameResource, Integer>> products = new ArrayList<>();

    boolean outSet = false;
    boolean inSet = false;

    public ResourceProducer(){

    }

    public ResourceProducer(Empire.Side side) {
        super(side);
    }

    public void setOutput(GameResource out, int amount){
        this.output = out;
        this.amount = amount;
    }

    public void addInput(GameResource in, int amount){
        products.add(Tuple.of(in, amount));
    }

    @Override
    public void update(float delta){

        if(output == GameResource.ENERGY || output == GameResource.ENTERTAINMENT){

        }else{

        }
    }
}
