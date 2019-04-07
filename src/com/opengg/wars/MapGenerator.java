package com.opengg.wars;

import com.opengg.core.engine.Resource;
import com.opengg.core.math.Quaternionf;
import com.opengg.core.math.Tuple;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.light.Light;
import com.opengg.core.render.texture.Texture;
import com.opengg.core.world.Terrain;
import com.opengg.core.world.WorldEngine;
import com.opengg.core.world.components.Component;
import com.opengg.core.world.components.LightComponent;
import com.opengg.core.world.components.TerrainComponent;
import com.opengg.core.world.components.WaterComponent;

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
        terrainComp.setScaleOffset(new Vector3f(512,1,512));
        terrainComp.setBlotmap(Resource.getTexture("blend1.png"));
        terrainComp.setGroundArray(Texture.create(Texture.arrayConfig(), Resource.getTextureData("bettergrass.png"), Resource.getTextureData("bettergrass.png"), Resource.getTextureData("bettergrass.png"), Resource.getTextureData("bettergrass.png")));

        try {
            BufferedImage image = ImageIO.read(new File(Resource.getTexturePath("pathfind.png")).toURI().toURL());
            var blockers = new ArrayList<Tuple<Integer, Integer>>();

            for(int i = 0; i < 512; i++){
                for(int j = 0; j < 512; j++){
                    int rgb = image.getRGB(i,j);
                    int red = (rgb >> 16) & 0xff;
                    SimonWars.map[i][j] = red == 255;
                    if(red != 255)
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

        WaterComponent water = new WaterComponent(Resource.getTexture("water.jpg").getData().get(0),1000f);
        water.setPositionOffset(new Vector3f(0,-1,0));
        WorldEngine.getCurrent().attach(water);
        return List.of(terrainComp.setPositionOffset(0,5,0), light,water);
    }
}
