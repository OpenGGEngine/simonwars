package com.opengg.wars.components;

import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.drawn.DrawnObject;
import com.opengg.core.render.objects.ObjectCreator;
import com.opengg.core.render.shader.ShaderController;
import com.opengg.core.util.GGInputStream;
import com.opengg.core.util.GGOutputStream;
import com.opengg.core.world.components.RenderComponent;

import java.io.IOException;

public class BarRenderComponent extends RenderComponent {
    Vector3f fill = new Vector3f(1,0,0);
    Vector3f back = new Vector3f(0.2f,0.2f,0.2f);
    float percent;

    public BarRenderComponent(){
        percent = 0;

        this.setDrawable(ObjectCreator.createSquare(new Vector2f(-0.5f,0), new Vector2f(0.5f,1f), 0));
        this.setScaleOffset(new Vector3f(2, 0.2f, 2));
        this.setShader("bar");
    }

    public BarRenderComponent setPercent(float percent){
        this.percent = percent;
        return this;
    }

    public void render(){
        ShaderController.setUniform("fill", fill);
        ShaderController.setUniform("back", back);
        ShaderController.setUniform("percent", percent);
        super.render();
    }

    @Override
    public void serializeUpdate(GGOutputStream out) throws IOException {
        super.serializeUpdate(out);
        out.write(percent);
    }

    @Override
    public void deserializeUpdate(GGInputStream in, float delta) throws IOException{
        super.deserializeUpdate(in, delta);
        percent = in.readFloat();
    }
}
