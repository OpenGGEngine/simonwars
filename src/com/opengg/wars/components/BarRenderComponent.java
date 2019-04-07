package com.opengg.wars.components;

import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.drawn.DrawnObject;
import com.opengg.core.render.objects.ObjectCreator;
import com.opengg.core.render.shader.ShaderController;
import com.opengg.core.world.components.RenderComponent;

public class BarRenderComponent extends RenderComponent {
    Vector3f fill;
    Vector3f back;
    float percent;

    public BarRenderComponent(Vector3f pos, Vector2f size, Vector3f fill, Vector3f back){
        percent = 0;
        this.fill = fill;
        this.back = back;

        this.setDrawable(ObjectCreator.createSquare(new Vector2f(-0.5f,0), new Vector2f(0.5f,0.1f), 0));
        this.setPositionOffset(pos);
        this.setShader("object");
    }

    public BarRenderComponent setPercent(float percent){
        this.percent = percent;
        return this;
    }

    public void render(){
        ShaderController.useConfiguration("bar");
        ShaderController.setUniform("fill", fill);
        ShaderController.setUniform("back", back);
        ShaderController.setUniform("percent", percent);
        super.render();

    }
}
