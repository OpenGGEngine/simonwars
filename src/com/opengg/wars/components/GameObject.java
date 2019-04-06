package com.opengg.wars.components;

import com.opengg.core.world.components.Component;
import com.opengg.wars.game.Empire;

public class GameObject extends Component {
    Empire.Side side;

    int health;

    public GameObject(Empire.Side side){
        this.side = side;
    }
}
