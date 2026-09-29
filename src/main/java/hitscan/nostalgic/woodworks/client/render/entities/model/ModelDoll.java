package hitscan.nostalgic.woodworks.client.render.entities.model;

import net.minecraft.client.model.ModelRenderer;

public class ModelDoll extends ModelStatue {
    public ModelDoll(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);

        float bipedHeadOldRotationPointY = this.bipedHead.rotationPointY;
        float bipedHeadwearOldRotationPointY = this.bipedHeadwear.rotationPointY;

        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F-2F, -4.0F, 8, 8, 8, 2F);
        this.bipedHead.setRotationPoint(0.0F, bipedHeadOldRotationPointY, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F-2F, -4.0F, 8, 8, 8, 1.5F + 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + bipedHeadwearOldRotationPointY, 0.0F);
    }
}
