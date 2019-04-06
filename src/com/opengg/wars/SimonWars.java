package com.opengg.wars;

import com.opengg.core.GGInfo;
import com.opengg.core.engine.BindController;
import com.opengg.core.engine.GGApplication;
import com.opengg.core.engine.OpenGG;
import com.opengg.core.engine.Resource;
import com.opengg.core.io.ControlType;
import com.opengg.core.network.NetworkEngine;
import com.opengg.core.render.texture.Texture;
import com.opengg.core.render.window.WindowInfo;
import com.opengg.core.world.Skybox;
import com.opengg.core.world.WorldEngine;
import com.opengg.core.world.components.FreeFlyComponent;
import com.opengg.core.world.components.WorldObject;

import java.util.Arrays;

import static com.opengg.core.io.input.keyboard.Key.*;

public class SimonWars extends GGApplication{
    public static final byte COMMAND_SEND_PACKET = 10;

    public static boolean[][] map = new boolean[512][512];
    public static int[][] blockers;

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
                MapGenerator.generateFromMaps().forEach(c -> WorldEngine.getCurrent().attach(c));
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
            WorldEngine.getCurrent().getRenderEnvironment().setSkybox(new Skybox(Texture.getSRGBCubemap(Resource.getTexturePath("skybox\\majestic_ft.png"),
                    Resource.getTexturePath("skybox\\majestic_bk.png"),
                    Resource.getTexturePath("skybox\\majestic_up.png"),
                    Resource.getTexturePath("skybox\\majestic_dn.png"),
                    Resource.getTexturePath("skybox\\majestic_rt.png"),
                    Resource.getTexturePath("skybox\\majestic_lf.png")), 1500f));
            WorldEngine.getCurrent().attach(new FreeFlyComponent());
            MapGenerator.generateFromMaps().forEach(c -> WorldEngine.getCurrent().attach(c));
            //NetworkEngine.connect("localhost", 25565);

            BindController.addBind(ControlType.KEYBOARD, "forward", KEY_W);
            BindController.addBind(ControlType.KEYBOARD, "backward", KEY_S);
            BindController.addBind(ControlType.KEYBOARD, "left", KEY_A);
            BindController.addBind(ControlType.KEYBOARD, "right", KEY_D);
            BindController.addBind(ControlType.KEYBOARD, "up", KEY_SPACE);
            BindController.addBind(ControlType.KEYBOARD, "down", KEY_LEFT_SHIFT);
            BindController.addBind(ControlType.KEYBOARD, "lookright", KEY_RIGHT);
            BindController.addBind(ControlType.KEYBOARD, "lookleft", KEY_LEFT);
            BindController.addBind(ControlType.KEYBOARD, "lookup", KEY_UP);
            BindController.addBind(ControlType.KEYBOARD, "lookdown", KEY_DOWN);
            BindController.addBind(ControlType.KEYBOARD, "aim", KEY_K);
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
            //CommandManager.sendAllCommands();
            //CommandManager.sendCommand(Command.create("yeeticus", "1"));
        }
    }
}
