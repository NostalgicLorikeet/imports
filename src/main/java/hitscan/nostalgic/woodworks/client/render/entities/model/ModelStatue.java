package hitscan.nostalgic.woodworks.client.render.entities.model;

import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import java.util.HashMap;

public class ModelStatue extends ModelPlayer {
    private final HashMap<EntityStrawStatue.Part, ModelRenderer> PART_TO_RENDERER_LIST = new HashMap<>();

    public ModelStatue(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.HEAD, bipedHead);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.BODY, bipedBody);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.ARM_LEFT, bipedLeftArm);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.ARM_RIGHT, bipedRightArm);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.LEG_LEFT, bipedLeftLeg);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.LEG_RIGHT, bipedRightLeg);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        if (entityIn instanceof EntityStrawStatue) {
            EntityStrawStatue statue = (EntityStrawStatue) entityIn;

            for (EntityStrawStatue.Part part : EntityStrawStatue.Part.values()) {
                PART_TO_RENDERER_LIST.get(part).rotateAngleX = statue.getLimbRotation(part, EntityStrawStatue.Axis.X);
                PART_TO_RENDERER_LIST.get(part).rotateAngleY = statue.getLimbRotation(part, EntityStrawStatue.Axis.Y);
                PART_TO_RENDERER_LIST.get(part).rotateAngleZ = statue.getLimbRotation(part, EntityStrawStatue.Axis.Z);
            }

            this.bipedRightArm.offsetX = 0.0F;
            this.bipedRightArm.offsetY = 0.0F;
            this.bipedRightArm.offsetZ = 0.0F;
            this.bipedLeftArm.offsetX = 0.0F;
            this.bipedLeftArm.offsetY = 0.0F;
            this.bipedLeftArm.offsetZ = 0.0F;
            this.bipedRightLeg.offsetX = 0.0F;
            this.bipedRightLeg.offsetY = 0.0F;
            this.bipedRightLeg.offsetZ = 0.0F;
            this.bipedLeftLeg.offsetX = 0.0F;
            this.bipedLeftLeg.offsetY = 0.0F;
            this.bipedLeftLeg.offsetZ = 0.0F;
            this.bipedBody.offsetX = 0.0F;
            this.bipedBody.offsetY = 0.0F;
            this.bipedBody.offsetZ = 0.0F;
            this.bipedHead.offsetX = 0.0F;
            this.bipedHead.offsetY = 0.0F;
            this.bipedHead.offsetZ = 0.0F;

            copyModelAngles(bipedRightArm, bipedRightArmwear);
            copyModelAngles(bipedLeftArm, bipedLeftArmwear);
            copyModelAngles(bipedRightLeg, bipedRightLegwear);
            copyModelAngles(bipedLeftLeg, bipedLeftLegwear);
            copyModelAngles(bipedBody, bipedBodyWear);
            copyModelAngles(bipedHead, bipedHeadwear);

            copyModelOffset(bipedRightArm, bipedRightArmwear);
            copyModelOffset(bipedLeftArm, bipedLeftArmwear);
            copyModelOffset(bipedRightLeg, bipedRightLegwear);
            copyModelOffset(bipedLeftLeg, bipedLeftLegwear);
            copyModelOffset(bipedBody, bipedBodyWear);
            copyModelOffset(bipedHead, bipedHeadwear);
        }
    }

    public static void copyModelOffset(ModelRenderer source, ModelRenderer dest)
    {
        dest.offsetX = source.offsetX;
        dest.offsetY = source.offsetY;
        dest.offsetZ = source.offsetZ;
    }
}
