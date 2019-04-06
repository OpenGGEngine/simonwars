package com.opengg.wars;

import com.opengg.core.engine.Resource;
import com.opengg.core.model.Model;

public class Models {
    static Model stone;
    static Model factory;
    public static void init(){
        factory = Resource.getModel("TheFactory");
        //stone = Resource.getModel("");
    }
}
