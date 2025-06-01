package com.p1nero.smc.mixin;

import com.p1nero.smc.entity.custom.boss.goldenflame.GoldenFlame;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.world.entity.projectile.AntitheusDarkness;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.HurtableEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;

/**
 * 从神王射出来的则硬直为long
 */
@Mixin(AntitheusDarkness.class)
public abstract class AntitheusDarknessMixin extends AbstractHurtingProjectile {
    protected AntitheusDarknessMixin(EntityType<? extends AbstractHurtingProjectile> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void inject(EntityHitResult p_37626_, CallbackInfo ci){
        if(this.getOwner() instanceof GoldenFlame){
            if (!this.level().isClientSide()) {
                Entity entity = p_37626_.getEntity();
                Entity entity1 = this.getOwner();
                LivingEntityPatch<?> livingEntityPatch = EpicFightCapabilities.getEntityPatch(this.getOwner(), LivingEntityPatch.class);
                if (entity1 instanceof LivingEntity livingentity && livingEntityPatch != null) {
                    HurtableEntityPatch<?> hitHurtableEntityPatch = EpicFightCapabilities.getEntityPatch(entity, HurtableEntityPatch.class);
                    EpicFightDamageSource damage = livingEntityPatch.getDamageSource(WOMAnimations.ANTITHEUS_SHOOT, InteractionHand.MAIN_HAND);
                    damage.setImpact(2.0F);
                    damage.setStunType(StunType.LONG);
                    if(entity1 instanceof GoldenFlame goldenFlame && entity instanceof Player){
                        if(livingEntityPatch.getEntityState().attackResult(damage).dealtDamage()){
                            goldenFlame.setHealth(goldenFlame.getHealth() + goldenFlame.getMaxHealth() / 100);
                        }
                    }
                    int prevInvulTime = entity.invulnerableTime;
                    entity.invulnerableTime = 0;
                    float entity1damage = 4.0F;
                    float enchantmentDamage = 0.0F;
                    if (entity instanceof LivingEntity) {
                        this.level().playSound(null, livingentity.getX(), livingentity.getY(), livingentity.getZ(), SoundEvents.WITHER_AMBIENT, this.getSoundSource(), 0.4F, 2.0F);
                        enchantmentDamage = EnchantmentHelper.getDamageBonus(livingentity.getItemInHand(InteractionHand.MAIN_HAND), ((LivingEntity)entity).getMobType());
                        entity1damage += enchantmentDamage;
                    }

                    entity.hurt(damage, entity1damage * (float)(1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, livingentity) / 3));
                    if (hitHurtableEntityPatch != null) {
                        hitHurtableEntityPatch.knockBackEntity(livingentity.getPosition(1.0F), 0.5F);
                    }

                    if (enchantmentDamage != 0.0F) {
                        ((ServerLevel)this.level()).sendParticles(ParticleTypes.ENCHANTED_HIT, this.getX(), this.getY(), this.getZ(), 20, 0.0, 0.0, 0.0, 0.5);
                    }

                    entity.invulnerableTime = prevInvulTime;
                    if (entity instanceof LivingEntity && !((LivingEntity)entity).isDeadOrDying() && !(entity instanceof ArmorStand)) {
                        livingentity.setLastHurtMob(entity);
                        livingentity.addTag("antitheus_pull:" + entity.getId());
                    }
                } else {
                    entity.hurt(this.damageSources().magic(), 4.0F);
                }
            }

        }
    }
}
