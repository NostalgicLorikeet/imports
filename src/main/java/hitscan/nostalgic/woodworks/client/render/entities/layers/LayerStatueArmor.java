package hitscan.nostalgic.woodworks.client.render.entities.layers;

import hitscan.nostalgic.woodworks.client.render.entities.model.ModelBipedStatueArmor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;

public class LayerStatueArmor extends LayerBipedArmor {
    boolean isDoll;

    public LayerStatueArmor(RenderLivingBase<?> rendererIn, boolean isDoll) {
        super(rendererIn);
        this.isDoll = isDoll;
        this.initArmor();
    }

    @Override
    protected void initArmor() {
        this.modelLeggings = createStaticArmorModel(0.5F);
        this.modelArmor = createStaticArmorModel(1.0F);
    }

    private ModelBiped createStaticArmorModel(float modelSize) {
        return new ModelBipedStatueArmor(modelSize, isDoll);
    }
}
