package com.opengg.wars.game;

import com.opengg.core.math.FastMath;

import java.util.HashMap;

public class Empire {
    public static Empire RED;
    public static Empire BLUE;

    public HashMap<GameResource, Integer> resources = new HashMap<>();

    public int populationSlots = 0;
    public int occupiedSlots = 0;
    public int populations = 0;

    public int energyPerTick = 0;
    public int energyUsedPerTick = 0;
    public int entertainmentPerTick = 0;
    public int entertainmentUsedPerTick = 0;


    public static void initialize(){
        RED = new Empire();
        BLUE = new Empire();
    }

    public static Empire get(Side side){
        if(side == Side.RED) return RED;
        return BLUE;
    }

    public Empire(){
        resources.put(GameResource.FOOD, 100);
        resources.put(GameResource.GOLD, 50);
        resources.put(GameResource.WOOD, 200);
        resources.put(GameResource.STEEL, 0);
        resources.put(GameResource.STONE, 100);
        resources.put(GameResource.IRON, 100);
    }

    public int add(GameResource resource, int amount){
        if(resources.containsKey(resource))
            return resources.put(resource, resources.get(resource) + amount);
        if(resource == GameResource.PEOPLESLOT)
            populationSlots += amount;
        if(resource == GameResource.PEOPLE)
            populations = FastMath.clamp(populations + amount, 0, populationSlots);
        if(resource == GameResource.ENERGY)
            energyPerTick += amount;
        if(resource == GameResource.ENTERTAINMENT)
            entertainmentPerTick += amount;
    }

    public int getAvailable(GameResource resource)
    {
        if(resources.containsKey(resource))
            return resources.get(resource);
        if(resource == GameResource.PEOPLE)
            return Math.max(populations - occupiedSlots, 0);
        if(resource == GameResource.PEOPLESLOT)
            return Math.max(populationSlots - occupiedSlots, 0);
        if(resource == GameResource.ENTERTAINMENT)
            return entertainmentPerTick - entertainmentUsedPerTick;
        if(resource == GameResource.ENERGY)
            return energyPerTick - energyUsedPerTick;
        return 0;
    }

    public boolean use(GameResource resource, int amount) {
        if(resources.containsKey(resource)){
            if (resources.get(resource) > amount) {
                resources.put(resource, resources.get(resource) - amount);
                return true;
            }else{
                return false;
            }
        }

        if(resource == GameResource.PEOPLE){
            if(populations - amount >= occupiedSlots){
                populations -= amount;
                return true;
            }else{
                return false;
            }
        }

        if(resource == GameResource.PEOPLESLOT){
            if(populationSlots - amount >= occupiedSlots){
                occupiedSlots += amount;
                return true;
            }else{
                return false;
            }
        }

        if(resource == GameResource.ENERGY){
            if(energyPerTick - amount >= energyUsedPerTick){
                energyUsedPerTick += amount;
                return true;
            }else{
                return false;
            }
        }

        if(resource == GameResource.ENTERTAINMENT){
            if(entertainmentPerTick - amount >= entertainmentUsedPerTick){
                entertainmentUsedPerTick += amount;
                return true;
            }else{
                return false;
            }
        }
    }

    public enum Side{
        RED,
        BLUE
    }
}
