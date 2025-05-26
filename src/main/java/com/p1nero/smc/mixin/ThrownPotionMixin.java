package com.p1nero.smc.mixin;

import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.effect.SMCEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ThrownPotion.class)
public abstract class ThrownPotionMixin extends ThrowableItemProjectile {

    public ThrownPotionMixin(EntityType<? extends ThrowableItemProjectile> p_37442_, Level p_37443_) {
        super(p_37442_, p_37443_);
    }

    @Inject(method = "applySplash", at = @At("HEAD"))
    private void smc$applyPotion(List<MobEffectInstance> effectInstances, Entity entity, CallbackInfo ci){
        if(effectInstances.stream().anyMatch(mobEffectInstance -> mobEffectInstance.getEffect().equals(SMCEffects.RUMOR.get()))) {
            AABB aabb = this.getBoundingBox().inflate(4.0, 2.0, 4.0);
            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
            if(this.getOwner() instanceof ServerPlayer serverPlayer && entities.contains(serverPlayer)) {
                SMCAdvancementData.finishAdvancement("rumor_hurt_self", serverPlayer);
            }
        }
    }
}
