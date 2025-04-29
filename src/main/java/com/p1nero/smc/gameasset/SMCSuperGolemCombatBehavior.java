package com.p1nero.smc.gameasset;

import com.merlin204.supergolem.gameassets.Animations;
import com.p1nero.smc.entity.custom.super_golem.SuperBadIronGolemPatch;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.IronGolem;
import yesman.epicfight.data.conditions.entity.HealthPoint;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

public class SMCSuperGolemCombatBehavior {

    public static final CombatBehaviors.Builder<SuperBadIronGolemPatch<?>> SUPER_IRON_GOLEM = CombatBehaviors.<SuperBadIronGolemPatch<?>>builder()
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<SuperBadIronGolemPatch<?>>builder().weight(20).cooldown(50).canBeInterrupted(true).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_KF_1).withinEyeHeight().withinDistance(0.0D, 2.0))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MS_2))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_1))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<SuperBadIronGolemPatch<?>>builder().weight(100).cooldown(50).canBeInterrupted(true).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MS_1).withinEyeHeight().withinDistance(3.0, 5.0))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MS_2))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_1))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<SuperBadIronGolemPatch<?>>builder().weight(100).cooldown(50).canBeInterrupted(true).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MA_1).withinEyeHeight().withinDistance(0.0, 2.0))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MA_2))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MA_1_1))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<SuperBadIronGolemPatch<?>>builder().weight(100).cooldown(50).canBeInterrupted(true).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MA_1).withinEyeHeight().withinDistance(0.0, 2.0))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MA_2))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MA_1_2))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<SuperBadIronGolemPatch<?>>builder().weight(100).cooldown(50).canBeInterrupted(true).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MA_2).withinEyeHeight().withinDistance(0.0, 2.0))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MA_1))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MA_2_1))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<SuperBadIronGolemPatch<?>>builder().weight(100).cooldown(50).canBeInterrupted(true).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MA_2).withinEyeHeight().withinDistance(0.0, 2.0))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MA_1))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MA_2_2))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<SuperBadIronGolemPatch<?>>builder().weight(100).cooldown(50).canBeInterrupted(true).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_2_P).withinEyeHeight().withinDistance(0.0, 2.0))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_3_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_1_P))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<SuperBadIronGolemPatch<?>>builder().weight(50).cooldown(50).canBeInterrupted(true).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_SA_3).withinEyeHeight().withinDistance(0.0, 2.0))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_SA_4))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_SKILL))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<SuperBadIronGolemPatch<?>>builder().weight(99999).cooldown(99999).canBeInterrupted(true).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_BURST)
                                    .withinEyeHeight()
                                    .withinDistance(0.0, 2.0)
                                    .health(0.5F, HealthPoint.Comparator.LESS_RATIO))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_2_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().behavior(superBadIronGolemPatch -> {
                                superBadIronGolemPatch.playSound(SoundEvents.IRON_GOLEM_DEATH, 5.0F, 1.0F, 2.0F);
                                if(superBadIronGolemPatch.getOriginal().level() instanceof ServerLevel serverLevel) {
                                    IronGolem original = superBadIronGolemPatch.getOriginal();
                                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, original.getX(), original.getEyeHeight(), original.getZ(), 100, 0.0D, 0.1D, 0.0D, 0.01);
                                    serverLevel.sendParticles(ParticleTypes.SOUL, original.getX(), original.getEyeHeight(), original.getZ(), 100, 0.0D, 0.1D, 0.0D, 0.01);
                                }
                            }))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_3_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_2_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_3_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_2_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_3_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_2_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_3_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_2_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_3_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_2_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_3_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_1_P))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<SuperBadIronGolemPatch<?>>builder().weight(50).cooldown(200).canBeInterrupted(true).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_SA_1).withinEyeHeight().withinDistance(0.0, 2.0)
                                    .health(0.5F, HealthPoint.Comparator.LESS_RATIO))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_3_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_SA_2))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_2_P))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_MS_2))
                            .nextBehavior(CombatBehaviors.Behavior.<SuperBadIronGolemPatch<?>>builder().animationBehavior(Animations.GOLEM_GA_1_P))
            );

}
