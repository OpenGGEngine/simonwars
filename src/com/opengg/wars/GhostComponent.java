package com.opengg.wars;

import com.opengg.core.io.input.mouse.MouseButton;
import com.opengg.core.io.input.mouse.MouseButtonListener;
import com.opengg.core.io.input.mouse.MouseController;
import com.opengg.core.math.*;
import com.opengg.core.render.RenderEngine;
import com.opengg.core.world.components.ModelComponent;
import com.opengg.wars.components.Building;
import com.opengg.wars.game.Deposit;
import com.opengg.wars.game.Empire;
import com.opengg.wars.game.GameResource;

import java.util.List;

public class GhostComponent extends ModelComponent implements MouseButtonListener {
    Building.BType type = Building.BType.FACTORY;

    public Vector2f[] collisions = {new Vector2f(0,0)};

    public GhostComponent(){
        this.setFormat(RenderEngine.tangentVAOFormat);
        this.setTransparency(true);
    }

    @Override
    public void update(float delta){
        var ray = MouseController.getRay();
        var pos = FastMath.getRayPlaneIntersection(ray.getRay(), new Vector3f(0,1,0), new Vector3f(0,1,0));
        Vector2f imFloored = pos.xz();
        int x = (int)imFloored.x;int z = (int)imFloored.y;
        if(!(x > SimonWars.map.length-1 || x<0||z<0||z>SimonWars.map[0].length-1) && SimonWars.map[x][z])
        setPositionOffset(new Vector3f(x,0,z));
    }

    @Override
    public void onButtonPress(int button) {

        if(!isEnabled()) return;
        if(button == MouseButton.LEFT){
            boolean oneByResource = (type == Building.BType.FACTORY)||(type == Building.BType.TOWN)||(type == Building.BType.FARM)||(type==Building.BType.BARRACKS);
            for(Vector2f check:collisions){
                int x = (int)getPosition().x; int z = (int)getPosition().z;
                int xPos = x+(int)check.x; int zPos = z+(int)check.y;
                if(xPos > SimonWars.map.length || xPos<0||zPos<0||zPos>SimonWars.map[0].length)return;
                if(!SimonWars.map[xPos][zPos]) return;
                for(int i = -1;i<2;i++){
                    for(int i2 = -1;i2<2;i2++){
                        for(Tuple<Vector2i, Deposit> deposit: SimonWars.deposits){
                            if(deposit.x.equals(new Vector2i(xPos+i,zPos+i2))) {
                                oneByResource |= (deposit.y == Deposit.IRON && type == Building.BType.IRONMINE)||(deposit.y == Deposit.GOLD && type == Building.BType.GOLDMINE)||
                                        (deposit.y == Deposit.STONE && type == Building.BType.QUARRY)||
                                        (deposit.y == Deposit.WOOD && type == Building.BType.CAMP);
                            }
                        }
                    }
                }
                if(!oneByResource)return;
                CommandManager.sendCommand(Command.create("building_create",  type.toString(), x+","+z, SimonWars.side.toString()));
                SimonWars.map[x][z] = false;
                this.setPositionOffset(new Vector3f(1000,0,1000));
                this.setEnabled(false);
                switch (type){
                    case FACTORY:
                        Empire.get(SimonWars.side).use(GameResource.IRON,150);
                        Empire.get(SimonWars.side).use(GameResource.WOOD,80);
                        break;
                    case GOLDMINE:
                        Empire.get(SimonWars.side).use(GameResource.IRON,40);
                        Empire.get(SimonWars.side).use(GameResource.STONE,100);
                        break;
                    case QUARRY:
                        Empire.get(SimonWars.side).use(GameResource.WOOD,40);
                        Empire.get(SimonWars.side).use(GameResource.IRON,10);
                        break;
                    case TOWN:
                        Empire.get(SimonWars.side).use(GameResource.ENTERTAINMENT,40);
                        Empire.get(SimonWars.side).use(GameResource.WOOD,150);
                        break;
                    case IRONMINE:
                        Empire.get(SimonWars.side).use(GameResource.WOOD,70);
                        Empire.get(SimonWars.side).use(GameResource.STONE,100);
                        break;
                    case CAMP:
                        Empire.get(SimonWars.side).use(GameResource.FOOD,40);
                        Empire.get(SimonWars.side).use(GameResource.WOOD,20);
                        break;
                    case BARRACKS:
                        Empire.get(SimonWars.side).use(GameResource.IRON,40);
                        Empire.get(SimonWars.side).use(GameResource.FOOD,60);
                        break;
                    case FARM:
                        Empire.get(SimonWars.side).use(GameResource.IRON,30);
                        Empire.get(SimonWars.side).use(GameResource.WOOD,30);
                        break;

                }
            }
        }
    }

    public void enable(Building.BType type){
        this.type = type;
        switch(type){
            case FACTORY:
                setModel(Models.factory);
                setScaleOffset(1f);
                setRotationOffset(new Vector3f(0));
                break;
            case BARRACKS:
                setModel(Models.barrack);
                setScaleOffset(0.02f);
                setRotationOffset(new Vector3f(90,0,0));
                break;
            case QUARRY:
            case IRONMINE:
            case GOLDMINE:
                setModel(Models.mine);
                setRotationOffset(new Vector3f(180,90,180));
                setScaleOffset(0.1f);
                break;
            case CAMP:
                setModel(Models.forestCamp);
                setScaleOffset(0.009f);
                setRotationOffset(new Vector3f(0));
                break;
            case TOWN:
                setModel(Models.house);
                setRotationOffset(new Vector3f(270,180,180));
                setScaleOffset(0.5f);
                break;
            case FARM:
                setModel(Models.farm);
                setScaleOffset(1f);
                setRotationOffset(new Vector3f(0,0,-90));
                break;
        }
        this.setEnabled(true);

    }

    @Override
    public void onButtonRelease(int button) {

    }
}
