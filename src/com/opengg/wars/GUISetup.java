package com.opengg.wars;

import com.opengg.core.gui.*;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.text.Text;

public class GUISetup {
    static GUI unitGUI,builderUI,townUI,resourceUI,barracksUI,factoryUI,mainResourceUI;
    public static void initialize(){
        unitGUI = new GUI();
        unitGUI.addItem("background",new GUITexture(Textures.unitMenu,new Vector2f(0.8333f,0.5268f),new Vector2f(0.16666667f,0.47407f)));
        unitGUI.addItem("health",new GUIProgressBar(new Vector2f(0.85156f,0.82167f),new Vector2f(0.129675f,0.0185f),new Vector3f(1,0,0),new Vector3f(0.5f)));
        unitGUI.addItem("stats",new GUIText(Text.from("Health: 100/100 \n\n" +
                "Attack: 25 \n\n" + "Defense: 124").size(0.22f),Textures.dFont,new Vector2f(0.84156f,0.77167f)).setLayer(0.5f));
        GUIController.add(unitGUI,"unitGUI");
        builderUI = new GUI();
        builderUI.addItem("background",new GUITexture(Textures.builderMenu,new Vector2f(0.8333f,0f),new Vector2f(0.166667f,1)).setLayer(-1f));

        GUIGroup fgroup = new GUIGroup(new Vector2f(0.8334f,1f));
        GUIButton factory = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        fgroup.addItem("factory",factory.setLayer(-0.6f));
        fgroup.addItem("name", new GUIText(Text.from("Factory\nCoal:1000\nPSlots:1000").size(0.2f),Textures.dFont,new Vector2f(0.01f,-0.032f)));
        builderUI.addItem("fgroup",fgroup);
        GUIGroup mgroup = new GUIGroup(new Vector2f(0.8334f,0.9056f));
        GUIButton mines = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        mgroup.addItem("mines",mines.setLayer(-0.6f));
        mgroup.addItem("name", new GUIText(Text.from("Mines\nCoal:1000\nPSlots:1000").size(0.2f),Textures.dFont,new Vector2f(0.01f,-0.032f)));
        builderUI.addItem("mgroup",mgroup);
        GUIGroup fagroup = new GUIGroup(new Vector2f(0.8334f,0.8112f));
        GUIButton farms = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        fagroup.addItem("farms",farms.setLayer(-0.6f));
        fagroup.addItem("name", new GUIText(Text.from("Farms\nCoal:1000\nPSlots:1000").size(0.2f),Textures.dFont,new Vector2f(0.01f,-0.032f)));
        builderUI.addItem("magroup",fagroup);
        GUIGroup cgroup = new GUIGroup(new Vector2f(0.8334f,0.7168f));
        GUIButton camp = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        cgroup.addItem("camps",camp.setLayer(-0.6f));
        cgroup.addItem("name", new GUIText(Text.from("Camps\nCoal:1000\nPSlots:1000").size(0.2f),Textures.dFont,new Vector2f(0.01f,-0.032f)));
        builderUI.addItem("cgroup",cgroup);
        GUIGroup bgroup = new GUIGroup(new Vector2f(0.8334f,0.6224f));
        GUIButton barracks = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        bgroup.addItem("barracks",barracks.setLayer(-0.6f));
        bgroup.addItem("name", new GUIText(Text.from("Barracks\nCoal:1000\nPSlots:1000").size(0.2f),Textures.dFont,new Vector2f(0.01f,-0.032f)));
        builderUI.addItem("bgroup",bgroup);
        GUIController.add(builderUI,"builderGUI");
        initTown();
        initRGetterGUI();
        initBarracksGUI();
        initFactoryGUI();
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
        resourceUI.addItem("text", new GUIText(Text.from("Type Here\n\n" + "Iron: 10/s").size(0.3f),Textures.dFont,new Vector2f(0.8433f,0.9468f)));
        resourceUI.addItem("background",new GUITexture(Textures.builderMenu,new Vector2f(0.8333f,0f),new Vector2f(0.186667f,1)).setLayer(-0.6f));
        resourceUI.addItem("icon",new GUITexture(Textures.foodIcon,new Vector2f(0.853f,0.568f),new Vector2f(0.122667f,0.122667f)));
        GUIController.add(resourceUI,"resourceGUI");
        //GUIController.activateGUI("resourceGUI");
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


        GUIController.add(mainResourceUI,"mainResourceGUI");
        GUIController.activateGUI("mainResourceGUI");
    }
}
