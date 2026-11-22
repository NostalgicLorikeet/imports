package hitscan.rubyutils.client.render.entities.render;

import hitscan.rubyutils.client.render.entities.layers.LayerStatueArmor;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderDoll extends AbstractRenderStatue {
    public RenderDoll(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.addLayer(new LayerStatueArmor(this, true));
    }
}