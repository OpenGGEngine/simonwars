package com.opengg.wars;

import com.opengg.core.engine.Resource;
import com.opengg.core.math.Quaternionf;
import com.opengg.core.math.Tuple;
import com.opengg.core.math.Vector2i;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.light.Light;
import com.opengg.core.render.texture.Texture;
import com.opengg.core.world.Terrain;
import com.opengg.core.world.WorldEngine;
import com.opengg.core.world.components.*;
import com.opengg.wars.game.Deposit;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapGenerator {
    public static List<Component> generateFromMaps(){
        var terrain = Terrain.generate(Resource.getTextureData("heightmap.png"));

        var terrainComp = new TerrainComponent(terrain);
        terrainComp.setScaleOffset(new Vector3f(192,1,192));
        terrainComp.setBlotmap(Resource.getTexture("blend1.png"));
        terrainComp.setGroundArray(Texture.create(Texture.arrayConfig(), Resource.getTextureData("bettergrass.png"), Resource.getTextureData("bettergrass.png"), Resource.getTextureData("bettergrass.png"), Resource.getTextureData("bettergrass.png")));

        var thingos = new ArrayList<Component>();
        try {
            BufferedImage pathfind = ImageIO.read(new File(Resource.getTexturePath("pathfind.png")).toURI().toURL());
            BufferedImage startpos = ImageIO.read(new File(Resource.getTexturePath("startpos.png")).toURI().toURL());

            var blockers = new ArrayList<Tuple<Integer, Integer>>();

            for(int i = 0; i < 192; i++){
                for(int j = 0; j < 192; j++){
                    int rgb = pathfind.getRGB(i,j);
                    int redHeight = (rgb >> 16) & 0xff;
                    SimonWars.map[i][j] = redHeight == 255;
                    if(redHeight != 255)
                        blockers.add(Tuple.of(i,j));


                    int pixel = startpos.getRGB(i,j);
                    int alpha = (pixel >> 24) & 0xff;
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = (pixel) & 0xff;

                    if(alpha == 255){
                        if(red == 255){
                            SimonWars.deposits.add(Tuple.of(new Vector2i(i,j), Deposit.GOLD));
                            thingos.add(new ModelComponent(Resource.getModel("Rock_6")).setPositionOffset(new Vector3f(i,0,j)));
                        }else if(green == 255){
                            SimonWars.deposits.add(Tuple.of(new Vector2i(i,j), Deposit.WOOD));
                            thingos.add(new ModelComponent(Resource.getModel("Rock_6")).setPositionOffset(new Vector3f(i,0,j)));
                        }else if(blue == 255){
                            SimonWars.deposits.add(Tuple.of(new Vector2i(i,j), Deposit.IRON));
                            thingos.add(new ModelComponent(Resource.getModel("Rock_6")).setPositionOffset(new Vector3f(i,0,j)));
                        }else{
                            SimonWars.deposits.add(Tuple.of(new Vector2i(i,j), Deposit.STONE));
                            thingos.add(new ModelComponent(Resource.getModel("Rock_6")).setPositionOffset(new Vector3f(i,0,j)));
                        }

                        SimonWars.map[i][j] = false;
                        blockers.add(Tuple.of(i,j));
                    }
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
        return Stream.concat(List.of(terrainComp.setPositionOffset(0,5,0), water,light).stream(), thingos.stream()).collect(Collectors.toList());
    }
}
