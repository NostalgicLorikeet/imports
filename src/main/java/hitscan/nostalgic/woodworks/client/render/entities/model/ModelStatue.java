package hitscan.nostalgic.woodworks.client.render.entities.model;

import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import java.util.HashMap;

public class ModelStatue extends ModelPlayer {
    protected final HashMap<EntityStrawStatue.Part, ModelRenderer> PART_TO_RENDERER_LIST = new HashMap<>();

    public ModelStatue(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);

        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.HEAD, bipedHead);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.BODY, bipedBody);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.ARM_LEFT, bipedLeftArm);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.ARM_RIGHT, bipedRightArm);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.LEG_LEFT, bipedLeftLeg);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.LEG_RIGHT, bipedRightLeg);
        for (EntityStrawStatue.Part part : EntityStrawStatue.Part.values()) {
            if (part != EntityStrawStatue.Part.BODY) {
                this.bipedBody.addChild(PART_TO_RENDERER_LIST.get(part));
            }
        }
        this.bipedBody.addChild(bipedHeadwear);
        this.bipedBody.addChild(bipedBodyWear);
        this.bipedBody.addChild(bipedRightArmwear);
        this.bipedBody.addChild(bipedLeftArmwear);
        this.bipedBody.addChild(bipedRightLegwear);
        this.bipedBody.addChild(bipedLeftLegwear);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        this.bipedBodyWear.rotateAngleX = 0.0F;
        this.bipedBodyWear.rotateAngleY = 0.0F;
        this.bipedBodyWear.rotateAngleZ = 0.0F;

        if (entityIn instanceof EntityStrawStatue) {
            EntityStrawStatue statue = (EntityStrawStatue) entityIn;

            this.bipedBody.offsetY = -statue.getBodyTranslation();

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

            for (EntityStrawStatue.Part part : EntityStrawStatue.Part.values()) {
                PART_TO_RENDERER_LIST.get(part).rotateAngleX = statue.getLimbRotation(part, EntityStrawStatue.Axis.X) - (!statue.areLegsLockedToBody() && part.isLeg() ? statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.X) : 0);
                PART_TO_RENDERER_LIST.get(part).rotateAngleY = statue.getLimbRotation(part, EntityStrawStatue.Axis.Y) - (!statue.areLegsLockedToBody() && part.isLeg() ? statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.Y) : 0);
                PART_TO_RENDERER_LIST.get(part).rotateAngleZ = statue.getLimbRotation(part, EntityStrawStatue.Axis.Z) - (!statue.areLegsLockedToBody() && part.isLeg() ? statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.Z) : 0);
            }

            copyModelAngles(bipedRightArm, bipedRightArmwear);
            copyModelAngles(bipedLeftArm, bipedLeftArmwear);
            copyModelAngles(bipedRightLeg, bipedRightLegwear);
            copyModelAngles(bipedLeftLeg, bipedLeftLegwear);
            copyModelAngles(bipedHead, bipedHeadwear);

            copyModelOffset(bipedRightArm, bipedRightArmwear);
            copyModelOffset(bipedLeftArm, bipedLeftArmwear);
            copyModelOffset(bipedRightLeg, bipedRightLegwear);
            copyModelOffset(bipedLeftLeg, bipedLeftLegwear);
            copyModelOffset(bipedHead, bipedHeadwear);
        }
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.bipedBody.render(scale);
    }

    public static void copyModelOffset(ModelRenderer source, ModelRenderer dest)
    {
        dest.offsetX = source.offsetX;
        dest.offsetY = source.offsetY;
        dest.offsetZ = source.offsetZ;
    }
}
