package com.opengg.wars.components;

import com.opengg.core.engine.Resource;
import com.opengg.core.math.Vector2f;
import com.opengg.core.render.drawn.TexturedDrawnObject;
import com.opengg.core.render.objects.ObjectCreator;
import com.opengg.core.util.GGInputStream;
import com.opengg.core.util.GGOutputStream;
import com.opengg.core.world.components.RenderComponent;

import java.io.IOException;

public class SpriteRenderComponent  extends RenderComponent {
    String source = "";
    public SpriteRenderComponent(){
        this.setDrawable(new TexturedDrawnObject(ObjectCreator.createSquare(new Vector2f(-0.5f,0), new Vector2f(0.5f,1), 0), Resource.getTexture("map1.png")));
        this.setShader("object");
    }

    public SpriteRenderComponent(String path){
        source = path;
        this.setDrawable(new TexturedDrawnObject(ObjectCreator.createSquare(new Vector2f(-0.5f,0), new Vector2f(0.5f,1), 0), Resource.getTexture(path)));
        this.setShader("object");
    }

    @Override
    public void serialize(GGOutputStream out) throws IOException {
        super.serialize(out);
        out.write(source);
    }

    @Override
    public void deserialize(GGInputStream in) throws IOException {
        super.deserialize(in);
        source = in.readString();
        this.setDrawable(new TexturedDrawnObject(ObjectCreator.createSquare(new Vector2f(-0.5f,0), new Vector2f(0.5f,1), 0), Resource.getTexture(source)));
    }
}
