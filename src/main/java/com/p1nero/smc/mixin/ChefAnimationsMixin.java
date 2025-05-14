package com.p1nero.smc.mixin;

import com.merlin204.super_chef.gameassets.ChefAnimations;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.effect.EpicFightMobEffects;

/**
 * 添加buff
 */
@Mixin(value = ChefAnimations.class, remap = false)
public class ChefAnimationsMixin {

    @Inject(method = "groundSplit", at = @At("HEAD"))
    private static void smc$groundSlam(LivingEntityPatch<?> entityPatch, double viewOffset, double xOffset, double yOffset, double zOffset, float radius, CallbackInfo ci){
        if(!entityPatch.getOriginal().level().isClientSide){
            entityPatch.getOriginal().addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), 200));
        }
    }
}
