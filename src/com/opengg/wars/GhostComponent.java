package com.opengg.wars;

import com.opengg.core.io.input.mouse.MouseButton;
import com.opengg.core.io.input.mouse.MouseButtonListener;
import com.opengg.core.io.input.mouse.MouseController;
import com.opengg.core.math.FastMath;
import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.world.components.ModelComponent;

public class GhostComponent extends ModelComponent implements MouseButtonListener {

    public Vector2f[] collisions = {new Vector2f(0,0)};
    @Override
    public void update(float delta){
        var ray = MouseController.getRay();
        var pos = FastMath.getRayPlaneIntersection(ray.getRay(), new Vector3f(0,1,0), new Vector3f(0,1,0));
        Vector2f imFloored = pos.xz();
        int x = (int)imFloored.x;int z = (int)imFloored.y;
        setPositionOffset(new Vector3f(x,0,z));
    }

    @Override
    public void onButtonPress(int button) {
        int x = (int)getPosition().x; int z = (int)getPosition().z;
        System.out.println(getPosition());
        if(button == MouseButton.LEFT){
            for(Vector2f check:collisions){
                int xPos = x+(int)check.x; int zPos = z+(int)check.y;
                System.out.println("Checked:" + xPos+","+zPos);
                if(xPos > SimonWars.map.length || xPos<0||zPos<0||zPos>SimonWars.map[0].length)return;
                if(!SimonWars.map[xPos][zPos]) return;
            }
        }
        System.out.println("Can Build");
    }

    @Override
    public void onButtonRelease(int button) {

    }
}
