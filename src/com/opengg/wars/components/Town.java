package com.opengg.wars.components;

import com.opengg.wars.SimonWars;
import com.opengg.wars.game.Empire;
import com.opengg.wars.game.GameResource;

public class Town extends Building {
    int regenRate = 300;
    private int counter = 0;
    public Town(Empire.Side side){
        super(side);
        Empire.get(SimonWars.side).add(GameResource.PEOPLE,6);
        Empire.get(SimonWars.side).add(GameResource.PEOPLESLOT,6);
    }
    public void update(float delta){
        regenRate = (Empire.get(SimonWars.side).getAvailable(GameResource.FOOD)<1)? 600:300;
        super.update(delta);
        if(counter % regenRate == 0){
            if( Empire.get(SimonWars.side).getAvailable(GameResource.PEOPLE) <Empire.get(SimonWars.side).getAvailable(GameResource.PEOPLESLOT) ){
                Empire.get(SimonWars.side).add(GameResource.PEOPLE,1);
            }else if(Empire.get(SimonWars.side).getAvailable(GameResource.PEOPLE) > Empire.get(SimonWars.side).getAvailable(GameResource.PEOPLESLOT)){
                Empire.get(SimonWars.side).use(GameResource.PEOPLE,Empire.get(SimonWars.side).getAvailable(GameResource.PEOPLE)- (Empire.get(SimonWars.side).getAvailable(GameResource.PEOPLESLOT)));
            }
        }
        if(counter % 60 == 0){
            Empire.get(SimonWars.side).use(GameResource.FOOD,-5*(Empire.get(SimonWars.side).getAvailable(GameResource.PEOPLESLOT)));
        }
        counter+=1;
    }
}
