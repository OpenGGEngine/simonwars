package com.opengg.wars;

import com.opengg.core.GGInfo;
import com.opengg.core.engine.BindController;
import com.opengg.core.engine.GGApplication;
import com.opengg.core.engine.OpenGG;
import com.opengg.core.engine.Resource;
import com.opengg.core.gui.GUI;
import com.opengg.core.gui.GUIController;
import com.opengg.core.gui.GUIProgressBar;
import com.opengg.core.io.ControlType;
import com.opengg.core.io.input.mouse.MouseButton;
import com.opengg.core.io.input.mouse.MouseButtonListener;
import com.opengg.core.io.input.mouse.MouseController;
import com.opengg.core.math.FastMath;
import com.opengg.core.math.Tuple;
import com.opengg.core.math.Vector2i;
import com.opengg.core.math.Vector3f;
import com.opengg.core.network.NetworkEngine;
import com.opengg.core.network.Packet;
import com.opengg.core.render.texture.Texture;
import com.opengg.core.render.window.WindowInfo;
import com.opengg.core.util.GGInputStream;
import com.opengg.core.util.GGOutputStream;
import com.opengg.core.world.Skybox;
import com.opengg.core.world.WorldEngine;
import com.opengg.core.world.components.ModelComponent;
import com.opengg.core.world.components.WorldObject;
import com.opengg.wars.components.*;
import com.opengg.wars.game.Deposit;
import com.opengg.wars.game.Empire;
import com.opengg.wars.game.GameResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.opengg.core.io.input.keyboard.Key.*;

public class SimonWars extends GGApplication implements MouseButtonListener {
    public static final byte COMMAND_SEND_PACKET = 10;
    public static final byte EMPIRE_SEND_PACKET = 11;

    public static boolean[][] map = new boolean[192][192];
    public static int[][] blockers;
    public static List<Tuple<Vector2i, Deposit>> deposits = new ArrayList<>();

    public static boolean offline = false;
    public static List<GameObject> selected = new ArrayList<>();
    public static GUI currentSelection;

    public static Empire.Side side = Empire.Side.RED;
    public static GhostComponent dragable;

    public static float empireTimer = 0;

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
        CommandParser.initialize();
        Empire.initialize();
        Models.init();

        if(GGInfo.isServer()){

            OpenGG.setTargetUpdateTime(1/20f);
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
            MapGenerator.generateFromMaps().forEach(c -> WorldEngine.getCurrent().attach(c));

            var unit = Unit.spawn(Unit.UType.WORKER, Empire.Side.RED);
            unit.setPositionOffset(new Vector3f(180, 0, 5));
            unit.calculateAndUsePath(unit.getPosition().xz());
            WorldEngine.getCurrent().attach(unit);


            var unit2 = Unit.spawn(Unit.UType.WORKER, Empire.Side.BLUE);
            unit2.setPositionOffset(new Vector3f(5, 0, 20));
            unit2.calculateAndUsePath(unit2.getPosition().xz());
            WorldEngine.getCurrent().attach(unit2);

            WorldEngine.getCurrent().attach(new UserViewComponent(0));
            WorldEngine.getCurrent().attach(new UserViewComponent(1));
        }else{

            if(offline){
                WorldEngine.getCurrent().getRenderEnvironment().setSkybox(new Skybox(Texture.getSRGBCubemap(Resource.getTexturePath("skybox\\majestic_ft.png"),
                        Resource.getTexturePath("skybox\\majestic_bk.png"),
                        Resource.getTexturePath("skybox\\majestic_up.png"),
                        Resource.getTexturePath("skybox\\majestic_dn.png"),
                        Resource.getTexturePath("skybox\\majestic_rt.png"),
                        Resource.getTexturePath("skybox\\majestic_lf.png")), 1500f));
                MapGenerator.generateFromMaps().forEach(c -> WorldEngine.getCurrent().attach(c));
                WorldEngine.getCurrent().attach(new UserViewComponent(0));

                var unit = Unit.spawn(Unit.UType.WORKER, Empire.Side.RED);
                unit.setPositionOffset(new Vector3f(5, 0, 5));
                unit.calculateAndUsePath(unit.getPosition().xz());
                WorldEngine.getCurrent().attach(unit);


                var unit2 = Unit.spawn(Unit.UType.WORKER, Empire.Side.BLUE);
                unit2.setPositionOffset(new Vector3f(180, 0, 20));
                unit2.calculateAndUsePath(unit2.getPosition().xz());
                WorldEngine.getCurrent().attach(unit2);
            }else{
                MapGenerator.generateFromMaps();
                NetworkEngine.connect("localhost", 25565);
                NetworkEngine.getReceiver().addProcessor(EMPIRE_SEND_PACKET, this::updateEmpires);
                side = GGInfo.getUserId() == 0 ? Empire.Side.RED : Empire.Side.BLUE;
            }

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

            dragable = new GhostComponent();
            dragable.setId(1293487023);
            dragable.setModel(Resource.getModel("cottage"));
            dragable.setEnabled(false);
            WorldEngine.getCurrent().attach(dragable);

            WorldEngine.getCurrent().attach(new ModelComponent(Models.factory).setPositionOffset(new Vector3f(20,600f,20)).setScaleOffset(new Vector3f(2f)));
            MouseController.onButtonPress(this);
            MouseController.onButtonPress(dragable);
        }
    }

    public void updateEmpires(Packet packet) {
        try {
            var in = new GGInputStream(packet.getData());
            for (int i = 0; i < 2; i++) {
                var empire = Empire.get(i == 0 ? Empire.Side.RED : Empire.Side.BLUE);

                empire.populations = in.readInt();
                empire.occupiedSlots = in.readInt();
                empire.populationSlots = in.readInt();
                empire.energyPerTick = in.readInt();
                empire.energyUsedPerTick = in.readInt();
                empire.entertainmentPerTick = in.readInt();
                empire.entertainmentUsedPerTick = in.readInt();
                empire.resources.put(GameResource.WOOD, in.readInt());
                empire.resources.put(GameResource.STONE, in.readInt());
                empire.resources.put(GameResource.STEEL, in.readInt());
                empire.resources.put(GameResource.IRON, in.readInt());
                empire.resources.put(GameResource.GOLD, in.readInt());
                empire.resources.put(GameResource.FOOD, in.readInt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendEmpires() {
        try {
            var out = new GGOutputStream();
            for (int i = 0; i < 2; i++) {
                var empire = Empire.get(i == 0 ? Empire.Side.RED : Empire.Side.BLUE);

                out.write(empire.populations);
                out.write(empire.occupiedSlots);
                out.write(empire.populationSlots);
                out.write(empire.energyPerTick);
                out.write(empire.energyUsedPerTick);
                out.write(empire.entertainmentPerTick);
                out.write(empire.entertainmentUsedPerTick);
                out.write(empire.resources.get(GameResource.WOOD));
                out.write(empire.resources.get(GameResource.STONE));
                out.write(empire.resources.get(GameResource.STEEL));
                out.write(empire.resources.get(GameResource.IRON));
                out.write(empire.resources.get(GameResource.GOLD));
                out.write(empire.resources.get(GameResource.FOOD));
            }
            for(var conn : NetworkEngine.getServer().getClients()){
                Packet.sendGuaranteed(NetworkEngine.getSocket(), EMPIRE_SEND_PACKET, out.asByteArray(), conn.getConnection());
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {

    }

    @Override
    public void update(float delta) {
        CommandManager.update();
        if(GGInfo.isServer()){
            empireTimer++;
            if(empireTimer > 1f/10f){
                empireTimer = 0;
                sendEmpires();
            }
        }else{
            if(SimonWars.selected.size()>0) {
                if (SimonWars.selected.get(0) instanceof UnitProducer && SimonWars.currentSelection != null) {
                    ((GUIProgressBar) SimonWars.currentSelection.getRoot().getItem("progress")).setPercent(((UnitProducer) SimonWars.selected.get(0)).progress/100);
                    ((GUIProgressBar) SimonWars.currentSelection.getRoot().getItem("health")).setPercent((float)((UnitProducer) SimonWars.selected.get(0)).health/SimonWars.selected.get(0).getHealth());
                }else if(SimonWars.selected.get(0) instanceof Unit){
                    GUISetup.updateUnitMenu((Unit)SimonWars.selected.get(0));
                }
            }
            GUISetup.updateResourceMenu();
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
            OpenGG.asyncExec(() -> {
            if(currentSelection != null){
                GUIController.deactivateGUI(currentSelection.getName());
            }

            if (!allfound.isEmpty()) {

                    selected.addAll(allfound);
                    if(allfound.size() == 1){
                        if(allfound.get(0) instanceof Villager){
                            var unit = (Villager) allfound.get(0);
                            GUIController.activateGUI("builderGUI");
                            currentSelection = GUIController.get("builderGUI");
                        }else if(allfound.get(0) instanceof Unit){
                            var unit = (Unit) allfound.get(0);
                            GUIController.activateGUI("unitGUI");
                            currentSelection = GUIController.get("unitGUI");
                            GUISetup.updateUnitMenu(unit);
                        }else if(allfound.get(0) instanceof UnitProducer){
                            currentSelection = GUISetup.updateUnitProducer((UnitProducer) allfound.get(0));
                            GUIController.addAndUse(currentSelection, "unitProd");
                        }else if(allfound.get(0) instanceof ResourceProducer){
                            currentSelection = GUISetup.getFactoryGUI((ResourceProducer) allfound.get(0));
                            GUIController.addAndUse(currentSelection, "producer");
                        }
                    }


            }
            });
        }

        if(button == MouseButton.RIGHT){
            var ray = MouseController.getRay();
            var pos = FastMath.getRayPlaneIntersection(ray.getRay(), new Vector3f(0,1,0), new Vector3f(0,1,0));
            if(!SimonWars.selected.isEmpty()){
                if(selected.size() == 1){
                    if(selected.get(0) instanceof Unit){
                        var unit = (Unit) selected.get(0);
                        var allfound = WorldEngine.getCurrent()
                                .getAllDescendants()
                                .stream()
                                .filter(c -> c instanceof GameObject)
                                .map(c -> (GameObject)c)
                                .filter(c -> c.getSide() != side)
                                .filter(c -> FastMath.closestPointTo(ray.getPos(), ray.getPos().add(ray.getDir()), c.getPosition(), false).distanceTo(c.getPosition()) < c.getColliderWidth())
                                .collect(Collectors.toList());

                        if(!allfound.isEmpty()){
                            CommandManager.sendCommand(Command.create("unit_attack", Integer.toString(unit.getId()), Integer.toString(allfound.get(0).getId())));
                        }else{
                            CommandManager.sendCommand(Command.create("unit_move", Integer.toString(unit.getId()), pos.x + "," + pos.z));

                        }

                    }else if(selected.get(0) instanceof UnitProducer){
                        var prod = (UnitProducer) selected.get(0);
                        CommandManager.sendCommand(Command.create("dropoff_set", Integer.toString(prod.getId()), pos.x + "," + pos.z));
                    }
                }
            }
        }
    }

    @Override
    public void onButtonRelease(int button) {

    }
}
