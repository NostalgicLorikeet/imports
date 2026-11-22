package hitscan.rubyutils.client.render.entities.render;

import hitscan.rubyutils.Tags;
import hitscan.rubyutils.client.render.entities.layers.LayerEncrustedGem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

public class RenderEncrusted extends RenderZombie {
    private static final ResourceLocation ENCRUSTED_TEXTURE = new ResourceLocation(Tags.MOD_ID, "textures/entity/encrusted.png");

    public RenderEncrusted(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.addLayer(new LayerEncrustedGem(this));
    }

    @Override
    public void doRender(EntityZombie entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }


    @Override
    protected ResourceLocation getEntityTexture(EntityZombie entity)
    {
        return ENCRUSTED_TEXTURE;
    }
}
