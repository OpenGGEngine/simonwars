package com.opengg.wars.components;

import com.opengg.core.math.Tuple;
import com.opengg.core.math.Vector2f;
import com.opengg.wars.game.Empire;
import com.opengg.wars.game.GameResource;

import java.util.ArrayList;
import java.util.List;

public class UnitProducer extends Building{
    Vector2f dropoffPoint;

    public List<Tuple<List<Tuple<GameResource, Integer>>, Unit.UType>> unitCreations = new ArrayList<>();

    public UnitProducer(){

    }

    public UnitProducer(Empire.Side side){
        super(side);
    }

    public void addUnit(Unit.UType unit, Tuple<GameResource, Integer>... price){
        unitCreations.add(Tuple.of(List.of(price), unit));
    }
}
