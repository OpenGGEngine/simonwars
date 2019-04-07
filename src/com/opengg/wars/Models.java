package com.opengg.wars;

import com.opengg.core.engine.Resource;
import com.opengg.core.model.Model;

public class Models {
    public static Model stone;
    public static Model factory;
    public static Model house;
    public static Model mine;
    public static Model gold;
    public static Model iron;
    public static Model forest;
    public static Model forestCamp;
    public static Model farm;
    public static Model barrack;
    public static void init(){
        factory = Resource.getModel("TheFactory");
        stone = Resource.getModel("Rock_6");
        gold = Resource.getModel("Gold");
        iron = Resource.getModel("Iron");
        house = Resource.getModel("cottage");
        mine = Resource.getModel("Bull");
        forest = Resource.getModel("Tree");
        forestCamp = Resource.getModel("Logcamp");
        farm = Resource.getModel("tractor");
        barrack = Resource.getModel("Barracks");
    }
}
