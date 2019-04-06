package com.opengg.wars;

import com.opengg.core.gui.*;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.text.Text;

public class GUISetup {
    static GUI unitGUI;
    public static void initialize(){
        unitGUI = new GUI();
        unitGUI.addItem("background",new GUITexture(Textures.unitMenu,new Vector2f(0.8333f,0.5268f),new Vector2f(0.16666667f,0.47407f)));
        unitGUI.addItem("health",new GUIProgressBar(new Vector2f(0.85156f,0.82167f),new Vector2f(0.129675f,0.0185f),new Vector3f(1,0,0),new Vector3f(0.5f)));
        unitGUI.addItem("stats",new GUIText(Text.from("Health: 100/100 \n\n" +
                "Attack: 25 \n\n" + "Defense: 124").size(0.22f),Textures.dFont,new Vector2f(0.84156f,0.77167f)).setLayer(0.5f));
        GUIController.add(unitGUI,"unitGUI");
        GUIController.activateGUI("unitGUI");
    }
}
