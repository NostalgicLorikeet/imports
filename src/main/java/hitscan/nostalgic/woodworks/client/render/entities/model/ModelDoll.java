package hitscan.nostalgic.woodworks.client.render.entities.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelDoll extends ModelStatue {
    ModelRenderer bipedDollHead;
    ModelRenderer bipedDollHeadwear;

    public ModelDoll(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);

        float bipedHeadOldRotationPointY = this.bipedHead.rotationPointY;
        float bipedHeadwearOldRotationPointY = this.bipedHeadwear.rotationPointY;

        this.bipedDollHead = new ModelRenderer(this, 0, 0);
        this.bipedDollHead.addBox(-4.0F, -8.0F-1.0F, -4.0F, 8, 8, 8, 1.5F);
        this.bipedDollHead.setRotationPoint(0.0F, bipedHeadOldRotationPointY, 0.0F);
        this.bipedDollHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedDollHeadwear.addBox(-4.0F, -8.0F-1.0F, -4.0F, 8, 8, 8, 1.5F + 0.5F);
        this.bipedDollHeadwear.setRotationPoint(0.0F, 0.0F + bipedHeadwearOldRotationPointY, 0.0F);

        this.bipedBody.addChild(bipedDollHead);
        this.bipedBody.addChild(bipedDollHeadwear);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        copyModelAngles(bipedHead, bipedDollHead);
        copyModelAngles(bipedHead, bipedDollHeadwear);
        copyModelOffset(bipedHead, bipedDollHead);
        copyModelOffset(bipedHead, bipedDollHeadwear);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.bipedHead.showModel = false;
        this.bipedHeadwear.showModel = false;
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }
}
