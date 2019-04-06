package com.opengg.wars;

import com.opengg.core.engine.Resource;
import com.opengg.core.render.texture.Texture;

public class Textures {
    static Texture waterIcon;static Texture ironIcon;static Texture goldIcon;static Texture coalIcon;static Texture peopleIcon;static Texture woodIcon;static Texture foodIcon;
    public static void loadTextures(){
        waterIcon = Resource.getTexture("icons/water.png");ironIcon = Resource.getTexture("icons/iron.png");goldIcon = Resource.getTexture("icons/gold.png");
        coalIcon = Resource.getTexture("icons/coal.png");peopleIcon = Resource.getTexture("icons/people.png");woodIcon = Resource.getTexture("icons/wood.png");
        foodIcon = Resource.getTexture("icons/food.png");
    }
}
