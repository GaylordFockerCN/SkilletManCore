package com.p1nero.smc.entity.custom.boss.goldenflame.ai;

import com.p1nero.smc.entity.custom.boss.goldenflame.GoldenFlamePatch;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.ai.epicfight.api.TimeStampedEvent;
import com.p1nero.smc.entity.custom.boss.goldenflame.BlackHoleEntity;
import com.p1nero.smc.entity.custom.boss.goldenflame.GoldenFlame;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.gameasset.WOMSounds;
import reascer.wom.particle.WOMParticles;
import yesman.epicfight.data.conditions.entity.HealthPoint;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.WitherGhostClone;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.p1nero.smc.entity.ai.epicfight.SMCCombatBehaviors.*;

/**
 * 金焰神王AI
 */
public class GoldenFlameCombatBehaviors {

    public static final Function<HumanoidMobPatch<?>, Boolean> CAN_CHARGING = (humanoidMobPatch -> humanoidMobPatch instanceof GoldenFlamePatch goldenFlamePatch
            && !goldenFlamePatch.getOriginal().getMainHandItem().isEmpty()
            && (!goldenFlamePatch.getOriginal().isCharging()
            && goldenFlamePatch.getOriginal().shouldRender()
            && goldenFlamePatch.getOriginal().getAntiFormCooldown() <= 0)
            && !goldenFlamePatch.getAnimator().getPlayerFor(null).getAnimation().equals(WOMAnimations.TORMENT_CHARGE));

    /**
     * 开始蓄力并播放音效
     */
    public static final Consumer<HumanoidMobPatch<?>> START_CHARGE = (humanoidMobPatch -> {
        if (humanoidMobPatch.getOriginal() instanceof GoldenFlame goldenFlame && !humanoidMobPatch.isLogicalClient() && !humanoidMobPatch.getEntityState().inaction()) {
            humanoidMobPatch.playAnimationSynchronized(WOMAnimations.TORMENT_CHARGE, 0.0F);
            goldenFlame.startCharging();
        }
    });

    /**
     * 结束蓄力状态并播放粒子音效提示
     */
    public static final Consumer<HumanoidMobPatch<?>> STOP_CHARGE = (humanoidMobPatch -> {
        if (humanoidMobPatch.getOriginal() instanceof GoldenFlame goldenFlame) {
            goldenFlame.resetCharging();
            goldenFlame.setStrafingTime(0);
            goldenFlame.setInactionTime(0);
            if (goldenFlame.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.FLAME, goldenFlame.getX(), goldenFlame.getY(), goldenFlame.getZ(), 200, 0, 1, 0, 0.1);
            }
        }
        humanoidMobPatch.playSound(SoundEvents.BLAZE_SHOOT, 1, 1);
    });

    /**
     * 开始隐身
     */
    public static final Consumer<HumanoidMobPatch<?>> SET_HIDE = (humanoidMobPatch -> {
        if (humanoidMobPatch.getOriginal() instanceof GoldenFlame goldenFlame) {
            goldenFlame.setShouldRender(false);
            if (goldenFlame.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(WOMParticles.TELEPORT.get(), humanoidMobPatch.getOriginal().position().x, humanoidMobPatch.getOriginal().position().y + 1.0, humanoidMobPatch.getOriginal().position().z, 1, 0.0, 0.0, 0.0, 0.0);
                humanoidMobPatch.playSound(SoundEvents.ENDERMAN_TELEPORT, 1, 1);
            }
        }
    });

    /**
     * 结束隐身
     */
    public static final Consumer<HumanoidMobPatch<?>> SET_NOT_HIDE = (humanoidMobPatch -> {
        if (humanoidMobPatch.getOriginal() instanceof GoldenFlame goldenFlame) {
            goldenFlame.setShouldRender(true);
            if (goldenFlame.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(WOMParticles.TELEPORT_REVERSE.get(), humanoidMobPatch.getOriginal().position().x, humanoidMobPatch.getOriginal().position().y + 1.0, humanoidMobPatch.getOriginal().position().z, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
    });

    public static final Consumer<LivingEntityPatch<?>> PLAY_GROUND_SLAM = livingEntityPatch -> {
        livingEntityPatch.playSound(EpicFightSounds.GROUND_SLAM.get(), 1, 1);
    };

    public static final Consumer<LivingEntityPatch<?>> PLAY_SOLAR_HIT = livingEntityPatch -> {
        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
    };

    public static final Consumer<HumanoidMobPatch<?>> SHOOT_WITHER_GHOST = (humanoidMobPatch -> {
        if (humanoidMobPatch.getTarget() != null && humanoidMobPatch.getOriginal().level() instanceof ServerLevel serverLevel) {
            humanoidMobPatch.getOriginal().getLookControl().setLookAt(humanoidMobPatch.getTarget());
            WitherGhostClone ghostClone = new WitherGhostClone(serverLevel, humanoidMobPatch.getOriginal().position(), humanoidMobPatch.getTarget());
            serverLevel.addFreshEntity(ghostClone);
        }
    });

    public static final Consumer<LivingEntityPatch<?>> TIME_STAMPED_SHOOT_WITHER_GHOST = (entityPatch -> {
        if (entityPatch.getTarget() != null && entityPatch.getOriginal().level() instanceof ServerLevel serverLevel) {
            entityPatch.getOriginal().lookAt(EntityAnchorArgument.Anchor.EYES, entityPatch.getTarget().position());
            WitherGhostClone ghostClone = new WitherGhostClone(serverLevel, entityPatch.getOriginal().position(), entityPatch.getTarget());
            serverLevel.addFreshEntity(ghostClone);
        }
    });

    public static final Consumer<LivingEntityPatch<?>> TIME_STAMPED_SUMMON_BLACK_HOLE = (entityPatch -> {
        if (entityPatch.getOriginal().level() instanceof ServerLevel serverLevel) {
            BlackHoleEntity blackHoleEntity = SMCEntities.BLACK_HOLE.get().create(serverLevel);
            if (blackHoleEntity != null) {
                blackHoleEntity.setPos(entityPatch.getOriginal().position());
                serverLevel.addFreshEntity(blackHoleEntity);
            }
        }
    });

    public static final Consumer<HumanoidMobPatch<?>> PLAY_TIME_TRAVEL_SOUND = (humanoidMobPatch -> humanoidMobPatch.playSound(WOMSounds.TIME_TRAVEL.get(), 1f, 1f));

    public static final Consumer<HumanoidMobPatch<?>> PLAY_SOLAR_BRASERO_CREMATORIO = customAttackAnimation(WOMAnimations.SOLAR_BRASERO_CREMATORIO, 0.3F, 0.8f, null, 0,
            new TimeStampedEvent(0.3F, (entityPatch -> {
                LivingEntity entity = entityPatch.getOriginal();
                if (entityPatch.getOriginal().level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, entity.getX(), entity.getY() + 1.0, entity.getZ(), 10, 0.0, 0.0, 0.0, 0.1);
                    serverLevel.playSound(null, entity.getX(), entity.getY() + 0.75, entity.getZ(), SoundEvents.PLAYER_HURT_ON_FIRE, SoundSource.BLOCKS, 1.0F, 0.5F);
                }
            })),
            new TimeStampedEvent(0.8F, (livingEntityPatch)  ->  {
                if(livingEntityPatch.getOriginal() instanceof GoldenFlame goldenFlame){
                    goldenFlame.setFlameCircleLifeTimeAndStart(600);
                }
            }),
            new TimeStampedEvent(1.0F, PLAY_SOLAR_BRASERO_CREMATORIO_PARTICLE),
            new TimeStampedEvent(1.5F, PLAY_SOLAR_BRASERO_CREMATORIO_PARTICLE),
            new TimeStampedEvent(2.0F, PLAY_SOLAR_BRASERO_CREMATORIO_PARTICLE));

    /**
     * 反神形态
     */
    public static final CombatBehaviors.Builder<HumanoidMobPatch<?>> GOLDEN_FLAME_FIST = CombatBehaviors.<HumanoidMobPatch<?>>builder()
            //常规出拳2种4套
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(10F).cooldown(600).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_AUTO_2, 0.3f, 0.5f, StunType.SHORT))
                                    .withinDistance(0, 2.5).withinEyeHeight())
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_AUTO_3, 0.1f, 0.5f, StunType.SHORT)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ENDERBLASTER_ONEHAND_AUTO_4, 0.2f, 0.6f, StunType.LONG))))
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(0.001F).cooldown(50).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_AUTO_2, 0.3f, 0.5f, StunType.SHORT))
                                    .withinDistance(0, 2.5).withinEyeHeight())
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_AUTO_3, 0.1f, 0.5f, StunType.SHORT)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ENDERBLASTER_ONEHAND_AUTO_4, 0.2f, 0.6f, StunType.LONG))))

            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(10F).cooldown(600).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_AUTO_2, 0.3f, 0.5f, StunType.SHORT))
                                    .withinDistance(0, 2.5).withinEyeHeight())
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_AUTO_3, 0.1f, 0.5f, StunType.SHORT)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_DEATHFALL, 0.3f, 0.6f, StunType.LONG, 1.3f,
                                    new TimeStampedEvent(0.01f, timeStampedPlaySound(SoundEvents.WITHER_SHOOT, 1, 0.5F)),
                                    new TimeStampedEvent(0.3f, PLAY_GROUND_SLAM))))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(0.001F).cooldown(50).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_AUTO_2, 0.3f, 0.5f, StunType.SHORT))
                                    .withinDistance(0, 2.5).withinEyeHeight())
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_AUTO_3, 0.1f, 0.5f, StunType.SHORT)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_DEATHFALL, 0.3f, 0.6f, StunType.LONG, 1.3f,
                                    new TimeStampedEvent(0.01f, timeStampedPlaySound(SoundEvents.WITHER_SHOOT, 1, 0.5F)),
                                    new TimeStampedEvent(0.3f, PLAY_GROUND_SLAM))))
            )
            //黄火拳+蓝火拳
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(5F).cooldown(400).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_AUTO_2, 0.3f, 0.5f, StunType.SHORT))
                                    .withinDistance(0, 2.5).withinEyeHeight())
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_1_POLVORA, 0.3f, 0.8f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.2f, PLAY_SOLAR_HIT))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_3_POLVORA, 0.7f, 1, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, PLAY_SOLAR_HIT),
                                    new TimeStampedEvent(0.6f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.SOLAR_AUTO_3_POLVORA.getTotalTime()))))))//打断播放，接下一个序列
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_3_POLVORA, 0.15f, 1, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, PLAY_SOLAR_HIT),
                                    new TimeStampedEvent(0.6f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.SOLAR_AUTO_3_POLVORA.getTotalTime()))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_3_POLVORA, 0.15f, 1, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, PLAY_SOLAR_HIT),
                                    new TimeStampedEvent(0.6f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.SOLAR_AUTO_3_POLVORA.getTotalTime()))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_DEATHFALL, 0.3f, 0.6f, StunType.LONG, 1.3f,
                                    new TimeStampedEvent(0.01f, timeStampedPlaySound(SoundEvents.WITHER_SHOOT, 1, 0.5F)),
                                    new TimeStampedEvent(0.3f, PLAY_GROUND_SLAM))))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(5F).cooldown(400).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_AUTO_2, 0.3f, 0.5f, StunType.SHORT))
                                    .withinDistance(0, 2.5).withinEyeHeight())
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_IMPACTO, 0.1f, 0.5f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.4f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_3, 0.4f, 0.9f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_3, 0.06f, 0.9f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_3, 0.06f, 0.9f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.KATANA_FATAL_DRAW_DASH, 0.3f, 0.8f, null, 1f,
                                    new TimeStampedEvent(0.4f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.KATANA_FATAL_DRAW_DASH.getTotalTime()))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_BLINK, 0f, 0.8f, StunType.HOLD, 1.3f,
                                    new TimeStampedEvent(0.0f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.ANTITHEUS_ASCENDED_BLINK.getTotalTime(), 0.1f))))))
            )
            //近距跳劈+中距跑居
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(1F).cooldown(160).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(0, 2.5).withinEyeHeight()
                                    .behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_DEATHFALL, 0.3f, 0.6f, StunType.LONG, 1.3f,
                                            new TimeStampedEvent(0.01f, timeStampedPlaySound(SoundEvents.WITHER_SHOOT, 1, 0.5F)),
                                            new TimeStampedEvent(0.3f, PLAY_GROUND_SLAM))))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(3F).cooldown(320).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(0, 2.5).withinEyeHeight()
                                    .behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_DEATHFALL, 0.3f, 0.6f, StunType.LONG, 1.3f,
                                            new TimeStampedEvent(0.01f, timeStampedPlaySound(SoundEvents.WITHER_SHOOT, 1, 0.5F)),
                                            new TimeStampedEvent(0.3f, PLAY_GROUND_SLAM))))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(10F).cooldown(200).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(3, 4.9).withinEyeHeight()
                                    .behavior(customAttackAnimation(WOMAnimations.KATANA_FATAL_DRAW_DASH, 0.3f, 0.8f, null, 1f,
                                            new TimeStampedEvent(0.4f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.KATANA_FATAL_DRAW_DASH.getTotalTime()))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_BLINK, 0f, 0.8f, StunType.HOLD, 1.3f,
                                    new TimeStampedEvent(0.0f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.ANTITHEUS_ASCENDED_BLINK.getTotalTime(), 0.1f))))))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(20F).cooldown(360).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(3, 4.9).withinEyeHeight()
                                    .behavior(customAttackAnimation(WOMAnimations.KATANA_FATAL_DRAW_DASH, 0.3f, 0.8f, null, 1f,
                                            new TimeStampedEvent(0.4f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.KATANA_FATAL_DRAW_DASH.getTotalTime()))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_BLINK, 0f, 0.8f, StunType.HOLD, 1.3f,
                                    new TimeStampedEvent(0.0f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.ANTITHEUS_ASCENDED_BLINK.getTotalTime(), 0.1f))))))
            )
            //常态黑洞
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(3F).cooldown(1000).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_AUTO_2, 0.3f, 0.5f, StunType.SHORT))
                                    .custom(worldLevelCheck(1))//第二轮回解锁
                                    .withinDistance(0, 2.5).withinEyeHeight())
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_AUTO_3, 0.1f, 0.5f, StunType.SHORT)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(true, 0, 0)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(ROTATE_TO_TARGET))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_BLACKHOLE, 0.2f, 0.8f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(1.75f, timeStampedPlaySound(WOMSounds.ANTITHEUS_BLACKKHOLE.get(), 1, 0.9F)),
                                    new TimeStampedEvent(1.75f, TIME_STAMPED_SUMMON_BLACK_HOLE),
                                    TimeStampedEvent.createTimeCommandEvent(1.75f, "summon minecraft:area_effect_cloud ~ ~ ~ {Duration:100,ReapplicationDelay:2,Radius:3,RadiusPerTick:0.03,WaitTime:10,Effects:[{Id: 20,Amplifier: 5,Duration: 120},{Id: 7,Amplifier: 0,Duration: 120}],Color:16777215}", false)
                            )))
            )
            //远距-1-3*3凋零头
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(2000F).cooldown(600).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(5, 50).withinEyeHeight()
                                    .behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_SHOOT, 0.7f, 0.8f, null, 1f,
                                            new TimeStampedEvent(0.3f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.ANTITHEUS_SHOOT.getTotalTime(), 0.01f))),
                                            new TimeStampedEvent(0.4f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.ANTITHEUS_SHOOT.getTotalTime(), 0.01f))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(5, 50).behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_SHOOT, 0.7f, 0.8f, null, 1f,
                                    new TimeStampedEvent(0.3f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.ANTITHEUS_SHOOT.getTotalTime(), 0.01f))),
                                    new TimeStampedEvent(0.4f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.ANTITHEUS_SHOOT.getTotalTime(), 0.01f))))))
            )
            //远距-2-3*2凋零头+跑居
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(20F).cooldown(280).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(5, 50).withinEyeHeight()
                                    .health(0.2F, HealthPoint.Comparator.LESS_RATIO).behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_SHOOT, 0.7f, 0.8f, null, 1f,
                                            new TimeStampedEvent(0.3f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.ANTITHEUS_SHOOT.getTotalTime(), 0.01f))),
                                            new TimeStampedEvent(0.4f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.ANTITHEUS_SHOOT.getTotalTime(), 0.01f))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ENDERSTEP_FORWARD, 0.2f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(true, 1, 1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinEyeHeight().behavior(customAttackAnimation(WOMAnimations.KATANA_FATAL_DRAW_DASH, 0.3f, 0.8f, null, 1f,
                                    new TimeStampedEvent(0.4f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.KATANA_FATAL_DRAW_DASH.getTotalTime()))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_BLINK, 0f, 0.8f, StunType.HOLD, 1.3f,
                                    new TimeStampedEvent(0.0f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.ANTITHEUS_ASCENDED_BLINK.getTotalTime(), 0.1f))))))
            )
            //远距-2-3*1凋零头+踢腿+黑洞+3*2凋零头
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(10F).cooldown(1200).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(5, 50).withinEyeHeight()
                                    .custom(worldLevelCheck(2))//第三轮回解锁
                                    .health(0.2F, HealthPoint.Comparator.LESS_RATIO)
                                    .behavior(customAttackAnimation(WOMAnimations.ENDERSTEP_FORWARD, 0.2f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SHOOT_WITHER_GHOST))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(false, 4, 4)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinEyeHeight().behavior(customAttackAnimation(WOMAnimations.ENDERBLASTER_TWOHAND_TISHNAW, 0.2f, 0.5f, StunType.LONG, 1.3f,
                                    new TimeStampedEvent(0.45f, PLAY_GROUND_SLAM),
                                    new TimeStampedEvent(0.65f, TIME_STAMPED_SHOOT_WITHER_GHOST))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(true, 3, 3)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(ROTATE_TO_TARGET))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.AGONY_CLAWSTRIKE, 0.0f, 0.6f, null, 1f,
                                    new TimeStampedEvent(0.9f, TIME_STAMPED_SHOOT_WITHER_GHOST))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(false, 0, 0)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENDED_BLACKHOLE, 0.15f, 0.8f, StunType.HOLD, 1f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(entityPatch -> {
                                if (entityPatch.getOriginal().level() instanceof ServerLevel serverLevel) {
                                    BlackHoleEntity blackHoleEntity = SMCEntities.BLACK_HOLE.get().create(serverLevel);
                                    entityPatch.playSound(WOMSounds.ANTITHEUS_BLACKKHOLE.get(), 0.9f, 0.9f);
                                    if (blackHoleEntity != null) {
                                        blackHoleEntity.setPos(entityPatch.getOriginal().position());
                                        serverLevel.addFreshEntity(blackHoleEntity);
                                    }
                                    CommandSourceStack css = entityPatch.getOriginal().createCommandSourceStack().withPermission(2).withSuppressedOutput();
                                    serverLevel.getServer().getCommands().performPrefixedCommand(css, "summon minecraft:area_effect_cloud ~ ~ ~ {Duration:100,ReapplicationDelay:2,Radius:3,RadiusPerTick:0.03,WaitTime:10,Effects:[{Id: 20,Amplifier: 5,Duration: 120},{Id: 7,Amplifier: 1,Duration: 120},{Id: 2,Amplifier: 1,Duration: 120}],Color:16731212}");
                                }
                            }))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_SHOOT, 0.7f, 0.8f, null, 1f,
                                    new TimeStampedEvent(0.3f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.ANTITHEUS_SHOOT.getTotalTime(), 0.01f))),
                                    new TimeStampedEvent(0.4f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.ANTITHEUS_SHOOT.getTotalTime(), 0.01f))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_SHOOT, 0.7f, 0.8f, null, 1f,
                                    new TimeStampedEvent(0.3f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.ANTITHEUS_SHOOT.getTotalTime(), 0.01f))),
                                    new TimeStampedEvent(0.4f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.ANTITHEUS_SHOOT.getTotalTime(), 0.01f))),
                                    new TimeStampedEvent(0.6f, TIME_STAMPED_SHOOT_WITHER_GHOST))))
            );
    /**
     * 大剑形态
     */
    public static final CombatBehaviors.Builder<HumanoidMobPatch<?>> GOLDEN_FLAME_GREAT_SWORD = CombatBehaviors.<HumanoidMobPatch<?>>builder()
            // 4/5血下——solar大招一段
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(9999999).cooldown(1200).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                    .behavior(customAttackAnimation(WOMAnimations.SOLAR_BRASERO, 0.4F, 0.8f, StunType.HOLD, 1f,
                                            new TimeStampedEvent(0.4f, timeStampedPlaySound(WOMSounds.SOLAR_HIT.get(), 1, 0.9F))))
                                    .health(0.85F, HealthPoint.Comparator.LESS_RATIO)
                                    .custom((humanoidMobPatch -> {
                                        if (humanoidMobPatch.getOriginal() instanceof GoldenFlame goldenFlame) {
                                            return !goldenFlame.isBlue();
                                        }
                                        return false;
                                    })))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                    .behavior(customAttackAnimation(WOMAnimations.SOLAR_BRASERO_OBSCURIDAD, 0.3F, 0.8f, StunType.LONG, 1f,
                                            new TimeStampedEvent(0.4f, timeStampedPlaySound(WOMSounds.SOLAR_HIT.get(), 1, 0.5F)))))
                            //进入蓝色阶段
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior((humanoidMobPatch -> {
                                if (humanoidMobPatch.getOriginal() instanceof GoldenFlame goldenFlame) {
                                    goldenFlame.setIsBlue(true);
                                }

                            })))
            )
            // 1/5血下——反神形态
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(9999999).cooldown(800).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                    .behavior(customAttackAnimation(WOMAnimations.ANTITHEUS_ASCENSION, 0.5f, 0.8f, null, 1f,
                                            new TimeStampedEvent(0.4f, timeStampedPlaySound(SoundEvents.WITHER_SHOOT, 1, 0.5F)),
                                            new TimeStampedEvent(0.17f, (livingEntityPatch -> {
                                                livingEntityPatch.getOriginal().addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 30, 9));
                                                livingEntityPatch.getOriginal().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1100, 1));
                                            })),
                                            //五雷轰顶
                                            new TimeStampedEvent(1.7f, (livingEntityPatch) -> {
                                                if (livingEntityPatch.getOriginal().level() instanceof ServerLevel serverLevel) {
                                                    for (int i = 0; i < 5; i++) {
                                                        EntityType.LIGHTNING_BOLT.spawn(serverLevel, livingEntityPatch.getOriginal().getOnPos(), MobSpawnType.MOB_SUMMONED);
                                                    }
                                                }
                                            })))
                                    //判断能否进入反神形态
                                    .custom(humanoidMobPatch -> humanoidMobPatch.getOriginal() instanceof GoldenFlame goldenFlame && goldenFlame.getAntiFormCooldown() == 0)
                                    .health(0.15F, HealthPoint.Comparator.LESS_RATIO))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(setPhase(0)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                    //进入反神形态
                                    .behavior((humanoidMobPatch -> {
                                        if (humanoidMobPatch.getOriginal() instanceof GoldenFlame goldenFlame) {
                                            goldenFlame.startAntiForm();
                                            playSound(SoundEvents.WITHER_SHOOT, 1, 0.5F).accept(humanoidMobPatch);
                                        }
                                    }))))
            //变招——1蓄（phase1）
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(100F).cooldown(40).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                    .custom(phaseCheck(1, 1)).behavior(setPhase(0)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_CHARGED_ATTACK_1, 0.3f, 0.65f, StunType.SHORT, 1.3f,
                                    new TimeStampedEvent(1.15f, PLAY_GROUND_SLAM))))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(10000F).cooldown(120).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                    .custom(phaseCheck(1, 1)).behavior(setPhase(0))
                                    .health(0.85F, HealthPoint.Comparator.LESS_RATIO))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_CHARGED_ATTACK_1, 0.2f, 0.65f, StunType.SHORT, 1f,
                                    new TimeStampedEvent(1.0f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.TORMENT_CHARGED_ATTACK_1.getTotalTime()))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_CHARGED_ATTACK_2, 0.2F, 1, StunType.KNOCKDOWN, 1,
                                    new TimeStampedEvent(1.6f, PLAY_GROUND_SLAM))))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(50000F).cooldown(120).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                    .custom(phaseCheck(1, 1)).behavior(setPhase(0))
                                    .health(0.64F, HealthPoint.Comparator.LESS_RATIO))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_CHARGED_ATTACK_1, 0.2f, 0.65f, StunType.LONG, 1f,
                                    new TimeStampedEvent(1.0f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.TORMENT_CHARGED_ATTACK_1.getTotalTime()))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_BERSERK_AIRSLAM, 0.05f, 0.7f, StunType.KNOCKDOWN, 1.3f,
                                    new TimeStampedEvent(0.1f, timeStampedPlaySound(EpicFightSounds.ENTITY_MOVE.get(), 1, 0.8F)),
                                    new TimeStampedEvent(0.6f, PLAY_GROUND_SLAM))))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(50000F).cooldown(120).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                    .custom(phaseCheck(1, 1)).behavior(setPhase(0))
                                    .health(0.64F, HealthPoint.Comparator.LESS_RATIO))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_CHARGED_ATTACK_1, 0.2f, 0.65f, StunType.LONG, 1f,
                                    new TimeStampedEvent(1.0f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.TORMENT_CHARGED_ATTACK_1.getTotalTime()))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_CHARGED_ATTACK_3, 0.3f, 0.6f, StunType.KNOCKDOWN, 1f,
                                    new TimeStampedEvent(0.15f, timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 1)),
                                    new TimeStampedEvent(1.1f, PLAY_GROUND_SLAM))))
            )
            //变招——3蓄（phase1）
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(200F).cooldown(40).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                    .custom(phaseCheck(1, 1)).behavior(setPhase(0)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_CHARGED_ATTACK_3, 0.3f, 0.6f, StunType.KNOCKDOWN, 1f,
                                    new TimeStampedEvent(0.15f, timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 1)),
                                    new TimeStampedEvent(1.1f, PLAY_GROUND_SLAM))))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(30000F).cooldown(160).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                    .custom(phaseCheck(1, 1)).behavior(setPhase(0))
                                    .health(0.85F, HealthPoint.Comparator.LESS_RATIO))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_CHARGED_ATTACK_3, 0.3f, 0.6f, null, 1f,
                                    new TimeStampedEvent(0.15f, timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 1)),
                                    new TimeStampedEvent(0.6f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.TORMENT_CHARGED_ATTACK_3.getTotalTime()))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_BERSERK_AIRSLAM, 0.05f, 0.7f, StunType.KNOCKDOWN, 1.3f,
                                    new TimeStampedEvent(0.1f, timeStampedPlaySound(EpicFightSounds.ENTITY_MOVE.get(), 1, 0.8F)),
                                    new TimeStampedEvent(0.6f, PLAY_GROUND_SLAM))))
            )
            //变招——黄火+蓝火（phase2）
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(5000F).cooldown(800).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                    .custom(phaseCheck(2, 2)).behavior(setPhase(0))
                                    .health(0.85F, HealthPoint.Comparator.LESS_RATIO))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_1_POLVORA, 0.3f, 0.8f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.2f, PLAY_SOLAR_HIT))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_3_POLVORA, 0.7f, 1, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, PLAY_SOLAR_HIT),
                                    new TimeStampedEvent(0.6f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.SOLAR_AUTO_3_POLVORA.getTotalTime()))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_3_POLVORA, 0.15f, 1, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, PLAY_SOLAR_HIT),
                                    new TimeStampedEvent(0.6f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.SOLAR_AUTO_3_POLVORA.getTotalTime()))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_3_POLVORA, 0.15f, 1, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, PLAY_SOLAR_HIT),
                                    new TimeStampedEvent(0.6f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.SOLAR_AUTO_3_POLVORA.getTotalTime()))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(false, 1, 1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(ROTATE_TO_TARGET))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_BRASERO, 0.1F, 0.8f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.4f, timeStampedPlaySound(WOMSounds.SOLAR_HIT.get(), 1, 0.9F)))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_BRASERO_OBSCURIDAD, 0.3F, 0.8f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.4f, timeStampedPlaySound(WOMSounds.SOLAR_HIT.get(), 1, 0.5F)),
                                    new TimeStampedEvent(0.4F, (livingEntityPatch)  ->  {
                                        if(livingEntityPatch.getOriginal() instanceof GoldenFlame goldenFlame){
                                            goldenFlame.setFlameCircleLifeTimeAndStart(300);
                                        }
                                    }))))
            )
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(8000F).cooldown(1000).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                    .custom(phaseCheck(2, 2)).behavior(setPhase(0))
                                    .health(0.64F, HealthPoint.Comparator.LESS_RATIO))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_IMPACTO, 0.2f, 0.5f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.4f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_3, 0.4f, 0.9f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_3, 0.06f, 0.9f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_3, 0.06f, 0.9f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(false, 1, 1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(ROTATE_TO_TARGET))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_BRASERO, 0.1F, 0.8f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.4f, timeStampedPlaySound(WOMSounds.SOLAR_HIT.get(), 1, 0.9F)))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_SOLAR_BRASERO_CREMATORIO))
            )
            //一阶段——平a
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(0.5F).cooldown(80).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_AUTO_1, 0.3f, 0.8f))
                                    .withinDistance(0, 3).withinEyeHeight().custom(phaseCheck(0, 0)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(setPhase(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_AUTO_2, 0.1f, 0.8f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_AUTO_3, 0.0f, 0.7f, null, 1,
                                    new TimeStampedEvent(0.4f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.TORMENT_AUTO_3.getTotalTime()))))))
            )
            //保险——无阶段检测+切换0阶段
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(0.2F).cooldown(50).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_1, 0.2f))
                                    .withinDistance(0, 3).withinEyeHeight())
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(setPhase(0)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_2, 0.2f, 0.9f, StunType.SHORT, 1,
                                    new TimeStampedEvent(0.6f, PLAY_GROUND_SLAM))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_3, 0.2f, 0.9f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_4, 0.2f, 0.9f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_4_POLVORA, 0.2f, 0.9f, StunType.SHORT, 1,
                                    new TimeStampedEvent(0.4f, PLAY_GROUND_SLAM))))
            )
            //二阶段——平a——phase1
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(2F).cooldown(160).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_1, 0.2f))
                                    .withinDistance(0, 3).custom(phaseCheck(0, 0))
                                    .health(0.85f, HealthPoint.Comparator.LESS_RATIO).withinEyeHeight())
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(setPhase(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_2, 0.2f, 0.9f, null, 1,
                                    new TimeStampedEvent(0.6f, PLAY_GROUND_SLAM))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_3, 0.2f, 0.9f, null, 1,
                                    new TimeStampedEvent(0.6f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.SOLAR_AUTO_3.getTotalTime()))))))
            )
            //二阶段——平a——phase2
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(2F).cooldown(320).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_1, 0.2f))
                                    .withinDistance(0, 3).custom(phaseCheck(0, 0))
                                    .health(0.85f, HealthPoint.Comparator.LESS_RATIO).withinEyeHeight())
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(setPhase(2)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_2, 0.2f, 0.9f, null, 1,
                                    new TimeStampedEvent(0.6f, PLAY_GROUND_SLAM))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_3, 0.2f, 0.9f, null, 1,
                                    new TimeStampedEvent(0.6f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.SOLAR_AUTO_3.getTotalTime()))))))
            )
            //一阶段-一蓄-1
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(0.1F).cooldown(100).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(0, 4).withinEyeHeight().custom(CAN_CHARGING).custom(phaseCheck(0, 0))
                                    .behavior(customAttackAnimation(WOMAnimations.ENDERSTEP_BACKWARD, 0.1f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(START_CHARGE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(15, 0.2F, 0.5F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(STOP_CHARGE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_AUTO_3, 0.3f, 1.2f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_AUTO_4, 0, 0.7f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.5f, PLAY_GROUND_SLAM))))
            )
            //一阶段-一蓄-2
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(0.1F).cooldown(100).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(0, 4).withinEyeHeight().custom(CAN_CHARGING).custom(phaseCheck(0, 0))
                                    .behavior(customAttackAnimation(WOMAnimations.ENDERSTEP_BACKWARD, 0.1f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(START_CHARGE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(15, 0.2F, -0.5F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(STOP_CHARGE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_AUTO_3, 0.3f, 1.2f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_BERSERK_AIRSLAM, 0.2f, 0.7f, StunType.KNOCKDOWN, 1.3f,
                                    new TimeStampedEvent(0.6f, PLAY_GROUND_SLAM))))
            )
            //二阶段-二蓄-1
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(10F).cooldown(160).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(0, 6).withinEyeHeight().custom(CAN_CHARGING).custom(phaseCheck(0, 0))
                                    .behavior(customAttackAnimation(WOMAnimations.ENDERSTEP_BACKWARD, 0.1f, 1, null, 1,
                                            new TimeStampedEvent(0, (livingEntityPatch -> livingEntityPatch.getOriginal().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 50, 0))))
                                    )))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(START_CHARGE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(20, -0.4F, 0.8F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(20, 0.6F, -0.6F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(STOP_CHARGE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_CHARGED_ATTACK_2, 0.4f, 0.8f, StunType.LONG, 1f)))
                            //重置状态，防止其他地方的TormentAuto4击中后影响变招
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior((humanoidMobPatch) -> {
                                if (humanoidMobPatch instanceof GoldenFlamePatch patch) {
                                    patch.resetOnTormentAuto4Hit();
                                }
                            }))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_AUTO_4, 0, 0.7f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.5f, PLAY_GROUND_SLAM))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_3, 0.2f, 0.9f))
                                    .custom(humanoidMobPatch -> {
                                        if (humanoidMobPatch instanceof GoldenFlamePatch goldenFlame) {
                                            //击中后才能变招
                                            if (goldenFlame.isOnTormentAuto4Hit()) {
                                                goldenFlame.resetOnTormentAuto4Hit();
                                                return true;
                                            }
                                        }
                                        return false;
                                    }))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_CHARGED_ATTACK_3, 0.3f, 0.6f, StunType.KNOCKDOWN, 1f,
                                    new TimeStampedEvent(0.15f, timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 1)),
                                    new TimeStampedEvent(1.1f, PLAY_GROUND_SLAM))))
            )
            //二阶段-二蓄-2
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(10F).cooldown(400).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(0, 6).withinEyeHeight()
                                    .health(0.8F, HealthPoint.Comparator.LESS_RATIO).custom(CAN_CHARGING).custom(phaseCheck(0, 0))
                                    .behavior(customAttackAnimation(WOMAnimations.ENDERSTEP_BACKWARD, 0.1f, 1, null, 1,
                                            new TimeStampedEvent(0, (livingEntityPatch -> livingEntityPatch.getOriginal().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 50, 0))))
                                    )))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(START_CHARGE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(20, 0.6F, -0.8F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(20, -0.4F, 0.6F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(STOP_CHARGE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_CHARGED_ATTACK_2, 0.5f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_AUTO_4, 0, 0.7f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.5f, PLAY_GROUND_SLAM))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_AUTO_4, 0.2F, 1, StunType.KNOCKDOWN, 1,
                                    new TimeStampedEvent(1.6f, PLAY_GROUND_SLAM))))
            )
            //二阶段——瞬闪1
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(100F).cooldown(700).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ENDERSTEP_BACKWARD, 0.1f))
                                    .custom(worldLevelCheck(2))//第三轮回解锁
                                    .health(0.85f, HealthPoint.Comparator.LESS_RATIO)
                                    .withinDistance(0, 3).withinEyeHeight().custom(phaseCheck(0, 0))
                                    .custom(attackLevelCheck(1, 2)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.AGONY_RISING_EAGLE, 0.3f, 0.4f, StunType.SHORT, 1f,
                                    new TimeStampedEvent(0.17f, (livingEntityPatch -> livingEntityPatch.getOriginal().addEffect(new MobEffectInstance(MobEffects.LEVITATION, 30)))),
                                    new TimeStampedEvent(0.0f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.AGONY_RISING_EAGLE.getTotalTime(), 0.17f))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToUp(false, 6, 6)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_HORNO, 0.15f, 0.8f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.4f, (livingEntityPatch -> livingEntityPatch.playSound(SoundEvents.TOTEM_USE, 1, 1)))
                            )))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(false, 7, 7)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_DINAMITA, 0.2f, 0.7f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.1F, PLAY_SOLAR_HIT),
                                    new TimeStampedEvent(0.6f, PLAY_GROUND_SLAM)
                            )))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(false, 0, 1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_BRASERO, 0.1F, 0.8f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.4f, timeStampedPlaySound(WOMSounds.SOLAR_HIT.get(), 1, 0.9F)))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_BRASERO_OBSCURIDAD, 0.3F, 0.8f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.4f, timeStampedPlaySound(WOMSounds.SOLAR_HIT.get(), 1, 0.5F)),
                                    new TimeStampedEvent(0.4F, (livingEntityPatch)  ->  {
                                        if(livingEntityPatch.getOriginal() instanceof GoldenFlame goldenFlame){
                                            goldenFlame.setFlameCircleLifeTimeAndStart(300);
                                        }
                                    }))))
            )
            //三阶段——三蓄-1
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(15F).cooldown(360).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(0, 8).withinEyeHeight()
                                    .health(0.64F, HealthPoint.Comparator.LESS_RATIO).custom(CAN_CHARGING).custom(phaseCheck(0, 0))
                                    .behavior(customAttackAnimation(WOMAnimations.ENDERSTEP_BACKWARD, 0.1f, 1, null, 1,
                                            new TimeStampedEvent(0, (livingEntityPatch -> livingEntityPatch.getOriginal().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1))))
                                    )))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(setPhase(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(START_CHARGE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(30, 0.6F, 0.8F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(30, -0.4F, -0.4F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(30, 0.2F, -0.2F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(STOP_CHARGE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_DASH, 0.1f, 0.7f, StunType.SHORT, 1f,
                                    new TimeStampedEvent(0.3f, PLAY_GROUND_SLAM))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(Animations.GREATSWORD_AUTO2, 0.2f, 0.9f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_AUTO_4, 0, 0.7f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.9f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.TORMENT_CHARGED_ATTACK_1.getTotalTime()))),
                                    new TimeStampedEvent(0.5f, PLAY_GROUND_SLAM))))
            )
            //三阶段——三蓄-2
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(15F).cooldown(360).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(0, 8)
                                    .withinEyeHeight().health(0.64F, HealthPoint.Comparator.LESS_RATIO).custom(CAN_CHARGING).custom(phaseCheck(0, 0))
                                    .behavior(customAttackAnimation(WOMAnimations.ENDERSTEP_BACKWARD, 0.1f, 1, null, 1,
                                            new TimeStampedEvent(0, (livingEntityPatch -> livingEntityPatch.getOriginal().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1))))
                                    )))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(setPhase(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(START_CHARGE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(30, -0.6F, -0.8F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(30, 0.4F, 0.4F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(30, 0.2F, 0.2F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(STOP_CHARGE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_DASH, 0.1f, 0.7f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.3f, PLAY_GROUND_SLAM))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_BERSERK_DASH, 0.3f, 0.6f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.3f, PLAY_GROUND_SLAM),
                                    new TimeStampedEvent(0.6f, PLAY_GROUND_SLAM),
                                    new TimeStampedEvent(1.15f, PLAY_GROUND_SLAM),
                                    new TimeStampedEvent(1.4f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.TORMENT_BERSERK_DASH.getTotalTime())))
                            )))
            )
            //三阶段-读攻击1
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(400F).cooldown(1200).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ENDERSTEP_BACKWARD, 0.1f, 1, null, 1,
                                            new TimeStampedEvent(0.2f, (livingEntityPatch -> {
                                                livingEntityPatch.playSound(EpicFightSounds.ENTITY_MOVE.get(), 1f, 1f);
                                            }))))
                                    .custom(worldLevelCheck(2))//第三轮回解锁
                                    .health(0.64f, HealthPoint.Comparator.LESS_RATIO)
                                    .withinDistance(0, 3).withinEyeHeight().custom(phaseCheck(0, 0))
                                    .custom(attackLevelCheck(1, 2)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.AGONY_RISING_EAGLE, 0.3f, 0.4f, StunType.SHORT, 1f,
                                    new TimeStampedEvent(0.15f, (livingEntityPatch -> livingEntityPatch.playSound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 1))),
                                    new TimeStampedEvent(0.2f, (livingEntityPatch -> livingEntityPatch.getOriginal().addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20)))),
                                    new TimeStampedEvent(0.0f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.AGONY_RISING_EAGLE.getTotalTime(), 0.2f))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_CHARGED_ATTACK_1, 0, 1, null, 1f,
                                    new TimeStampedEvent(0.0f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.TORMENT_CHARGED_ATTACK_1.getTotalTime(), 0.7f))),
                                    new TimeStampedEvent(0.3f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.TORMENT_CHARGED_ATTACK_1.getTotalTime())))
                            )))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(Animations.WRATHFUL_LIGHTING, 0.1f, 1, null, 1f,
                                    new TimeStampedEvent(0.1f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(Animations.WRATHFUL_LIGHTING.getTotalTime()))))))

                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_HORNO, 0.15f, 0.8f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.SOLAR_HORNO.getTotalTime(), 0.2f))),
                                    new TimeStampedEvent(0.4f, (livingEntityPatch -> livingEntityPatch.playSound(SoundEvents.TOTEM_USE, 1, 1))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_1, 0.2f, 0.6f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_IMPACTO, 0.2f, 0.5f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.4f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_3, 0.4f, 0.9f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_3, 0.06f, 0.9f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_3, 0.06f, 0.9f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(true, 1, 1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_BRASERO, 0.1f, 0.8f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.4f, timeStampedPlaySound(WOMSounds.SOLAR_HIT.get(), 1, 0.9F)))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_SOLAR_BRASERO_CREMATORIO))
            )
            //三阶段——瞬闪2
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(160F).cooldown(800).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ENDERSTEP_BACKWARD, 0.1f))
                                    .health(0.64f, HealthPoint.Comparator.LESS_RATIO)
                                    .withinDistance(0, 3).withinEyeHeight()
                                    .custom(attackLevelCheck(1, 3)).custom(phaseCheck(0, 0)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(setPhase(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.AGONY_RISING_EAGLE, 0.3f, 0.4f, StunType.SHORT, 1f,
                                    new TimeStampedEvent(0.17f, (livingEntityPatch -> livingEntityPatch.getOriginal().addEffect(new MobEffectInstance(MobEffects.LEVITATION, 30)))),
                                    new TimeStampedEvent(0.0f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.AGONY_RISING_EAGLE.getTotalTime(), 0.17f))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToUp(false, 6, 6)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_HORNO, 0.15f, 0.8f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.4f, (livingEntityPatch -> livingEntityPatch.playSound(SoundEvents.TOTEM_USE, 1, 1)))
                            )))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(false, 7, 7)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_DINAMITA, 0.2f, 0.7f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.1F, PLAY_SOLAR_HIT),
                                    new TimeStampedEvent(0.6f, PLAY_GROUND_SLAM)
                            )))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(true, 1, 2)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_2, 0.1f, 0.9f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.6f, PLAY_GROUND_SLAM))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_3, 0.2f, 0.9f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(false, 6, 6)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(ROTATE_TO_TARGET))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_DASH, 0.1f, 0.7f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.3f, PLAY_GROUND_SLAM))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_1, 0.2f, 0.7f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.5f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_1.getTotalTime())))
                            )))
            )
            //四阶段-瞬闪3
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(500F).cooldown(1400).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ENDERSTEP_BACKWARD, 0.1f))
                                    .custom(worldLevelCheck(2))//第三轮回解锁
                                    .health(0.36f, HealthPoint.Comparator.LESS_RATIO)
                                    .withinDistance(0, 3).withinEyeHeight().custom(phaseCheck(0, 0))
                                    .custom(attackLevelCheck(1, 3)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.ENDERSTEP_RIGHT, 0.2f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(false, 1, 1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))

                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_2, 0.1f, 0.9f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.6f, PLAY_GROUND_SLAM))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_4, 0.2f, 0.9f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_AUTO_4_POLVORA, 0.2f, 0.9f, StunType.FALL, 1f,
                                    new TimeStampedEvent(0.3f, PLAY_GROUND_SLAM))))

                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(true, 3, 3)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_IMPACTO, 0.2f, 0.5f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.4f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(chaseTarget(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_3, 0.4f, 0.9f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_3, 0.06f, 0.9f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_OBSCURIDAD_AUTO_3, 0.06f, 0.9f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.2f, (livingEntityPatch -> {
                                        timeStampedPlaySound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 0.5F).accept(livingEntityPatch);
                                        livingEntityPatch.playSound(WOMSounds.SOLAR_HIT.get(), 1, 1);
                                    })))))

                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.BIPED_HIT_SHORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(Animations.BIPED_HIT_SHORT, 0.2f, 1, null, 1,
                                    new TimeStampedEvent(0, (livingEntityPatch -> livingEntityPatch.getOriginal().addEffect(new MobEffectInstance(MobEffects.LEVITATION, 30)))))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToUp(false, 6, 6)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_HORNO, 0.15f, 0.8f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.4f, (livingEntityPatch -> livingEntityPatch.playSound(SoundEvents.TOTEM_USE, 1, 1))),
                                    new TimeStampedEvent(0.7f, (livingEntityPatch -> livingEntityPatch.getAnimator().getPlayerFor(null).setElapsedTime(WOMAnimations.SOLAR_HORNO.getTotalTime())))
                            )))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.SOLAR_BRASERO_INFIERNO, 0.4f, 0.8f, StunType.HOLD, 2f,
                                    new TimeStampedEvent(0.65F, (livingEntityPatch)  ->  {
                                        if(livingEntityPatch.getOriginal() instanceof GoldenFlame goldenFlame){
                                            goldenFlame.setFlameCircleLifeTimeAndStart(900);
                                        }
                            }))))
            )
            //四阶段-四蓄力
            .newBehaviorSeries(
                    CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder().weight(20F).cooldown(240).canBeInterrupted(false).looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().withinDistance(0, 8).withinEyeHeight().custom(phaseCheck(0, 0))
                                    .health(0.36F, HealthPoint.Comparator.LESS_RATIO).custom(CAN_CHARGING)
                                    .behavior(customAttackAnimation(WOMAnimations.ENDERSTEP_BACKWARD, 0.1f, 1, null, 1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(humanoidMobPatch -> humanoidMobPatch.getOriginal().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 130, 2))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(setPhase(1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(START_CHARGE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(30, -0.6F, 0.8F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(30, 0.4F, -0.6F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(30, -0.2F, -0.4F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(wander(30, 0.1F, 0.2F)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(STOP_CHARGE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_DASH, 0.1f, 0.7f, StunType.LONG, 1f,
                                    new TimeStampedEvent(0.3f, PLAY_GROUND_SLAM))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(Animations.GREATSWORD_AUTO2, 0.3f, 0.9f)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(customAttackAnimation(WOMAnimations.TORMENT_BERSERK_DASH, 0.3f, 0.6f, StunType.HOLD, 1f,
                                    new TimeStampedEvent(0.3f, PLAY_GROUND_SLAM),
                                    new TimeStampedEvent(0.6f, PLAY_GROUND_SLAM),
                                    new TimeStampedEvent(1.15f, PLAY_GROUND_SLAM))))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_HIDE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(PLAY_TIME_TRAVEL_SOUND))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(RANDOM_TELEPORT))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(teleportToFront(false, 1, 1)))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().animationBehavior(Animations.ENDERMAN_TP_EMERGENCE))
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder().behavior(SET_NOT_HIDE))
            );
}