package com.p1nero.smc.item.custom.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.p1nero.smc.SMCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

/**
 * 换副手渲染
 */
public class PotatoCannonRenderer extends RenderItemBase {
    public PotatoCannonRenderer() {
        super((new OpenMatrix4f()).translate(0.0F, 0.0F, -0.13F)
                        .rotateDeg(200, Vec3f.X_AXIS)
                        .rotateDeg(10, Vec3f.Y_AXIS)
                        .rotateDeg(1, Vec3f.Z_AXIS)
                , (new OpenMatrix4f()).translate(0.0F, 0.0F, -0.13F).rotateDeg(-90.0F, Vec3f.X_AXIS));
    }

    @Override
    public void renderItemInHand(ItemStack stack, LivingEntityPatch<?> entityPatch, InteractionHand hand, HumanoidArmature armature, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        OpenMatrix4f modelMatrix = new OpenMatrix4f(this.mainhandcorrectionMatrix);
        Joint holdingHand = armature.toolR;
        modelMatrix.mulFront(poses[holdingHand.getId()]);
        poseStack.pushPose();
        this.mulPoseStack(poseStack, modelMatrix);
        ItemDisplayContext transformType = ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
        Minecraft mc = Minecraft.getInstance();
        mc.gameRenderer.itemInHandRenderer.renderItem(entityPatch.getOriginal(), stack, transformType, false, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}
