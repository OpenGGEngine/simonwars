package com.opengg.wars;

import com.opengg.core.gui.*;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.text.Text;

public class GUISetup {
    static GUI unitGUI,builderUI,townUI,resourceUI;
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
        GUIController.activateGUI("builderGUI");
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
}
