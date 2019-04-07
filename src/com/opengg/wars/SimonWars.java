package com.opengg.wars;

import com.opengg.core.GGInfo;
import com.opengg.core.engine.BindController;
import com.opengg.core.engine.GGApplication;
import com.opengg.core.engine.OpenGG;
import com.opengg.core.engine.Resource;
import com.opengg.core.gui.GUI;
import com.opengg.core.gui.GUIController;
import com.opengg.core.gui.GUIProgressBar;
import com.opengg.core.gui.GUITexture;
import com.opengg.core.io.ControlType;
import com.opengg.core.io.input.mouse.MouseButton;
import com.opengg.core.io.input.mouse.MouseButtonListener;
import com.opengg.core.io.input.mouse.MouseController;
import com.opengg.core.math.*;
import com.opengg.core.math.geom.Ray;
import com.opengg.core.network.NetworkEngine;
import com.opengg.core.network.Packet;
import com.opengg.core.render.texture.Texture;
import com.opengg.core.render.window.Window;
import com.opengg.core.render.window.WindowController;
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
import static com.opengg.core.math.FastMath.PI;

public class SimonWars extends GGApplication implements MouseButtonListener {
    public static final byte COMMAND_SEND_PACKET = 10;
    public static final byte EMPIRE_SEND_PACKET = 11;

    public static boolean[][] map = new boolean[192][192];
    public static int[][] blockers;
    public static List<Tuple<Vector2i, Deposit>> deposits = new ArrayList<>();

    public static boolean offline = true;
    public static List<GameObject> selected = new ArrayList<>();
    public static GUI currentSelection;

    public static Empire.Side side = Empire.Side.RED;
    public static GhostComponent dragable;
    public static float pressTimer = 0;

    public static float empireTimer = 0;
    private Vector2f screenPos1;

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
                unit.setPositionOffset(new Vector3f(180, 0, 5));
                unit.calculateAndUsePath(unit.getPosition().xz());
                WorldEngine.getCurrent().attach(unit);

                var unit2 = Unit.spawn(Unit.UType.ARCHER, Empire.Side.RED);
                unit2.setPositionOffset(new Vector3f(180, 0, 10));
                unit2.calculateAndUsePath(unit2.getPosition().xz());
                WorldEngine.getCurrent().attach(unit2);


                var unit3 = Unit.spawn(Unit.UType.INFANTRY, Empire.Side.BLUE);
                unit3.setPositionOffset(new Vector3f(180, 0, 20));
                unit3.calculateAndUsePath(unit3.getPosition().xz());
                WorldEngine.getCurrent().attach(unit3);
            }else{
                MapGenerator.generateFromMaps();
                NetworkEngine.connect("10.56.90.42", 25565);
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
            BindController.addBind(ControlType.KEYBOARD, "esc", KEY_ESCAPE);
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

            if(screenPos1 != null){
                pressTimer += delta;
                GUIController.activateGUI("borderGUI");
                var gui = GUIController.get("borderGUI");
                var img = (GUITexture)gui.getRoot().getItem("border");

                var pos2 = MouseController.get().divide(new Vector2f(WindowController.getWidth(), WindowController.getHeight()));

                var lowX = screenPos1.x < pos2.x ? screenPos1.x : pos2.x;
                var lowY = 1-screenPos1.y < 1-pos2.y ? 1-screenPos1.y : 1-pos2.y;

                var highX = screenPos1.x >= pos2.x ? screenPos1.x : pos2.x;
                var highY = 1-screenPos1.y >= 1-pos2.y ? 1-screenPos1.y : 1-pos2.y;


                img.setPositionOffset(new Vector2f(lowX, lowY));
                img.setSize(new Vector2f(highX, highY).subtract(new Vector2f(lowX, lowY)));
            }else{
                GUIController.deactivateGUI("borderGUI");
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
                    if (allfound.size() == 1) {
                        if (allfound.get(0) instanceof Villager) {
                            var unit = (Villager) allfound.get(0);
                            GUIController.activateGUI("builderGUI");
                            currentSelection = GUIController.get("builderGUI");
                        } else if (allfound.get(0) instanceof Unit) {
                            var unit = (Unit) allfound.get(0);
                            GUIController.activateGUI("unitGUI");
                            currentSelection = GUIController.get("unitGUI");
                            GUISetup.updateUnitMenu(unit);
                        } else if (allfound.get(0) instanceof UnitProducer) {
                            currentSelection = GUISetup.updateUnitProducer((UnitProducer) allfound.get(0));
                            GUIController.addAndUse(currentSelection, "unitProd");
                        } else if (allfound.get(0) instanceof ResourceProducer) {
                            currentSelection = GUISetup.getFactoryGUI((ResourceProducer) allfound.get(0));
                            GUIController.addAndUse(currentSelection, "producer");
                        }
                    }
                } else {
                    screenPos1 = MouseController.get().divide(new Vector2f(WindowController.getWidth(), WindowController.getHeight()));
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
                }else{
                    var allUnits = selected.stream().filter(c -> c instanceof Unit).map(c -> (Unit)c).collect(Collectors.toList());

                    var allfound = WorldEngine.getCurrent()
                            .getAllDescendants()
                            .stream()
                            .filter(c -> c instanceof GameObject)
                            .map(c -> (GameObject)c)
                            .filter(c -> c.getSide() != side)
                            .filter(c -> FastMath.closestPointTo(ray.getPos(), ray.getPos().add(ray.getDir()), c.getPosition(), false).distanceTo(c.getPosition()) < c.getColliderWidth())
                            .collect(Collectors.toList());

                    if(!allfound.isEmpty()){
                        for(var unit : allUnits){
                            CommandManager.sendCommand(Command.create("unit_attack", Integer.toString(unit.getId()), Integer.toString(allfound.get(0).getId())));
                        }
                    }else{
                        for(var unit : allUnits){
                            float a = (float) (Math.random() * 2 * PI);
                            float r = (float) (allUnits.size()/2 * Math.sqrt(Math.random()));
                            float x = r * FastMath.cos(a);
                            float y = r *  FastMath.sin(a);
                            var newPos = pos.add(new Vector3f(x,0,y));
                            CommandManager.sendCommand(Command.create("unit_move", Integer.toString(unit.getId()), newPos.x + "," + newPos.z));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onButtonRelease(int button) {
        if(button == MouseButton.LEFT){
            if(screenPos1 != null && pressTimer > 0.2f){

                var pos1 = screenPos1.multiply(new Vector2f(WindowController.getWidth(), WindowController.getHeight()));
                var pos2 = MouseController.get();

                var lowX = pos1.x < pos2.x ? pos1.x : pos2.x;
                var lowY = pos1.y < pos2.y ? pos1.y : pos2.y;

                var highX = pos1.x >= pos2.x ? pos1.x : pos2.x;
                var highY = pos1.y >= pos2.y ? pos1.y : pos2.y;

                var intersection1 = FastMath.getRayPlaneIntersection(MouseController.getRay(lowX, lowY).getRay(), new Vector3f(0,1,0), new Vector3f(0,1,0));
                var intersection2 = FastMath.getRayPlaneIntersection(MouseController.getRay(highX, lowY).getRay(), new Vector3f(0,1,0), new Vector3f(0,1,0));

                var intersection3 = FastMath.getRayPlaneIntersection(MouseController.getRay(lowX, highY).getRay(), new Vector3f(0,1,0), new Vector3f(0,1,0));
                var intersection4 = FastMath.getRayPlaneIntersection(MouseController.getRay(highX, highY).getRay(), new Vector3f(0,1,0), new Vector3f(0,1,0));

                var allfound = WorldEngine.getCurrent()
                        .getAllDescendants()
                        .stream()
                        .filter(c -> c instanceof GameObject)
                        .map(c -> (GameObject)c)
                        .filter(c -> c.getSide() == side)
                        .filter(c -> FastMath.isPointInPolygon(c.getPosition().xz(), List.of(intersection1.xz(), intersection2.xz(), intersection3.xz(), intersection4.xz())))
                        .collect(Collectors.toList());

                selected.clear();
                if(!allfound.isEmpty())
                {
                    selected.addAll(allfound);
                }

            }
            pressTimer = 0;
            screenPos1 = null;

        }
    }
}
