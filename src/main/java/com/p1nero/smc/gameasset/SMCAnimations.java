package com.p1nero.smc.gameasset;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.entity.ai.epicfight.api.IModifyAttackSpeedEntityPatch;
import com.p1nero.smc.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import reascer.wom.animation.attacks.UltimateAttackAnimation;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.gameasset.WOMWeaponColliders;
import reascer.wom.particle.WOMParticles;
import reascer.wom.skill.WOMSkillDataKeys;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.api.utils.LevelUtil;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.StunType;

import java.util.Random;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SMCAnimations {

    public static StaticAnimation ANTITHEUS_ASCENSION;
    public static StaticAnimation POWER_SIT;
    public static StaticAnimation TALKING;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put(SkilletManCoreMod.MOD_ID, SMCAnimations::build);
    }

    private static void build() {
        HumanoidArmature biped = Armatures.BIPED;
        POWER_SIT = new StaticAnimation(true, "biped/living/power_sit", biped);
        TALKING = new StaticAnimation(true, "biped/living/talking", biped);
        ANTITHEUS_ASCENSION = (new UltimateAttackAnimation(0.1F, "biped/skill/antitheus_ascension", biped, new AttackAnimation.Phase(0.0F, 0.5F, 0.6F, 0.65F, 0.65F, biped.rootJoint, WOMWeaponColliders.PLUNDER_PERDITION), new AttackAnimation.Phase(0.65F, 1.75F, 2.05F, 2.8F, Float.MAX_VALUE, biped.rootJoint, WOMWeaponColliders.PLUNDER_PERDITION))).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(4.0F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(20.0F)).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, WOMParticles.SOLAR_HIT).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F), 1).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(20.0F), 1).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, WOMParticles.SOLAR_HIT, 1).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NONE, 1).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.7F).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 1.0F).addEvents(AnimationEvent.TimeStampedEvent.create(0.5F, (entityPatch, self, params) -> {
                    if (entityPatch instanceof PlayerPatch) {
                        entityPatch.getOriginal().level().playSound((Player)entityPatch.getOriginal(), entityPatch.getOriginal(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 1.0F, 0.5F);
                    }
                }, AnimationEvent.Side.CLIENT), AnimationEvent.TimeStampedEvent.create(1.75F, (entityPatch, self, params) -> {
                    for(Entity entity : EntityUtil.getNearByEntities(entityPatch.getOriginal(), 5)){
                        entity.setSecondsOnFire(10);
                    }
                    entityPatch.getOriginal().level().playSound(null, entityPatch.getOriginal(), SoundEvents.WITHER_BREAK_BLOCK, SoundSource.PLAYERS, 1.0F, 0.5F);
                    entityPatch.getOriginal().level().playSound(null, entityPatch.getOriginal(), SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.0F, 0.5F);
                    entityPatch.getOriginal().level().playSound(null, entityPatch.getOriginal(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);
                }, AnimationEvent.Side.SERVER), AnimationEvent.TimeStampedEvent.create(1.75F, (entityPatch, self, params) -> {
                    float target_x = (float) entityPatch.getOriginal().getX();
                    float target_y = (float) entityPatch.getOriginal().getY() + 0.2F;
                    float target_z = (float) entityPatch.getOriginal().getZ();
                    int n = 80;
                    double r = 0.6;
                    double t = 0.05;

                    int i;
                    double theta;
                    double phi;
                    double x;
                    double y;
                    double z;
                    Vec3f direction;
                    OpenMatrix4f rotation;
                    for(i = 0; i < n * 2; ++i) {
                        theta = 6.283185307179586 * (new Random()).nextDouble();
                        phi = ((new Random()).nextDouble() - 0.5) * Math.PI * t / r;
                        x = r * Math.cos(phi) * Math.cos(theta);
                        y = r * Math.cos(phi) * Math.sin(theta);
                        z = r * Math.sin(phi);
                        direction = new Vec3f((float)x, (float)y, (float)z);
                        rotation = (new OpenMatrix4f()).rotate((float)Math.toRadians(90.0), new Vec3f(1.0F, 0.0F, 0.0F));
                        OpenMatrix4f.transform3v(rotation, direction, direction);
                        entityPatch.getOriginal().level().addParticle(ParticleTypes.FLAME, target_x, target_y, target_z, direction.x, direction.y, direction.z);
                    }

                    for(i = 0; i < n; ++i) {
                        theta = 6.283185307179586 * (new Random()).nextDouble();
                        phi = ((new Random()).nextDouble() - 0.5) * Math.PI * t / r;
                        x = r * Math.cos(phi) * Math.cos(theta);
                        y = r * Math.cos(phi) * Math.sin(theta);
                        z = r * Math.sin(phi);
                        direction = new Vec3f((float)x * ((new Random()).nextFloat() + 0.5F) * 0.8F, (float)y * ((new Random()).nextFloat() + 0.5F) * 0.8F, (float)z * ((new Random()).nextFloat() + 0.5F) * 0.8F);
                        rotation = (new OpenMatrix4f()).rotate((float)Math.toRadians(90.0), new Vec3f(1.0F, 0.0F, 0.0F));
                        OpenMatrix4f.transform3v(rotation, direction, direction);
                        entityPatch.getOriginal().level().addParticle(ParticleTypes.FLAME, target_x, target_y, target_z, direction.x, direction.y, direction.z);
                    }

                    for(i = 0; i < 60; ++i) {
                        entityPatch.getOriginal().level().addParticle(ParticleTypes.FLAME, target_x + ((new Random()).nextFloat() - 0.5F) * 1.2F, target_y + 0.2F, (target_z + ((new Random()).nextFloat() - 0.5F) * 1.2F), 0.0,((new Random()).nextFloat() * 1.5F), 0.0);
                    }

                    Level level = entityPatch.getOriginal().level();
                    Vec3 floorPos = WOMAnimations.ReuseableEvents.getfloor(entityPatch, self, new Vec3f(0.0F, 0.0F, 0.0F), Armatures.BIPED.rootJoint);
                    Vec3 weaponEdge = new Vec3(floorPos.x, floorPos.y, floorPos.z);
                    LevelUtil.circleSlamFracture(entityPatch.getOriginal(), level, weaponEdge, 4.0, true, true);
                }, AnimationEvent.Side.CLIENT));

        if (WOMAnimations.TIME_TRAVEL != null) {
            WOMAnimations.TIME_TRAVEL.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, ((dynamicAnimation, livingEntityPatch, v, v1, v2) -> {
                if (livingEntityPatch instanceof IModifyAttackSpeedEntityPatch patch) {
                    return patch.getAttackSpeed();
                }
                return 1.0F;
            }));
        }

    }

}


