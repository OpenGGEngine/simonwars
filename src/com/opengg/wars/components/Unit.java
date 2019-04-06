package com.opengg.wars.components;

import com.opengg.core.GGInfo;
import com.opengg.core.engine.Resource;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.world.components.ModelComponent;
import com.opengg.wars.AStar;
import com.opengg.wars.Node;
import com.opengg.wars.SimonWars;
import com.opengg.wars.game.Empire;

import java.util.List;
import java.util.stream.Collectors;

public class Unit extends GameObject{
    String name;
    String visibleName;

    int armor;
    int pierceArmor;

    float speed = 8f;

    List<Attack> attacks;

    Vector2f target;
    Vector2f nextNode;

    List<Vector2f> path;

    public Unit(Empire.Side side) {
        super(side);
        this.attach(new ModelComponent(Resource.getModel("pear")));
    }

    public void update(float delta){
        if(GGInfo.isServer() || SimonWars.offline){
            if(this.getPosition().xz().subtract(nextNode).length() < 0.2f){
                if(!path.isEmpty()){
                    nextNode = path.get(0);
                    path.remove(0);
                }
            }else{
                var vel = this.getPosition().xz().subtract(nextNode).inverse().normalize().multiply(speed);
                var change = vel.multiply(delta);
                this.setPositionOffset(this.getPosition().add(new Vector3f(change.x, 0, change.y)));
            }
        }else{

        }
    }

    public void calculateAndUsePath(Vector2f pos){
        var star = new AStar(512,512, new Node((int)getPosition().x, (int)getPosition().z), new Node((int)pos.x,(int)pos.y));
        star.setBlocks(SimonWars.blockers);
        var info = star.findPath().stream().map(n -> new Vector2f(n.getRow(), n.getCol())).collect(Collectors.toList());
        setNewPath(info);
    }

    public void setNewPath(List<Vector2f> path){
        nextNode = path.get(0);
        this.path = path;
    }
}
