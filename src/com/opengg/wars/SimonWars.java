package com.opengg.wars;

import com.opengg.core.GGInfo;
import com.opengg.core.engine.GGApplication;
import com.opengg.core.engine.OpenGG;
import com.opengg.core.render.window.WindowInfo;

public class SimonWars extends GGApplication{

    public static void main(String... args){
        if(args.length > 0 && args[0].equals("server")){
            OpenGG.initializeHeadless(new SimonWars());
        }else{
            OpenGG.initialize(new SimonWars(), new WindowInfo()
                    .setWidth(1280)
                    .setHeight(720)
                    .setVsync(true)
                    .setName("Simon Wars"));
        }
    }

    @Override
    public void setup() {
        if(GGInfo.isServer()){

        }else{
            Textures.loadTextures();
            GUISetup.initialize();

        }
    }

    @Override
    public void render() {

    }

    @Override
    public void update(float delta) {
        if(GGInfo.isServer()){

        }else{

        }
    }
}
