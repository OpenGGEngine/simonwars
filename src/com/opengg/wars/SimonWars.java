package com.opengg.wars;

import com.opengg.core.GGInfo;
import com.opengg.core.engine.GGApplication;
import com.opengg.core.engine.OpenGG;
import com.opengg.core.engine.Resource;
import com.opengg.core.network.NetworkEngine;
import com.opengg.core.render.texture.Texture;
import com.opengg.core.render.window.WindowInfo;
import com.opengg.core.world.Skybox;
import com.opengg.core.world.WorldEngine;
import com.opengg.core.world.components.WorldObject;

public class SimonWars extends GGApplication{
    public static final byte COMMAND_SEND_PACKET = 10;

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
        if(GGInfo.isServer()){
                NetworkEngine.initializeServer("yeeticus", 25565);
                CommandManager.initialize();

                WorldEngine.getCurrent().attach(new WorldObject());

                WorldEngine.getCurrent().getRenderEnvironment().setSkybox(new Skybox(Texture.getSRGBCubemap(Resource.getTexturePath("skybox\\majestic_ft.png"),
                        Resource.getTexturePath("skybox\\majestic_bk.png"),
                        Resource.getTexturePath("skybox\\majestic_up.png"),
                        Resource.getTexturePath("skybox\\majestic_dn.png"),
                        Resource.getTexturePath("skybox\\majestic_rt.png"),
                        Resource.getTexturePath("skybox\\majestic_lf.png")), 1500f));

        }else{
            NetworkEngine.connect("localhost", 25565);

            Textures.loadTextures();
        }
    }

    @Override
    public void render() {

    }

    @Override
    public void update(float delta) {
        if(GGInfo.isServer()){

        }else{
            CommandManager.sendAllCommands();
            CommandManager.sendCommand(Command.create("yeeticus", "1"));
        }
    }
}
