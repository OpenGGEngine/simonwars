package com.opengg.wars.components;

import com.opengg.core.physics.collision.AABB;
import com.opengg.core.world.components.Component;
import com.opengg.wars.game.Empire;

public class GameObject extends Component {
    Empire.Side side;

    int maxhealth = 100;
    int health = 100;

    float colliderWidth = 2f;

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
}
