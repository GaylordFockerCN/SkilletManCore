package com.p1nero.smc.mixin;

import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import java.util.Random;

@Mixin(value = WOMAnimations.class, remap = false)
public class WOMAnimationsMixin {

    /**
     * antic为0会导致出伤异常
     */
    @ModifyArg(method = "build", at = @At(value = "INVOKE", target = "Lreascer/wom/animation/attacks/BasicMultipleAttackAnimation;<init>(FFFFLyesman/epicfight/api/collider/Collider;Lyesman/epicfight/api/animation/Joint;Ljava/lang/String;Lyesman/epicfight/api/model/Armature;)V"), index = 1)
    private static float modify(float antic){
        if(antic == 0 /*&& VersonCheck.isGreaterThan("20.9.6", ModList.get().getModFileById("epicfight").versionString())*/){
            return 0.1F;
        }
        return antic;
    }

    /**
     * 补玩家判定防崩溃
     */
    @Inject(method = "lambda$build$171", at = @At("HEAD"), cancellable = true)
    private static void inject(LivingEntityPatch<?> entityPatch, StaticAnimation self, Object[] params, CallbackInfo ci){
        if(!(entityPatch instanceof ServerPlayerPatch)){
            OpenMatrix4f transformMatrix = entityPatch.getArmature().getBindedTransformFor(entityPatch.getAnimator().getPose(0.0F), Armatures.BIPED.toolL);
            transformMatrix.translate(new Vec3f(0.0F, 0.0F, 0.0F));
            OpenMatrix4f.mul((new OpenMatrix4f()).rotate(-((float)Math.toRadians(entityPatch.getOriginal().yBodyRotO + 180.0F)), new Vec3f(0.0F, 1.0F, 0.0F)), transformMatrix, transformMatrix);
            int n = 70;
            double r = 0.1;

            for(int i = 0; i < n; ++i) {
                double theta = 6.283185307179586 * (new Random()).nextDouble();
                double phi = Math.acos(2.0 * (new Random()).nextDouble() - 1.0);
                double x = r * Math.sin(phi) * Math.cos(theta);
                double y = r * Math.sin(phi) * Math.sin(theta);
                double z = r * Math.cos(phi);
                entityPatch.getOriginal().level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, (double)transformMatrix.m30 + entityPatch.getOriginal().getX(), (double)transformMatrix.m31 + entityPatch.getOriginal().getY(), (double)transformMatrix.m32 + entityPatch.getOriginal().getZ(), (float)x, (float)y, (float)z);
                if (i % 2 == 0) {
                    entityPatch.getOriginal().level().addParticle(ParticleTypes.LAVA, (double)transformMatrix.m30 + entityPatch.getOriginal().getX(), (double)transformMatrix.m31 + entityPatch.getOriginal().getY(), (double)transformMatrix.m32 + entityPatch.getOriginal().getZ(), (float)x, (float)y, (float)z);
                }
            }
            ci.cancel();
        }
    }

}
