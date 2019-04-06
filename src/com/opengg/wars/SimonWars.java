package com.opengg.wars;

import com.opengg.core.GGInfo;
import com.opengg.core.engine.BindController;
import com.opengg.core.engine.GGApplication;
import com.opengg.core.engine.OpenGG;
import com.opengg.core.engine.Resource;
import com.opengg.core.gui.GUIController;
import com.opengg.core.gui.GUIProgressBar;
import com.opengg.core.gui.GUIText;
import com.opengg.core.io.ControlType;
import com.opengg.core.io.input.mouse.MouseButton;
import com.opengg.core.io.input.mouse.MouseButtonListener;
import com.opengg.core.io.input.mouse.MouseController;
import com.opengg.core.math.FastMath;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.network.NetworkEngine;
import com.opengg.core.render.text.Text;
import com.opengg.core.render.texture.Texture;
import com.opengg.core.render.window.WindowController;
import com.opengg.core.render.window.WindowInfo;
import com.opengg.core.world.Skybox;
import com.opengg.core.world.WorldEngine;
import com.opengg.core.world.components.FreeFlyComponent;
import com.opengg.core.world.components.ModelComponent;
import com.opengg.core.world.components.WorldObject;
import com.opengg.wars.components.GameObject;
import com.opengg.wars.components.Unit;
import com.opengg.wars.components.UserViewComponent;
import com.opengg.wars.game.Empire;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.opengg.core.io.input.keyboard.Key.*;

public class SimonWars extends GGApplication implements MouseButtonListener {
    public static final byte COMMAND_SEND_PACKET = 10;

    public static boolean[][] map = new boolean[512][512];
    public static int[][] blockers;

    public static boolean offline = true;
    public static List<GameObject> selected = new ArrayList<>();

    public static Empire.Side side = Empire.Side.RED;

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
        Empire.initialize();
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
            Models.init();
            WorldEngine.getCurrent().getRenderEnvironment().setSkybox(new Skybox(Texture.getSRGBCubemap(Resource.getTexturePath("skybox\\majestic_ft.png"),
                    Resource.getTexturePath("skybox\\majestic_bk.png"),
                    Resource.getTexturePath("skybox\\majestic_up.png"),
                    Resource.getTexturePath("skybox\\majestic_dn.png"),
                    Resource.getTexturePath("skybox\\majestic_rt.png"),
                    Resource.getTexturePath("skybox\\majestic_lf.png")), 1500f));
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

            var star = new AStar(512,512, new Node(500,2), new Node(500,110));
            star.setBlocks(blockers);
            var info = star.findPath().stream().map(n -> new Vector2f(n.getRow(), n.getCol())).collect(Collectors.toList());

            var unit = new Unit(Empire.Side.RED);
            unit.setPositionOffset(new Vector3f(info.get(0).x, 0, info.get(0).y));
            unit.setNewPath(info);

            WorldEngine.getCurrent().attach(new UserViewComponent());
            GhostComponent dragable = new GhostComponent();
            dragable.setModel(Resource.getModel("TheFactory"));
            WorldEngine.getCurrent().attach(dragable);

            WorldEngine.getCurrent().attach(unit);
            WorldEngine.getCurrent().attach(new ModelComponent(Models.factory).setPositionOffset(new Vector3f(20,600f,20)).setScaleOffset(new Vector3f(2f)));
            MouseController.onButtonPress(this);
            MouseController.onButtonPress(dragable);
        }
    }

    @Override
    public void render() {

    }

    @Override
    public void update(float delta) {
        if(GGInfo.isServer()){

        }else{
            GUISetup.updateResourceMenu();
            //CommandManager.sendAllCommands();
            //CommandManager.sendCommand(Command.create("yeeticus", "1"));
        }
    }

    @Override
    public void onButtonPress(int button) {
        if(button == MouseButton.LEFT){
            var ray = MouseController.getRay();
            var allfound = WorldEngine.getCurrent()
                    .getAllDescendants()
                    .stream()
                    .filter(c -> c instanceof GameObject)
                    .map(c -> (GameObject)c)
                    .filter(c -> c.getSide() == side)
                    .filter(c -> FastMath.closestPointTo(ray.getPos(), ray.getPos().add(ray.getDir()), c.getPosition(), false).distanceTo(c.getPosition()) < c.getColliderWidth())
                    .collect(Collectors.toList());

            selected.clear();
            if(allfound.isEmpty()){
                GUIController.deactivateGUI("unitGUI");
            }else{
                selected.addAll(allfound);
                if(allfound.size() == 1){
                    if(allfound.get(0) instanceof Unit){
                        var unit = (Unit) allfound.get(0);
                        GUIController.activateGUI("unitGUI");
                        GUISetup.updateUnitMenu(unit);
                    }
                }
            }
        }
        if(button == MouseButton.RIGHT){
            var ray = MouseController.getRay();
            var pos = FastMath.getRayPlaneIntersection(ray.getRay(), new Vector3f(0,1,0), new Vector3f(0,1,0));
            if(!SimonWars.selected.isEmpty()){
                if(selected.size() == 1){
                    if(selected.get(0) instanceof Unit){
                        var unit = (Unit) selected.get(0);
                        CommandManager.sendCommand(Command.create("unit_move", unit.getName()));
                        unit.calculateAndUsePath(pos.xz());
                    }
                }
            }
        }
    }

    @Override
    public void onButtonRelease(int button) {

    }
}
