package com.opengg.wars.components;

import com.opengg.core.GGInfo;
import com.opengg.core.util.GGInputStream;
import com.opengg.core.util.GGOutputStream;
import com.opengg.wars.SimonWars;
import com.opengg.wars.game.Empire;
import com.opengg.wars.game.GameResource;

import java.io.IOException;

public class Town extends Building {
    int regenRate = 300;
    private int counter = 0;

    public Town(){

    }

    public Town(Empire.Side side){
        super(side);
        Empire.get(side).add(GameResource.PEOPLESLOT,6);
    }
    public void update(float delta){
        if(GGInfo.isServer() || SimonWars.offline){
            regenRate = (Empire.get(side).getAvailable(GameResource.FOOD)<1)? 600:300;
            super.update(delta);
            if(counter % regenRate == 0){
                System.out.println(Empire.get(this.side).populations);
                System.out.println(Empire.get(this.side).populationSlots);

                if( Empire.get(this.side).populations < Empire.get(this.side).populationSlots ){
                    System.out.println("lkfds;lk");
                    System.out.println();
                    Empire.get(side).populations++;
                }else if(Empire.get(side).getAvailable(GameResource.PEOPLE) > Empire.get(side).getAvailable(GameResource.PEOPLESLOT)){
                    Empire.get(side).use(GameResource.PEOPLE,Empire.get(side).getAvailable(GameResource.PEOPLE) - (Empire.get(side).getAvailable(GameResource.PEOPLESLOT)));
                }
            }
            if(counter % 60 == 0){
                Empire.get(side).use(GameResource.FOOD, Empire.get(this.side).populations);
            }
            counter+=1;
        }
    }

    @Override
    public void deserialize(GGInputStream in) throws IOException {
        super.deserialize(in);

        Empire.get(this.side).add(GameResource.PEOPLESLOT,6);
    }
}
