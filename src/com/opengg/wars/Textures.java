package com.opengg.wars;

import com.opengg.core.engine.Resource;
import com.opengg.core.render.text.Font;
import com.opengg.core.render.texture.Texture;

public class Textures {
    public static Texture waterIcon;static Texture ironIcon;static Texture goldIcon;static Texture stoneIcon;static Texture peopleIcon;static Texture woodIcon;static Texture foodIcon;
    public static Texture unitMenu,builderMenu;
    public static Texture button;
    public static Font dFont;
    public static void loadTextures(){
        dFont = Resource.getTruetypeFont("font.ttf");
        waterIcon = Resource.getTexture("icons/water.png");ironIcon = Resource.getTexture("icons/iron.png");goldIcon = Resource.getTexture("icons/gold.png");
        stoneIcon = Resource.getTexture("icons/stone.png");peopleIcon = Resource.getTexture("icons/people.png");woodIcon = Resource.getTexture("icons/wood.png");
        foodIcon = Resource.getTexture("icons/food.png");
        unitMenu = Resource.getTexture("unitmenu.png");builderMenu = Resource.getTexture("builder.png");button = Resource.getTexture("button.png");
    }
}
