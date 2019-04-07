package com.opengg.wars;

import com.opengg.core.io.input.mouse.MouseButton;
import com.opengg.core.io.input.mouse.MouseButtonListener;
import com.opengg.core.io.input.mouse.MouseController;
import com.opengg.core.math.FastMath;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.RenderEngine;
import com.opengg.core.world.components.ModelComponent;
import com.opengg.wars.components.Building;
import com.opengg.wars.game.Empire;

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
            for(Vector2f check:collisions){
                int x = (int)getPosition().x; int z = (int)getPosition().z;
                int xPos = x+(int)check.x; int zPos = z+(int)check.y;
                if(xPos > SimonWars.map.length || xPos<0||zPos<0||zPos>SimonWars.map[0].length)return;
                if(!SimonWars.map[xPos][zPos]) return;
                CommandManager.sendCommand(Command.create("building_create",  type.toString(), x+","+z, SimonWars.side.toString()));
                this.setEnabled(false);
            }
        }
    }

    public void enable(Building.BType type){
        this.type = type;
        switch(type){
            case FACTORY:
                setModel(Models.factory);
                setScaleOffset(1f);
                break;
            case QUARRY:
            case IRONMINE:
            case GOLDMINE:
                setModel(Models.mine);
                setRotationOffset(new Vector3f(180,90,180));
                setScaleOffset(0.001f);
                break;
            case TOWN:
                setModel(Models.house);
                setRotationOffset(new Vector3f(0,0,90));
                setScaleOffset(1f);
                break;
        }
        this.setEnabled(true);

    }

    @Override
    public void onButtonRelease(int button) {

    }
}
