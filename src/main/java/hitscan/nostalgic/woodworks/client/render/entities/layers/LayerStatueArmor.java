package hitscan.nostalgic.woodworks.client.render.entities.layers;

import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.Entity;

public class LayerStatueArmor extends LayerBipedArmor {
    public LayerStatueArmor(RenderLivingBase<?> rendererIn) {
        super(rendererIn);
    }

    @Override
    protected void initArmor() {
        this.modelLeggings = createStaticArmorModel(0.5F);
        this.modelArmor = createStaticArmorModel(1.0F);
    }

    private ModelBiped createStaticArmorModel(float modelSize) {
        return new ModelBiped(modelSize) {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
                super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

                if (entityIn instanceof EntityStrawStatue) {
                    EntityStrawStatue statue = (EntityStrawStatue) entityIn;

                    this.bipedRightArm.rotateAngleX = statue.getLimbRotation(EntityStrawStatue.Part.ARM_RIGHT, EntityStrawStatue.Axis.X);
                    this.bipedRightArm.rotateAngleY = statue.getLimbRotation(EntityStrawStatue.Part.ARM_RIGHT, EntityStrawStatue.Axis.Y);
                    this.bipedRightArm.rotateAngleZ = statue.getLimbRotation(EntityStrawStatue.Part.ARM_RIGHT, EntityStrawStatue.Axis.Z);
                    this.bipedLeftArm.rotateAngleX = statue.getLimbRotation(EntityStrawStatue.Part.ARM_LEFT, EntityStrawStatue.Axis.X);
                    this.bipedLeftArm.rotateAngleY = statue.getLimbRotation(EntityStrawStatue.Part.ARM_LEFT, EntityStrawStatue.Axis.Y);
                    this.bipedLeftArm.rotateAngleZ = statue.getLimbRotation(EntityStrawStatue.Part.ARM_LEFT, EntityStrawStatue.Axis.Z);
                    this.bipedRightLeg.rotateAngleX = statue.getLimbRotation(EntityStrawStatue.Part.LEG_RIGHT, EntityStrawStatue.Axis.X);
                    this.bipedRightLeg.rotateAngleY = statue.getLimbRotation(EntityStrawStatue.Part.LEG_RIGHT, EntityStrawStatue.Axis.Y);
                    this.bipedRightLeg.rotateAngleZ = statue.getLimbRotation(EntityStrawStatue.Part.LEG_RIGHT, EntityStrawStatue.Axis.Z);
                    this.bipedLeftLeg.rotateAngleX = statue.getLimbRotation(EntityStrawStatue.Part.LEG_LEFT, EntityStrawStatue.Axis.X);
                    this.bipedLeftLeg.rotateAngleY = statue.getLimbRotation(EntityStrawStatue.Part.LEG_LEFT, EntityStrawStatue.Axis.Y);
                    this.bipedLeftLeg.rotateAngleZ = statue.getLimbRotation(EntityStrawStatue.Part.LEG_LEFT, EntityStrawStatue.Axis.Z);
                    this.bipedBody.rotateAngleX = statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.X);
                    this.bipedBody.rotateAngleY = statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.Y);
                    this.bipedBody.rotateAngleZ = statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.Z);
                    this.bipedHead.rotateAngleX = statue.getLimbRotation(EntityStrawStatue.Part.HEAD, EntityStrawStatue.Axis.X);
                    this.bipedHead.rotateAngleY = statue.getLimbRotation(EntityStrawStatue.Part.HEAD, EntityStrawStatue.Axis.Y);
                    this.bipedHead.rotateAngleZ = statue.getLimbRotation(EntityStrawStatue.Part.HEAD, EntityStrawStatue.Axis.Z);
                }
            }
        };
    }
}
