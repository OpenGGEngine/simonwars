package com.opengg.wars;

import com.opengg.core.engine.GGApplication;
import com.opengg.core.engine.OpenGG;
import com.opengg.core.render.window.WindowInfo;

public class SimonWars extends GGApplication{

    public static void main(String... args){
        if(args.length > 0 && args[0].equals("server")){
            OpenGG.initializeHeadless(new SimonWars());
        }else{
            OpenGG.initialize(new SimonWars(), new WindowInfo()
                    .setWidth(1920)
                    .setHeight(1080)
                    .setVsync(true)
                    .setName("Simon Wars"));
        }
    }

    @Override
    public void setup() {

    }

    @Override
    public void render() {

    }

    @Override
    public void update(float delta) {

    }
}
