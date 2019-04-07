package com.opengg.wars;

import com.opengg.core.engine.Resource;
import com.opengg.core.gui.*;
import com.opengg.core.math.Tuple;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.text.Text;
import com.opengg.wars.components.Building;
import com.opengg.wars.components.ResourceProducer;
import com.opengg.wars.components.Unit;
import com.opengg.wars.components.UnitProducer;
import com.opengg.wars.game.Empire;
import com.opengg.wars.game.GameResource;

import java.util.List;

public class GUISetup {
    static GUI unitGUI,builderUI,townUI,resourceUI,barracksUI,factoryUI,mainResourceUI,unitSelect,borderGUI;

    public static void initialize(){
        borderGUI = new GUI();
        borderGUI.addItem("border", new GUITexture(Resource.getTexture("border.png"), new Vector2f(0,0), new Vector2f(1,1)));
        GUIController.add(borderGUI, "borderGUI");

        unitGUI = new GUI();
        unitSelect = new GUI();
        unitSelect.addItem("background",new GUITexture(Textures.builderMenu,new Vector2f(0.8333f,0f),new Vector2f(0.186667f,1)).setLayer(-0.6f));
        unitGUI.addItem("background",new GUITexture(Textures.unitMenu,new Vector2f(0.8333f,0.4768f),new Vector2f(0.16666667f,0.5207f)));
        unitGUI.addItem("health",new GUIProgressBar(new Vector2f(0.85156f,0.82167f),new Vector2f(0.129675f,0.0185f),new Vector3f(1,0,0),new Vector3f(0.5f)));

        unitGUI.addItem("stats",new GUIText(Text.from("Enemy Here\n\n Health: 100/100 \n\n" +
                "Attack: 25 \n\n" + "Defense: 124").size(0.22f),Textures.dFont,new Vector2f(0.84156f,0.77167f)).setLayer(0.5f));
        GUIController.add(unitGUI,"unitGUI");

        builderUI = new GUI();
        builderUI.addItem("background",new GUITexture(Textures.builderMenu,new Vector2f(0.8333f,0f),new Vector2f(0.166667f,1)).setLayer(-1f));

        GUIGroup fgroup = new GUIGroup(new Vector2f(0.8334f,0.9056f));
        GUIButton factory = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        factory.setOnClick(() -> {
            if(Empire.get(SimonWars.side).getAvailable(GameResource.IRON) >= 150 && Empire.get(SimonWars.side).getAvailable(GameResource.WOOD) >= 80 && 0 < Empire.get(SimonWars.side).getAvailable(GameResource.PEOPLESLOT)) {
                SimonWars.dragable.enable(Building.BType.FACTORY);
            }
        });
        fgroup.addItem("factory",factory.setLayer(-0.6f));
        fgroup.addItem("name", new GUIText(Text.from("Factory\nIron:150\nWood:80").size(0.2f),Textures.dFont, new Vector2f(0.01f, 0.06f)));
        builderUI.addItem("fgroup",fgroup);
        GUIGroup mgroup = new GUIGroup(new Vector2f(0.8334f,0.8112f));
        GUIButton mines = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        mines.setOnClick(() -> {
            if(Empire.get(SimonWars.side).getAvailable(GameResource.IRON) >= 40 && Empire.get(SimonWars.side).getAvailable(GameResource.STONE) >= 100&& 0 < Empire.get(SimonWars.side).getAvailable(GameResource.PEOPLESLOT)) {
                SimonWars.dragable.enable(Building.BType.GOLDMINE);
            }
        });
        mgroup.addItem("mines",mines.setLayer(-0.6f));
        mgroup.addItem("name", new GUIText(Text.from("Gold Mine\nStone:100\nIron:40").size(0.2f),Textures.dFont, new Vector2f(0.01f, 0.06f)));
        builderUI.addItem("mgroup",mgroup);
        GUIGroup fagroup = new GUIGroup(new Vector2f(0.8334f,0.7168f));
        GUIButton farms = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        farms.setOnClick(() -> {
            if(Empire.get(SimonWars.side).getAvailable(GameResource.FOOD) >= 30 && Empire.get(SimonWars.side).getAvailable(GameResource.IRON) >= 30&& 0 < Empire.get(SimonWars.side).getAvailable(GameResource.PEOPLESLOT)) {
                SimonWars.dragable.enable(Building.BType.FARM);
            }
        });
        fagroup.addItem("farms",farms.setLayer(-0.6f));
        fagroup.addItem("name", new GUIText(Text.from("Farms\nIron:30\nWood:30").size(0.2f),Textures.dFont, new Vector2f(0.01f, 0.06f)));
        builderUI.addItem("magroup",fagroup);
        GUIGroup cgroup = new GUIGroup(new Vector2f(0.8334f,0.6224f));
        GUIButton camp = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        camp.setOnClick(() -> {
            if(Empire.get(SimonWars.side).getAvailable(GameResource.FOOD) >= 40 && Empire.get(SimonWars.side).getAvailable(GameResource.WOOD) >= 20&& 0< Empire.get(SimonWars.side).getAvailable(GameResource.PEOPLESLOT)) {
                SimonWars.dragable.enable(Building.BType.CAMP);
            }
        });
        cgroup.addItem("camps",camp.setLayer(-0.6f));
        cgroup.addItem("name", new GUIText(Text.from("Camps\nFood:40\nWood:20").size(0.2f),Textures.dFont, new Vector2f(0.01f, 0.06f)));
        builderUI.addItem("cgroup",cgroup);
        GUIGroup bgroup = new GUIGroup(new Vector2f(0.8334f,0.528f));
        GUIButton barracks = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        barracks.setOnClick(() -> {
            if(Empire.get(SimonWars.side).getAvailable(GameResource.IRON) >= 40 && Empire.get(SimonWars.side).getAvailable(GameResource.FOOD) >= 60) {
            SimonWars.dragable.enable(Building.BType.BARRACKS);
            }
        });
        bgroup.addItem("barracks",barracks.setLayer(-0.6f));
        bgroup.addItem("name", new GUIText(Text.from("Barracks\nIron:40\nFood:60").size(0.2f),Textures.dFont, new Vector2f(0.01f, 0.06f)));
        builderUI.addItem("bgroup",bgroup);
        GUIGroup blgroup = new GUIGroup(new Vector2f(0.8334f,0.4336f));
        GUIButton ironmine = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        ironmine.setOnClick(() -> {
            if(Empire.get(SimonWars.side).getAvailable(GameResource.WOOD) >= 70 && Empire.get(SimonWars.side).getAvailable(GameResource.STONE) >= 100&& 0 < Empire.get(SimonWars.side).getAvailable(GameResource.PEOPLESLOT)) {
            SimonWars.dragable.enable(Building.BType.IRONMINE);
            }
        });
        blgroup.addItem("ironmine",ironmine.setLayer(-0.6f));
        blgroup.addItem("name", new GUIText(Text.from("Iron Mine\nWood:70\nStone:100").size(0.2f),Textures.dFont, new Vector2f(0.01f, 0.06f)));
        builderUI.addItem("blgroup",blgroup);
        GUIGroup qlgroup = new GUIGroup(new Vector2f(0.8334f,0.3392f));
        GUIButton quarry = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        quarry.setOnClick(() -> {
            if(Empire.get(SimonWars.side).getAvailable(GameResource.WOOD) >= 40 && Empire.get(SimonWars.side).getAvailable(GameResource.IRON) >= 10&& 0 < Empire.get(SimonWars.side).getAvailable(GameResource.PEOPLESLOT)) {
                SimonWars.dragable.enable(Building.BType.QUARRY);
            }
        });
        qlgroup.addItem("quarry",quarry.setLayer(-0.6f));
        qlgroup.addItem("name", new GUIText(Text.from("Quarry\nWood:40\nIron:10").size(0.2f),Textures.dFont, new Vector2f(0.01f, 0.06f)));
        builderUI.addItem("qlgroup",qlgroup);
        GUIGroup tgroup = new GUIGroup(new Vector2f(0.8334f,0.2448f));
        GUIButton town = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        town.setOnClick(() -> {
            if(Empire.get(SimonWars.side).getAvailable(GameResource.ENTERTAINMENT) >= 40 && Empire.get(SimonWars.side).getAvailable(GameResource.WOOD) >= 150) {
                SimonWars.dragable.enable(Building.BType.TOWN);
            }
        });
        tgroup.addItem("town",town.setLayer(-0.6f));
        tgroup.addItem("name", new GUIText(Text.from("Town\nEntertain:40\nWood:150").size(0.2f),Textures.dFont, new Vector2f(0.01f, 0.06f)));
        builderUI.addItem("tgroup",tgroup);
        GUIController.add(builderUI,"builderGUI");
        initTown();
        initRGetterGUI();
        initBarracksGUI();
        //initFactoryGUI();
        initResourceGUI();
    }
    public static void initTown(){
        townUI = new GUI();
        townUI.addItem("townText", new GUIText(Text.from("Town Menu").size(0.3f),Textures.dFont,new Vector2f(0.8433f,0.9468f)));
        townUI.addItem("background",new GUITexture(Textures.builderMenu,new Vector2f(0.8333f,0f),new Vector2f(0.186667f,1)).setLayer(-0.6f));
        townUI.addItem("townStats", new GUIText(Text.from("Free Men: 100\n\n"+"Open Slots:100\n\n"+"Max Slots: 120\n\n"+"Gen Rate: 5s").size(0.2f),Textures.dFont,new Vector2f(0.8433f,0.9068f)));
        GUIGroup worker = new GUIGroup(new Vector2f(0.835f,0.1f));
        GUIButton button = new GUIButton(new Vector2f(),new Vector2f(0.1625f,0.0944f),Textures.button);
        worker.addItem("makeWorker",button);
        worker.addItem("text",new GUIText(Text.from("Make Worker").size(0.2f),Textures.dFont,new Vector2f(0.02f,0.053f)));
        townUI.addItem("buttonGroup",worker);
        GUIController.add(townUI,"townGUI");

    }
    public static void initRGetterGUI(){
        resourceUI = new GUI();
        resourceUI.addItem("text", new GUIText(Text.from("Type Here\n\n" + "Iron: 10/s\n\n"+"Health: 100/100\n\nDefense: 100\n\nP-Defense: 100").size(0.2f),Textures.dFont,new Vector2f(0.8433f,0.9468f)));
        resourceUI.addItem("background",new GUITexture(Textures.builderMenu,new Vector2f(0.8333f,0f),new Vector2f(0.186667f,1)).setLayer(-0.6f));
        resourceUI.addItem("health",new GUIProgressBar(new Vector2f(0.85156f,0.52167f),new Vector2f(0.129675f,0.0185f),new Vector3f(1,0,0),new Vector3f(0.5f)));
        resourceUI.addItem("icon",new GUITexture(Textures.foodIcon,new Vector2f(0.853f,0.568f),new Vector2f(0.122667f,0.122667f)));
        GUIController.add(resourceUI,"resourceGUI");
    }
    public static void initBarracksGUI(){
        barracksUI = new GUI();
        barracksUI.addItem("background",new GUITexture(Textures.builderMenu,new Vector2f(0.8333f,0f),new Vector2f(0.186667f,1)).setLayer(-1f));
        GUIGroup fgroup = new GUIGroup(new Vector2f(0.8334f,1f));
        GUIButton factory = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        fgroup.addItem("infantry",factory.setLayer(-0.55f));
        fgroup.addItem("name", new GUIText(Text.from("Infantry\nCoal:1000\nPSlots:1000").size(0.2f),Textures.dFont,new Vector2f(0.01f,-0.032f)));
        barracksUI.addItem("fgroup",fgroup);
        GUIGroup mgroup = new GUIGroup(new Vector2f(0.8334f,0.9056f));
        GUIButton mines = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        mgroup.addItem("cavalry",mines.setLayer(-0.55f));
        mgroup.addItem("name", new GUIText(Text.from("Cavalry\nCoal:1000\nPSlots:1000").size(0.2f),Textures.dFont,new Vector2f(0.01f,-0.032f)));
        barracksUI.addItem("mgroup",mgroup);
        GUIGroup fagroup = new GUIGroup(new Vector2f(0.8334f,0.8112f));
        GUIButton farms = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        fagroup.addItem("archer",farms.setLayer(-0.55f));
        fagroup.addItem("name", new GUIText(Text.from("Archer\nCoal:1000\nPSlots:1000").size(0.2f),Textures.dFont,new Vector2f(0.01f,-0.032f)));
        barracksUI.addItem("mgroup",fagroup);
        GUIController.add(barracksUI,"barracksGUI");
    }
    public static void initFactoryGUI(){
        factoryUI = new GUI();
        factoryUI.addItem("background",new GUITexture(Textures.builderMenu,new Vector2f(0.8333f,0f),new Vector2f(0.186667f,1)).setLayer(-1f));
        GUIGroup fgroup = new GUIGroup(new Vector2f(0.8334f,1f));
        GUIButton factory = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        fgroup.addItem("steel",factory.setLayer(-0.55f));
        fgroup.addItem("name", new GUIText(Text.from("Steel\nCoal:1000\nPSlots:1000").size(0.2f),Textures.dFont,new Vector2f(0.01f,-0.032f)));
        factoryUI.addItem("fgroup",fgroup);
        GUIGroup mgroup = new GUIGroup(new Vector2f(0.8334f,0.9056f));
        GUIButton mines = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        mgroup.addItem("luxury",mines.setLayer(-0.55f));
        mgroup.addItem("name", new GUIText(Text.from("Luxury\nCoal:1000\nPSlots:1000").size(0.2f),Textures.dFont,new Vector2f(0.01f,-0.032f)));
        factoryUI.addItem("mgroup",mgroup);
        GUIController.add(factoryUI,"factoryGUI");
    }
    public static void initResourceGUI(){
        mainResourceUI = new GUI();
        mainResourceUI.addItem("iron",new GUITexture(Textures.ironIcon,new Vector2f(0,0.9537f),new Vector2f(0.02604f,0.0462f)));
        mainResourceUI.addItem("ironText", new GUIText(Text.from("12000").size(0.2f),Textures.dFont,new Vector2f(0.02604f,0.975f)));
        mainResourceUI.addItem("gold",new GUITexture(Textures.goldIcon,new Vector2f(0.08046875f,0.9537f),new Vector2f(0.02604f,0.0462f)));
        mainResourceUI.addItem("goldText", new GUIText(Text.from("12000").size(0.2f),Textures.dFont,new Vector2f(0.11484375f,0.975f)));
        mainResourceUI.addItem("stone",new GUITexture(Textures.stoneIcon,new Vector2f(0.16953126f,0.9537f),new Vector2f(0.02604f,0.0462f)));
        mainResourceUI.addItem("stoneText", new GUIText(Text.from("12000").size(0.2f),Textures.dFont,new Vector2f(0.20390625f,0.975f)));
        mainResourceUI.addItem("food",new GUITexture(Textures.foodIcon,new Vector2f(0.26171875f,0.9537f),new Vector2f(0.02604f,0.0462f)));
        mainResourceUI.addItem("foodText", new GUIText(Text.from("12000").size(0.2f),Textures.dFont,new Vector2f(0.29140624f,0.975f)));
        mainResourceUI.addItem("wood",new GUITexture(Textures.woodIcon,new Vector2f(0.34765625f,0.9537f),new Vector2f(0.02604f,0.0462f)));
        mainResourceUI.addItem("woodText", new GUIText(Text.from("12000").size(0.2f),Textures.dFont,new Vector2f(0.384375f,0.975f)));
        mainResourceUI.addItem("people",new GUITexture(Textures.peopleIcon,new Vector2f(0.44453126f,0.9537f),new Vector2f(0.02604f,0.0462f)));
        mainResourceUI.addItem("peopleText", new GUIText(Text.from("1200/1200  Available People: 21").size(0.2f),Textures.dFont,new Vector2f(0.4703125f,0.975f)));

        mainResourceUI.addItem("steel",new GUITexture(Textures.steelIcon,new Vector2f(0,0.8937f),new Vector2f(0.02604f,0.0462f)));
        mainResourceUI.addItem("steelText", new GUIText(Text.from("12000").size(0.2f),Textures.dFont,new Vector2f(0.02604f,0.895f)));
        mainResourceUI.addItem("luxury",new GUITexture(Textures.luxuryIcon,new Vector2f(0.08046875f,0.8937f),new Vector2f(0.02604f,0.0462f)));
        mainResourceUI.addItem("luxuryText", new GUIText(Text.from("12000").size(0.2f),Textures.dFont,new Vector2f(0.11484375f,0.895f)));
        mainResourceUI.addItem("energy",new GUITexture(Textures.energyIcon,new Vector2f(0.16953126f,0.8937f),new Vector2f(0.02604f,0.0462f)));
        mainResourceUI.addItem("energyText", new GUIText(Text.from("12000/s").size(0.2f),Textures.dFont,new Vector2f(0.20390625f,0.895f)));

        GUIController.add(mainResourceUI,"mainResourceGUI");
        GUIController.activateGUI("mainResourceGUI");
    }
    public static void updateResourceMenu(){
        Empire e = Empire.get(SimonWars.side);
        ((GUIText)(mainResourceUI.getRoot().getItem("ironText"))).setText(Integer.toString(e.getAvailable(GameResource.IRON)));
        ((GUIText)(mainResourceUI.getRoot().getItem("goldText"))).setText(Integer.toString(e.getAvailable(GameResource.GOLD)));
        ((GUIText)(mainResourceUI.getRoot().getItem("stoneText"))).setText(Integer.toString(e.getAvailable(GameResource.STONE)));
        ((GUIText)(mainResourceUI.getRoot().getItem("foodText"))).setText(Integer.toString(e.getAvailable(GameResource.FOOD)));
        ((GUIText)(mainResourceUI.getRoot().getItem("woodText"))).setText(Integer.toString(e.getAvailable(GameResource.WOOD)));
        ((GUIText)(mainResourceUI.getRoot().getItem("peopleText"))).setText(e.getAvailable(GameResource.PEOPLESLOT) + "/" +e.populationSlots+
                " Available People: "+Integer.toString(e.getAvailable(GameResource.PEOPLE)));
        ((GUIText)(mainResourceUI.getRoot().getItem("steelText"))).setText(Integer.toString(e.getAvailable(GameResource.STEEL)));
        ((GUIText)(mainResourceUI.getRoot().getItem("luxuryText"))).setText(Integer.toString(e.getAvailable(GameResource.ENTERTAINMENT))+"/s");
        ((GUIText)(mainResourceUI.getRoot().getItem("energyText"))).setText(Integer.toString(e.getAvailable(GameResource.ENERGY))+"/s");

    }
    public static void updateUnitMenu(Unit unit){
        ((GUIProgressBar)unitGUI.getRoot().getItem("health")).setPercent((float)unit.getHealth()/unit.getMaxhealth());
        ((GUIText)unitGUI.getRoot().getItem("stats")).setText(unit.getVisibleName()+"\n\nHealth: "+unit.getHealth()+"/"+unit.getMaxhealth()+"\n\nAttack: "
        +unit.getAttack().attack+"\n\nP-Attack: " +unit.getAttack().pierceAttack+"\n\nRange: "+unit.getAttack().range
        +"\n\nDefense: "+unit.getArmor()+"\n\nP-Armor: "+unit.getPierceArmor());
    }
    public static GUI getFactoryGUI(ResourceProducer producer){
        GUI newGUI = new GUI();
        newGUI.addItem("background",new GUITexture(Textures.builderMenu,new Vector2f(0.8333f,0f),new Vector2f(0.186667f,1)).setLayer(-1f));
        int index = 0;
        for (Tuple<List<Tuple<GameResource, Integer>>, Tuple<GameResource, Integer>> product : producer.products) {
            GUIGroup group = new GUIGroup(new Vector2f(0.8334f, 0.9056f - (index * 0.0944f)));
            GUIButton factory = new GUIButton(new Vector2f(0, 0), new Vector2f(0.1625f, 0.0944f), Textures.button);
            String resourceList = "";
            for (Tuple<GameResource, Integer> resource : product.getFirst()) {
                resourceList += (resource.x.name() + ": " + resource.y + "\n");
            }
            group.addItem("name", new GUIText(Text.from(product.y.x.name() + "\n" + resourceList).size(0.2f), Textures.dFont, new Vector2f(0.01f, 0.06f)));
            group.addItem("button", factory);
            final int temp = index;
            factory.setOnClick(() -> {
                Empire e = Empire.get(SimonWars.side);
                for (Tuple<GameResource, Integer> resource : product.getFirst()) {
                    if (e.getAvailable(resource.x) < resource.y) return;
                }
                producer.selected = temp;
            });
            newGUI.addItem(Integer.toString(index), group);
            index++;
        }
        return newGUI;
    }

    public static GUI updateUnitProducer(UnitProducer producer) {
        GUI newGUI = new GUI();
        newGUI.addItem("background",new GUITexture(Textures.builderMenu,new Vector2f(0.8333f,0f),new Vector2f(0.186667f,1)).setLayer(-1f));
        int index = 0;
        for (Tuple<List<Tuple<GameResource, Integer>>, Unit.UType> product : producer.unitCreations) {
            GUIGroup group = new GUIGroup(new Vector2f(0.8334f, 0.9056f - (index * 0.0944f)));
            GUIButton factory = new GUIButton(new Vector2f(0, 0), new Vector2f(0.1625f, 0.0944f), Textures.button);
            factory.setLayer(-0.5f);
            String resourceList = "";
            for (Tuple<GameResource, Integer> resource : product.getFirst()) {
                resourceList += (resource.x.name() + ": " + resource.y + "\n");
            }
            group.addItem("name", new GUIText(Text.from(product.y.name() + "\n" + resourceList).size(0.2f), Textures.dFont, new Vector2f(0.01f, 0.06f)));
            group.addItem("button", factory);
            factory.setOnClick(() -> {
                Empire e = Empire.get(SimonWars.side);
                for (Tuple<GameResource, Integer> resource : product.getFirst()) {
                    if (e.getAvailable(resource.x) < resource.y) return;
                }
                CommandManager.sendCommand(Command.create("unit_order", product.y.toString(), String.valueOf(producer.getId())));
            });
            newGUI.addItem(Integer.toString(index), group);
            index++;
        }
        newGUI.addItem("health",new GUIProgressBar(new Vector2f(0.85156f,0.1567f),new Vector2f(0.129675f,0.0185f),new Vector3f(1,0,0),new Vector3f(0.5f)));
        newGUI.addItem("progress",new GUIProgressBar(new Vector2f(0.85156f,0.0567f),new Vector2f(0.129675f,0.0185f),new Vector3f(0,0.2f,1),new Vector3f(0.5f)));
        return newGUI;
    }
    public static void updateUnitSelect(List<Unit> units) {
        unitSelect.getRoot().clear();
        String tokenizer = "";
        for (Unit unit : units) {
            tokenizer += unit.getName() + "\n" + unit.health + "/" + unit.getMaxhealth() + "\n";
        }
        unitSelect.getRoot().addItem("text", new GUIText(Text.from(tokenizer).size(0.2f), Textures.dFont, new Vector2f(0.851f, 0.9f)));
    }
}
