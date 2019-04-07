package com.opengg.wars.components;

import com.opengg.core.physics.collision.AABB;
import com.opengg.core.util.GGInputStream;
import com.opengg.core.util.GGOutputStream;
import com.opengg.core.world.WorldEngine;
import com.opengg.core.world.components.Component;
import com.opengg.wars.game.Empire;

import java.io.IOException;

public class GameObject extends Component {
    Empire.Side side;

    String typeName = "";
    String visibleName = "ladical";

    int maxhealth = 100;
    public int health = 100;

    int armor = 100;
    int pierceArmor = 1;

    float colliderWidth = 2f;

    public GameObject() {

    }

    public GameObject(Empire.Side side){
        this.side = side;
    }

    public Empire.Side getSide() {
        return side;
    }

    public int getMaxhealth() {
        return maxhealth;
    }

    public int getHealth() {
        return health;
    }

    public float getColliderWidth(){
        return colliderWidth;
    }

    public void update(float delta){
        if(health <=0){
            WorldEngine.markForRemoval(this);
        }
    }

    @Override
    public void serialize(GGOutputStream out) throws IOException {
        super.serialize(out);
        out.write(maxhealth);
        out.write(health);
        out.write(colliderWidth);
        out.write(armor);
        out.write(pierceArmor);
        out.write(typeName);
        out.write(visibleName);
        out.write(side == Empire.Side.RED ? 0 : 1);
    }

    @Override
    public void serializeUpdate(GGOutputStream out) throws IOException{
        super.serializeUpdate(out);
        out.write((short) health);
    }

    @Override
    public void deserialize(GGInputStream in) throws IOException {
        super.deserialize(in);
        maxhealth = in.readInt();
        health = in.readInt();
        colliderWidth = in.readFloat();
        armor = in.readInt();
        pierceArmor = in.readInt();
        typeName = in.readString();
        visibleName = in.readString();
        side = in.readInt() == 0 ? Empire.Side.RED : Empire.Side.BLUE;
    }

    @Override
    public void deserializeUpdate(GGInputStream in, float delta) throws IOException{
        super.deserializeUpdate(in, delta);
        health = in.readShort();
    }
}
