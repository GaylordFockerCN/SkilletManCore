package com.p1nero.smc.mixin;

import com.p1nero.smc.entity.ai.epicfight.api.IModifyAttackSpeedEntityPatch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin(AttackAnimation.class)
public class AttackAnimationMixin {
    @Inject(method = "getPlaySpeed", at = @At("HEAD"), cancellable = true, remap = false)
    private void onGetPlaySpeed(LivingEntityPatch<?> entityPatch, DynamicAnimation animation, CallbackInfoReturnable<Float> cir) {
        if(entityPatch instanceof IModifyAttackSpeedEntityPatch entity){
            if(entity.getAttackSpeed() == 0){
                cir.setReturnValue(1.0F);
            }
            cir.setReturnValue(entity.getAttackSpeed());
        }
    }
}