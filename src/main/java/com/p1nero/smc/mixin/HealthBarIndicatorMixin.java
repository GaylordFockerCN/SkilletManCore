package com.p1nero.smc.mixin;

import com.p1nero.smc.entity.custom.boss.SMCBoss;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.gui.HealthBarIndicator;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

/**
 * 取消boss血条绘制
 */
@Mixin(value = HealthBarIndicator.class, remap = false)
public abstract class HealthBarIndicatorMixin {

    @Inject(method = "shouldDraw", at = @At("HEAD"), cancellable = true)
    private void smc$shouldDraw(LivingEntity entity, LivingEntityPatch<?> entitypatch, LocalPlayerPatch playerpatch, CallbackInfoReturnable<Boolean> cir) {
        if(entity instanceof SMCBoss) {
            cir.setReturnValue(false);
        }
    }

}
