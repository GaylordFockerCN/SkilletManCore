package com.p1nero.smc.mixin;

import com.p1nero.smc.entity.ai.epicfight.api.IModifyStunTypeEntityPatch;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import reascer.wom.animation.attacks.BasicMultipleAttackAnimation;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

/**
 * 应用自己的硬直类型
 */
@Mixin(value = BasicMultipleAttackAnimation.class, remap = false)
public abstract class BasicMultipleAttackAnimationMixin extends AttackAnimation {
    public BasicMultipleAttackAnimationMixin(float convertTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        super(convertTime, antic, preDelay, contact, recovery, collider, colliderJoint, path, armature);
    }

    @Inject(method = "hurtCollidingEntities", at = @At(value = "HEAD"), cancellable = true)
    private void mixinStun(LivingEntityPatch<?> entityPatch, float prevElapsedTime, float elapsedTime, EntityState prevState, EntityState state, Phase phase, CallbackInfo ci){
        if(entityPatch instanceof IModifyStunTypeEntityPatch patch){
            if(patch.getStunType() != null){
                super.hurtCollidingEntities(entityPatch, prevElapsedTime, elapsedTime, prevState, state, phase);
                ci.cancel();
            }
        }
    }

    /**
     * 不丢锁
     */
    @Inject(method = "getCoordVector", at = @At("HEAD"), cancellable = true)
    private void smc$getCoordVector(LivingEntityPatch<?> entitypatch, DynamicAnimation dynamicAnimation, CallbackInfoReturnable<Vec3> cir){
        Vec3 vec3 = super.getCoordVector(entitypatch, dynamicAnimation);
        if (entitypatch.shouldBlockMoving() && this.getProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE).orElse(false)) {
            vec3 = vec3.scale(0.0);
        }
        cir.setReturnValue(vec3);
    }

}
