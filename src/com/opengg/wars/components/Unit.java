package com.opengg.wars.components;

import com.opengg.core.GGInfo;
import com.opengg.core.math.FastMath;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.util.GGInputStream;
import com.opengg.core.util.GGOutputStream;
import com.opengg.core.world.WorldEngine;
import com.opengg.wars.AStar;
import com.opengg.wars.Node;
import com.opengg.wars.SimonWars;
import com.opengg.wars.game.Empire;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Unit extends GameObject{
    float speed = 8f;

    Attack attack = new Attack();

    Vector2f lastTargetPos = new Vector2f();
    public GameObject target;
    Vector2f nextNode;

    float timeSinceLastAttack = 0;
    float timeSinceLastUpdate = 0;
    float timeSinceLastScan = 0;


    List<Vector2f> path;

    public enum UType{
        WORKER, CAVALRY, INFANTRY, ARCHER
    }

    public Unit(){

    }

    public Unit(Empire.Side side) {
        super(side);
    }

    public static Unit spawn(UType type, Empire.Side side){
        switch (type) {
            case INFANTRY:
                Unit infantry = new Unit(side);
                infantry.health = 10;
                infantry.maxhealth = 10;
                infantry.armor = 2;
                infantry.pierceArmor = 3;
                infantry.speed = 5f;
                infantry.getAttack().attack = 4;
                infantry.getAttack().pierceAttack = 0;
                infantry.getAttack().range = 0;
                infantry.getAttack().speed = 0.5f;
                infantry.attach(new SpriteRenderComponent(side + "/Infantry.png").setRotationOffset(new Vector3f(-15,45,0)).setScaleOffset(3));
                infantry.visibleName = "Infantry";
                return infantry;
            case CAVALRY:
                Unit cavalry = new Unit(side);
                cavalry.health = 40;
                cavalry.maxhealth = 40;
                cavalry.armor = 4;
                cavalry.pierceArmor = 3;
                cavalry.speed = 10f;
                cavalry.getAttack().attack = 4;
                cavalry.getAttack().pierceAttack = 0;
                cavalry.getAttack().range = 0;
                cavalry.getAttack().speed = 1f;
                cavalry.attach(new SpriteRenderComponent(side + "/Cavalry.png").setRotationOffset(new Vector3f(-15,45,0)).setScaleOffset(5));
                cavalry.visibleName = "Cavalry";
                return cavalry;
            case ARCHER:
                Unit archer = new Unit(side);
                archer.health = 20;
                archer.maxhealth = 20;
                archer.armor = 4;
                archer.pierceArmor = 3;
                archer.speed = 6f;
                archer.getAttack().attack = 0;
                archer.getAttack().pierceAttack = 5;
                archer.getAttack().range = 9;
                archer.getAttack().speed = 2f;
                archer.attach(new SpriteRenderComponent(side + "/Archer.png").setRotationOffset(new Vector3f(-15,45,0)).setScaleOffset(3));
                archer.visibleName = "Archer";
                return archer;
            case WORKER:
                Villager worker = new Villager(side);
                worker.health = 20;
                worker.maxhealth = 20;
                worker.armor = 4;
                worker.pierceArmor = 3;
                worker.getAttack().attack = 1;
                worker.getAttack().pierceAttack = 0;
                worker.getAttack().range = 0;
                worker.attach(new SpriteRenderComponent(side + "/Worker.png").setRotationOffset(new Vector3f(-15,45,0)).setScaleOffset(3));
                worker.visibleName = "Villager";
                return worker;
        }
        return null;
    }


    @Override
    public void whenAttackedBy(Unit unit){
        super.whenAttackedBy(unit);
        if(target == null) target = unit;
    }

    public void attack(GameObject unit){
        unit.health=Math.max(0,unit.health-Math.max(0,Math.max(attack.pierceAttack - unit.pierceArmor,1)));
        unit.health=Math.max(0,unit.health-Math.max(0,Math.max(attack.attack- unit.armor ,1)));
        unit.whenAttackedBy(this);
        if(unit.health < 0){
            unit.kill();
            this.target = null;
        }
    }

    public void update(float delta){
        timeSinceLastAttack += delta;
        timeSinceLastUpdate += delta;
        timeSinceLastScan += delta;
        super.update(delta);
        if(GGInfo.isServer() || SimonWars.offline){
            if(this.target != null){
                if(target.health <= 0){
                    this.target = null;
                    return;
                }
                if(!this.target.getPosition().xz().equals(lastTargetPos)){
                    if(timeSinceLastUpdate > 0.5f){
                        timeSinceLastUpdate = 0f;
                        calculateAndUsePath(target.getPosition().xz());
                    }
                }
                lastTargetPos = this.target.getPosition().xz();
                if(this.getPosition().xz().subtract(target.getPosition().xz()).length() < target.colliderWidth + this.colliderWidth + this.getAttack().range){
                    if(timeSinceLastAttack >= this.attack.speed){
                        timeSinceLastAttack = 0;
                        attack(target);
                    }
                    return;
                }
            }

            if(this.getPosition().xz().subtract(nextNode).length() < 0.2f){
                if(!path.isEmpty()){
                    nextNode = path.get(0);
                    path.remove(0);
                }else{
                    if(timeSinceLastScan > 0.5f){
                        timeSinceLastScan = 0f;
                        WorldEngine.getCurrent().getAllDescendants()
                                .stream()
                                .filter(c -> c instanceof  GameObject)
                                .map(c -> (GameObject)c)
                                .filter(c -> c.side != this.side)
                                .filter(c -> c.getPosition().distanceTo(this.getPosition()) < 10f)
                                .findFirst().ifPresent(c -> target = c);
                    }
                }
            }
        }

        var vel = new Vector2f();
        if(this.getPosition().xz().subtract(nextNode).length() > 0.05f)
            vel = this.getPosition().xz().subtract(nextNode).inverse().normalize().multiply(speed);
        var change = vel.multiply(delta);
        this.setPositionOffset(this.getPosition().add(new Vector3f(change.x, 0, change.y)));
    }

    public void calculateAndUsePath(Vector2f pos){
        var star = new AStar(192,192, new Node((int)getPosition().x, (int)getPosition().z), new Node((int)pos.x,(int)pos.y));
        star.setBlocks(SimonWars.blockers);
        var info = star.findPath().stream().map(n -> new Vector2f(n.getRow(), n.getCol())).collect(Collectors.toList());
        setNewPath(info);
    }

    public void setNewPath(List<Vector2f> path){
        if(path.isEmpty()) return;
        nextNode = path.get(0);
        this.path = path;
    }

    public String getVisibleName() {
        return visibleName;
    }

    public int getArmor() {
        return armor;
    }

    public int getPierceArmor() {
        return pierceArmor;
    }

    public float getSpeed() {
        return speed;
    }

    public Attack getAttack(){return attack;}


    @Override
    public void serialize(GGOutputStream out) throws IOException {
        super.serialize(out);
        out.write(nextNode);
    }

    @Override
    public void serializeUpdate(GGOutputStream out) throws IOException{
        super.serializeUpdate(out);
        out.write(nextNode);
    }

    @Override
    public void deserialize(GGInputStream in) throws IOException {
        super.deserialize(in);
        nextNode = in.readVector2f();
    }

    @Override
    public void deserializeUpdate(GGInputStream in, float delta) throws IOException{
        super.deserializeUpdate(in, delta);
        nextNode = in.readVector2f();
    }

}
