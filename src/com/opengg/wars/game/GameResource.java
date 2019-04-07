package com.opengg.wars.game;

import com.opengg.core.engine.Resource;

public enum GameResource {
    PEOPLE(Resource.getTexturePath("person.png")),
    PEOPLESLOT("person.png"),
    FOOD(Resource.getTexturePath("food.png")),
    WOOD(Resource.getTexturePath("wood.png")),
    STONE(Resource.getTexturePath("stone.png")),
    IRON(Resource.getTexturePath("iron.png")),
    GOLD(Resource.getTexturePath("gold.png")),
    ENERGY(Resource.getTexturePath("energy.png")),
    STEEL(Resource.getTexturePath("steel.png")),
    ENTERTAINMENT(Resource.getTexturePath("entertainment.png")),
    NONE(Resource.getTexturePath("steel.png"));

    String path;

    GameResource(String path){
        this.path = path;
    }
}
