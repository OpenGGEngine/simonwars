package com.opengg.wars.components;

import com.opengg.wars.game.Empire;
import com.opengg.wars.game.GameResource;

public class ResourceProducer extends Building{
    GameResource output;
    int amount;

    GameResource input;
    int inamount;

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

    public void setInput(GameResource in, int amount){
        this.input = in;
        this.inamount = amount;
    }

    @Override
    public void update(float delta){
        

        if(output == GameResource.ENERGY || output == GameResource.ENTERTAINMENT){

        }else{

        }
    }
}
