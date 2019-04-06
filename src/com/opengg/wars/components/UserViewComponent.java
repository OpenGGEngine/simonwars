package com.opengg.wars.components;

import com.opengg.core.math.Quaternionf;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.math.Vector3fm;
import com.opengg.core.world.Action;
import com.opengg.core.world.ActionType;
import com.opengg.core.world.Actionable;
import com.opengg.core.world.components.ActionTransmitterComponent;
import com.opengg.core.world.components.CameraComponent;
import com.opengg.core.world.components.ControlledComponent;

public class UserViewComponent extends ControlledComponent implements Actionable {
    private Vector3fm control = new Vector3fm();
    private Vector3f vel = new Vector3f();

    private float speed = 25;
    private float accel = 10;

    public UserViewComponent(){
        this.attach(new CameraComponent());
        this.attach(new ActionTransmitterComponent());
        this.setPositionOffset(new Vector3f(500,25,0));
        this.setRotationOffset(new Vector3f(55,-135, 0));
    }

    @Override
    public void update(float delta){
        if(isCurrentUser()){
            var dir = this.getRotation().transform(new Vector3f(control)).setY(0);
            var target = dir.multiply(speed);
            var actual = target.subtract(vel);
            vel = vel.add(actual.multiply(delta * accel));

            setPositionOffset(getPositionOffset().add(vel.multiply(delta)));
            var pos = this.getPosition();
            if(pos.x <= 0) this.setPositionOffset(pos.setX(0));
            if(pos.x >= 512) this.setPositionOffset(pos.setX(512));

            if(pos.z <= 0) this.setPositionOffset(pos.setZ(0));
            if(pos.z >= 512) this.setPositionOffset(pos.setZ(512));

        }else{
        }

    }

    @Override
    public void onAction(Action action) {
        if (action.type == ActionType.PRESS) {
            switch (action.name) {
                case "forward":
                    control.z -= 1;
                    break;
                case "backward":
                    control.z += 1;
                    break;
                case "left":
                    control.x -= 1;
                    break;
                case "right":
                    control.x += 1;
                    break;
            }
        } else {
            switch (action.name) {
                case "forward":
                    control.z += 1;
                    break;
                case "backward":
                    control.z -= 1;
                    break;
                case "left":
                    control.x += 1;
                    break;
                case "right":
                    control.x -= 1;
                    break;
            }
        }
    }
}
