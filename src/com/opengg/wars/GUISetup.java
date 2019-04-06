package com.opengg.wars;

import com.opengg.core.gui.*;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.text.Text;

public class GUISetup {
    static GUI unitGUI,builderUI,townUI;
    public static void initialize(){
        unitGUI = new GUI();
        unitGUI.addItem("background",new GUITexture(Textures.unitMenu,new Vector2f(0.8333f,0.5268f),new Vector2f(0.16666667f,0.47407f)));
        unitGUI.addItem("health",new GUIProgressBar(new Vector2f(0.85156f,0.82167f),new Vector2f(0.129675f,0.0185f),new Vector3f(1,0,0),new Vector3f(0.5f)));
        unitGUI.addItem("stats",new GUIText(Text.from("Health: 100/100 \n\n" +
                "Attack: 25 \n\n" + "Defense: 124").size(0.22f),Textures.dFont,new Vector2f(0.84156f,0.77167f)).setLayer(0.5f));
        GUIController.add(unitGUI,"unitGUI");
        builderUI = new GUI();
        builderUI.addItem("background",new GUITexture(Textures.builderMenu,new Vector2f(0.8333f,0f),new Vector2f(0.166667f,1)).setLayer(-0.6f));

        GUIGroup fgroup = new GUIGroup(new Vector2f(0.8334f,1f));
        GUIButton factory = new GUIButton(new Vector2f(0,0),new Vector2f(),Textures.button);
        fgroup.addItem("factory",factory);
        builderUI.addItem("fgroup",fgroup);
        GUIGroup sgroup = new GUIGroup(new Vector2f(0.8334f,0.9056f));
        GUIButton storage = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        sgroup.addItem("storage",storage);
        builderUI.addItem("sgroup",sgroup);
        GUIGroup cgroup = new GUIGroup(new Vector2f(0.8334f,0.8112f));
        GUIButton coalplant = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        cgroup.addItem("coalp",coalplant);
        builderUI.addItem("cgroup",cgroup);
        GUIGroup bgroup = new GUIGroup(new Vector2f(0.8334f,0.7168f));
        GUIButton barracks = new GUIButton(new Vector2f(0,0),new Vector2f(0.1625f,0.0944f),Textures.button);
        bgroup.addItem("barracks",barracks);
        builderUI.addItem("bgroup",bgroup);
        GUIController.add(builderUI,"builderGUI");
        initTown();
        //GUIController.activateGUI("builderGUI");
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
        GUIController.activateGUI("townGUI");

    }
}
