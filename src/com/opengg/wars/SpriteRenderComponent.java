package com.opengg.wars;

import com.opengg.core.engine.Resource;
import com.opengg.core.math.Vector2f;
import com.opengg.core.render.drawn.TexturedDrawnObject;
import com.opengg.core.render.objects.ObjectCreator;
import com.opengg.core.world.components.RenderComponent;

public class SpriteRenderComponent  extends RenderComponent {
    public SpriteRenderComponent(){
        this.setDrawable(new TexturedDrawnObject(ObjectCreator.createSquare(new Vector2f(-0.5f,0), new Vector2f(0.5f,1), 0), Resource.getTexture("map1.png")));
        this.setShader("object");
    }
}
