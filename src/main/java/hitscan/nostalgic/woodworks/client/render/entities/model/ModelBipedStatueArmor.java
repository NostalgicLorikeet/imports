package hitscan.nostalgic.woodworks.client.render.entities.model;

import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import java.util.HashMap;

public class ModelBipedStatueArmor extends ModelBiped {
    private final HashMap<EntityStrawStatue.Part, ModelRenderer> PART_TO_RENDERER_LIST = new HashMap<>();

    public ModelRenderer bipedFakeBody;
    public ModelBipedStatueArmor(float modelSize, boolean isDoll) {
        super(modelSize);
        float addToSize = (isDoll ? 1.5F : 0F);
        float offsetY = (isDoll ? -8.0F-modelSize+addToSize-1.0F : -8.0F-modelSize+addToSize+1.0F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, offsetY, -4.0F, 8, 8, 8, modelSize + addToSize + 0.5F);
        this.bipedHead.setRotationPoint(0.0F, 0.0F + this.bipedHead.rotationPointY, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, offsetY, -4.0F, 8, 8, 8, modelSize + 1.0F + addToSize);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + this.bipedHeadwear.rotationPointY, 0.0F);

        //ACTUAL BULLSHIT
        this.bipedFakeBody = new ModelRenderer(this);
        this.bipedFakeBody.setRotationPoint(0.0F, 0.0F + this.bipedBody.rotationPointY, 0.0F);

        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.HEAD, bipedHead);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.BODY, bipedBody);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.ARM_LEFT, bipedLeftArm);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.ARM_RIGHT, bipedRightArm);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.LEG_LEFT, bipedLeftLeg);
        PART_TO_RENDERER_LIST.put(EntityStrawStatue.Part.LEG_RIGHT, bipedRightLeg);
        for (EntityStrawStatue.Part part : EntityStrawStatue.Part.values()) {
            if (part != EntityStrawStatue.Part.BODY) {
                this.bipedFakeBody.addChild(PART_TO_RENDERER_LIST.get(part));
            }
        }
        this.bipedFakeBody.addChild(bipedHeadwear);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        if (entityIn instanceof EntityStrawStatue) {
            EntityStrawStatue statue = (EntityStrawStatue) entityIn;

            this.bipedBody.offsetY = -statue.getBodyTranslation();
            this.bipedFakeBody.offsetY = -statue.getBodyTranslation();

            this.bipedRightArm.rotateAngleX = statue.getLimbRotation(EntityStrawStatue.Part.ARM_RIGHT, EntityStrawStatue.Axis.X);
            this.bipedRightArm.rotateAngleY = statue.getLimbRotation(EntityStrawStatue.Part.ARM_RIGHT, EntityStrawStatue.Axis.Y);
            this.bipedRightArm.rotateAngleZ = statue.getLimbRotation(EntityStrawStatue.Part.ARM_RIGHT, EntityStrawStatue.Axis.Z);
            this.bipedLeftArm.rotateAngleX = statue.getLimbRotation(EntityStrawStatue.Part.ARM_LEFT, EntityStrawStatue.Axis.X);
            this.bipedLeftArm.rotateAngleY = statue.getLimbRotation(EntityStrawStatue.Part.ARM_LEFT, EntityStrawStatue.Axis.Y);
            this.bipedLeftArm.rotateAngleZ = statue.getLimbRotation(EntityStrawStatue.Part.ARM_LEFT, EntityStrawStatue.Axis.Z);
            this.bipedRightLeg.rotateAngleX = statue.getLimbRotation(EntityStrawStatue.Part.LEG_RIGHT, EntityStrawStatue.Axis.X) - (!statue.areLegsLockedToBody() ? statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.X) : 0);
            this.bipedRightLeg.rotateAngleY = statue.getLimbRotation(EntityStrawStatue.Part.LEG_RIGHT, EntityStrawStatue.Axis.Y) - (!statue.areLegsLockedToBody() ? statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.Y) : 0);
            this.bipedRightLeg.rotateAngleZ = statue.getLimbRotation(EntityStrawStatue.Part.LEG_RIGHT, EntityStrawStatue.Axis.Z) - (!statue.areLegsLockedToBody() ? statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.Z) : 0);
            this.bipedLeftLeg.rotateAngleX = statue.getLimbRotation(EntityStrawStatue.Part.LEG_LEFT, EntityStrawStatue.Axis.X) - (!statue.areLegsLockedToBody() ? statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.X) : 0);
            this.bipedLeftLeg.rotateAngleY = statue.getLimbRotation(EntityStrawStatue.Part.LEG_LEFT, EntityStrawStatue.Axis.Y) - (!statue.areLegsLockedToBody() ? statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.Y) : 0);
            this.bipedLeftLeg.rotateAngleZ = statue.getLimbRotation(EntityStrawStatue.Part.LEG_LEFT, EntityStrawStatue.Axis.Z) - (!statue.areLegsLockedToBody() ? statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.Z) : 0);
            this.bipedBody.rotateAngleX = statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.X);
            this.bipedBody.rotateAngleY = statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.Y);
            this.bipedBody.rotateAngleZ = statue.getLimbRotation(EntityStrawStatue.Part.BODY, EntityStrawStatue.Axis.Z);
            this.bipedHead.rotateAngleX = statue.getLimbRotation(EntityStrawStatue.Part.HEAD, EntityStrawStatue.Axis.X);
            this.bipedHead.rotateAngleY = statue.getLimbRotation(EntityStrawStatue.Part.HEAD, EntityStrawStatue.Axis.Y);
            this.bipedHead.rotateAngleZ = statue.getLimbRotation(EntityStrawStatue.Part.HEAD, EntityStrawStatue.Axis.Z);

            copyModelAngles(bipedHead, bipedHeadwear);
            bipedHeadwear.offsetX = bipedHead.offsetX;
            bipedHeadwear.offsetY = bipedHead.offsetY;
            bipedHeadwear.offsetZ = bipedHead.offsetZ;

            copyModelAngles(bipedBody, bipedFakeBody);
            bipedFakeBody.offsetX = bipedBody.offsetX;
            bipedFakeBody.offsetY = bipedBody.offsetY;
            bipedFakeBody.offsetZ = bipedBody.offsetZ;
        }
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw , headPitch, scale, entityIn);
        this.bipedBody.render(scale);
        this.bipedFakeBody.render(scale);
    }
}
