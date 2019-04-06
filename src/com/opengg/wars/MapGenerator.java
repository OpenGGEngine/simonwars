package com.opengg.wars;

import com.opengg.core.engine.Resource;
import com.opengg.core.math.Quaternionf;
import com.opengg.core.math.Tuple;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.light.Light;
import com.opengg.core.render.texture.Texture;
import com.opengg.core.world.Terrain;
import com.opengg.core.world.components.Component;
import com.opengg.core.world.components.LightComponent;
import com.opengg.core.world.components.TerrainComponent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapGenerator {
    public static List<Component> generateFromMaps(){
        var terrain = Terrain.generate(Resource.getTextureData("heightmap.png"));

        var terrainComp = new TerrainComponent(terrain);
        terrainComp.setScaleOffset(new Vector3f(256,1,256));
        terrainComp.setPositionOffset(new Vector3f(-128,0,-128));
        terrainComp.setBlotmap(Resource.getTexture("blend1.png"));
        terrainComp.setGroundArray(Texture.create(Texture.arrayConfig(), Resource.getTextureData("road.png"), Resource.getTextureData("road.png"), Resource.getTextureData("road.png"), Resource.getTextureData("road.png")));

        try {
            BufferedImage image = ImageIO.read(new File(Resource.getTexturePath("pathfind.png")).toURI().toURL());
            var blockers = new ArrayList<Tuple<Integer, Integer>>();

            for(int i = 0; i < 512; i++){
                for(int j = 0; j < 512; j++){
                    int rgb = image.getRGB(i,j);
                    int red = (rgb >> 16) & 0xff;
                    SimonWars.map[i][j] = red != 0;
                    if(red == 0)
                        blockers.add(Tuple.of(i,j));
                }
            }

            SimonWars.blockers = blockers.stream()
                    .map(c -> new int[]{c.x, c.y})
                    .toArray(int[][]::new);

        } catch (IOException e) {
            e.printStackTrace();
        }

        var light = new LightComponent(Light.createDirectional(new Quaternionf(new Vector3f(0,0f,-50)),
                new Vector3f(1,1,1)));

        return List.of(terrainComp, light);
    }
}
