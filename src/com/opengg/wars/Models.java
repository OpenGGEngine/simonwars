package com.opengg.wars;

import com.opengg.core.engine.Resource;
import com.opengg.core.model.Model;

public class Models {
    static Model stone;
    public static void init(){
        stone = Resource.getModel("");
    }
}
