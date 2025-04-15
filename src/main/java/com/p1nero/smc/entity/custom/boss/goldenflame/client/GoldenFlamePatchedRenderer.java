package com.p1nero.smc.entity.custom.boss.goldenflame.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.p1nero.smc.entity.custom.boss.goldenflame.GoldenFlame;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import yesman.epicfight.api.client.model.MeshProvider;
import yesman.epicfight.client.renderer.patched.entity.PHumanoidRenderer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class GoldenFlamePatchedRenderer extends PHumanoidRenderer {
    public GoldenFlamePatchedRenderer(MeshProvider mesh, EntityRendererProvider.Context context, EntityType entityType) {
        super(mesh, context, entityType);
    }

    @Override
    public void render(LivingEntity entity, LivingEntityPatch entitypatch, LivingEntityRenderer renderer, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        if(entity instanceof GoldenFlame goldenFlame && goldenFlame.shouldRender()){
            super.render(entity, entitypatch, renderer, buffer, poseStack, packedLight, partialTicks);
        }
    }
}
