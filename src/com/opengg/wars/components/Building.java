package com.opengg.wars.components;

import com.opengg.wars.game.Empire;

public class Building extends GameObject{
    public enum BType{
        FACTORY,MINE,CAMP,BARRACKS,TOWN
    }
    public Building(Empire.Side side) {
        super(side);
    }
}
