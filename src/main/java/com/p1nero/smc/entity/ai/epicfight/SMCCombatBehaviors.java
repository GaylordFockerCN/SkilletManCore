package com.p1nero.smc.entity.ai.epicfight;

import com.p1nero.smc.archive.SMCArchiveManager;
import com.p1nero.smc.entity.api.IWanderableEntity;
import com.p1nero.smc.entity.ai.epicfight.api.*;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.packet.clientbound.AddEntityAfterImageParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.damagesource.StunType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 提供一些通用预设
 */
public class SMCCombatBehaviors {

    /**
     * 设置自定义阶段
     */
    public static <T extends MobPatch<?>> Consumer<T> setPhase(int phase) {
        return (patch) -> {
            if (patch instanceof IMultiPhaseEntityPatch phaseEntityPatch) {
                phaseEntityPatch.setPhase(phase);
            }
        };
    }

    public static final Consumer<HumanoidMobPatch<?>> LEFT_ENTITY_AFTER_IMAGE = (humanoidMobPatch -> {
        PacketRelay.sendToAll(SMCPacketHandler.INSTANCE, new AddEntityAfterImageParticle(humanoidMobPatch.getOriginal().getId()));
        humanoidMobPatch.playSound(EpicFightSounds.ENTITY_MOVE.get(), 0.5F, 1, 1);
    });

    public static final Consumer<HumanoidMobPatch<?>> SUMMON_LIGHTNING = (humanoidMobPatch -> {
        if (humanoidMobPatch.getOriginal().level() instanceof ServerLevel serverLevel) {
            EntityType.LIGHTNING_BOLT.spawn(serverLevel, humanoidMobPatch.getOriginal().getOnPos(), MobSpawnType.MOB_SUMMONED);
        }
    });

    public static final Consumer<LivingEntityPatch<?>> PLAY_SOLAR_BRASERO_CREMATORIO_PARTICLE = (entityPatch -> {
        OpenMatrix4f transformMatrix = entityPatch.getArmature().getBindedTransformFor(entityPatch.getAnimator().getPose(0.0F), Armatures.BIPED.toolL);
        transformMatrix.translate(new Vec3f(0.0F, 0.0F, 0.0F));
        OpenMatrix4f CORRECTION = (new OpenMatrix4f()).rotate(-((float)Math.toRadians((entityPatch.getOriginal().yRotO - 180.0F))), new Vec3f(0.0F, 1.0F, 0.0F));
        OpenMatrix4f.mul(CORRECTION, transformMatrix, transformMatrix);
        int n = 12;
        float t = 0.1F;

        for(int i = 0; i < n; ++i) {
            double theta = 6.283185 * (new Random()).nextDouble();
            double phi = Math.acos((new Random()).nextDouble());
            double x = t * Math.sin(phi) * Math.cos(theta);
            double y = t * Math.sin(phi) * Math.sin(theta);
            double z = t * Math.cos(phi);
            Vec3f direction = new Vec3f((float)x, (float)y, (float)z);
            OpenMatrix4f rotation = (new OpenMatrix4f()).rotate(-((float)Math.toRadians((entityPatch.getOriginal()).yBodyRotO)), new Vec3f(0.0F, 1.0F, 0.0F));
            rotation.translate(new Vec3f(0.0F, 0.0F, 0.2F));
            OpenMatrix4f.transform3v(rotation, direction, direction);
            if(entityPatch.getOriginal().level() instanceof ServerLevel serverLevel){
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, transformMatrix.m30 + entityPatch.getOriginal().getX(), transformMatrix.m31 + entityPatch.getOriginal().getY(), transformMatrix.m32 + entityPatch.getOriginal().getZ(), 0, direction.x, direction.y, direction.z, 1);
                if ((new Random()).nextBoolean()) {
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, transformMatrix.m30 + entityPatch.getOriginal().getX(), transformMatrix.m31 + entityPatch.getOriginal().getY(), transformMatrix.m32 + entityPatch.getOriginal().getZ(), 0, (((new Random()).nextFloat() - 0.5F) * 0.05F), (((new Random()).nextFloat() - 0.5F) * 0.05F), (((new Random()).nextFloat() - 0.5F) * 0.05F), 1);
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, transformMatrix.m30 + entityPatch.getOriginal().getX(), transformMatrix.m31 + entityPatch.getOriginal().getY(), transformMatrix.m32 + (entityPatch.getOriginal()).getZ(), 0, 0.0, ((new Random()).nextFloat() * 0.05F), 0.0, 1);
                }
            }
        }
    });

    public static <T extends MobPatch<?>> Consumer<T> wander(int strafingTime, float forward, float clockwise) {
        return (patch) -> {
            if (patch.getOriginal() instanceof IWanderableEntity wanderableEntity && !patch.isLogicalClient()) {
                wanderableEntity.setStrafingTime(strafingTime);
                wanderableEntity.setStrafingForward(forward);
                wanderableEntity.setStrafingClockwise(clockwise);
            }
        };
    }

    /**
     * entityPatch的playSound似乎不能修改音调
     */
    public static <T extends MobPatch<?>> Consumer<T> playSound(SoundEvent sound, float volume, float pitch) {
        return (patch) -> {
            LivingEntity entity = patch.getOriginal();
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.BLOCKS, volume, pitch);
        };
    }

    /**
     * entityPatch的playSound似乎不能修改音调
     */
    public static <T extends LivingEntityPatch<?>> Consumer<T> timeStampedPlaySound(SoundEvent sound, float volume, float pitch) {
        return (patch) -> {
            LivingEntity entity = patch.getOriginal();
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.BLOCKS, volume, pitch);
        };
    }

    /**
     * 播放动画，带ConvertTime也带变速
     */
    public static <T extends MobPatch<?>> Consumer<T> customAttackAnimation(StaticAnimation animation, float convertTime, float attackSpeed, @Nullable StunType stunType, float damage, TimeStampedEvent... events) {
        return (patch) -> {
            if (patch instanceof IModifyAttackSpeedEntityPatch entity) {
                entity.setAttackSpeed(attackSpeed);
            }
            if (patch instanceof IModifyStunTypeEntityPatch entity) {
                entity.setStunType(stunType);
            }
            if (patch instanceof IModifyAttackDamageEntityPatch entity) {
                entity.setNewDamage(damage);
            }
            if (patch instanceof ITimeEventListEntityPatch entity) {
                entity.clearEvents();
                for (TimeStampedEvent event : events) {
                    event.resetExecuted();
                    entity.addEvent(event);
                }
            }
            patch.playAnimationSynchronized(animation, convertTime);
        };
    }

    /**
     * 播放动画，带ConvertTime也带变速
     */
    public static <T extends MobPatch<?>> Consumer<T> customAttackAnimation(StaticAnimation animation, float convertTime, float attackSpeed, @Nullable StunType stunType, float damage) {
        return (patch) -> {
            if (patch instanceof IModifyAttackSpeedEntityPatch entity) {
                entity.setAttackSpeed(attackSpeed);
            }
            if (patch instanceof IModifyStunTypeEntityPatch entity) {
                entity.setStunType(stunType);
            }
            if (patch instanceof IModifyAttackDamageEntityPatch entity) {
                entity.setNewDamage(damage);
            }
            patch.playAnimationSynchronized(animation, convertTime);
        };
    }

    /**
     * 播放动画，带ConvertTime也带变速
     */
    public static <T extends MobPatch<?>> Consumer<T> customAttackAnimation(StaticAnimation animation, float convertTime, float attackSpeed, @Nullable StunType stunType) {
        return customAttackAnimation(animation, convertTime, attackSpeed, stunType, 0);
    }

    /**
     * 播放动画，带ConvertTime也带变速
     */
    public static <T extends MobPatch<?>> Consumer<T> customAttackAnimation(StaticAnimation animation, float convertTime, float attackSpeed) {
        return customAttackAnimation(animation, convertTime, attackSpeed, null, 0);
    }

    /**
     * 播放动画，带ConvertTime不带变速
     */
    public static <T extends MobPatch<?>> Consumer<T> customAttackAnimation(StaticAnimation animation, float convertTime) {
        return customAttackAnimation(animation, convertTime, 1.0F);
    }

    /**
     * 转向目标
     */
    public static final Consumer<HumanoidMobPatch<?>> ROTATE_TO_TARGET = (humanoidMobPatch -> {
        if (humanoidMobPatch.getTarget() != null) {
            humanoidMobPatch.rotateTo(humanoidMobPatch.getTarget(), 30F, true);
        }
    });

    /**
     * 瞬移到目标边上（可能会到身后）
     */
    public static final Consumer<HumanoidMobPatch<?>> MOVE_TO_TARGET = (humanoidMobPatch -> {
        if (humanoidMobPatch.getTarget() != null) {
            humanoidMobPatch.getOriginal().moveTo(humanoidMobPatch.getTarget().position());
            humanoidMobPatch.rotateTo(humanoidMobPatch.getTarget(), 30F, true);
        }
    });

    /**
     * 随机方向跨步
     */
    public static final Consumer<HumanoidMobPatch<?>> RANDOM_ENDER_STEP = (humanoidMobPatch -> {
        List<StaticAnimation> steps = new ArrayList<>();
        steps.add(WOMAnimations.ENDERSTEP_BACKWARD);
        steps.add(WOMAnimations.ENDERSTEP_FORWARD);
        steps.add(WOMAnimations.ENDERSTEP_LEFT);
        steps.add(WOMAnimations.ENDERSTEP_RIGHT);
        humanoidMobPatch.playAnimationSynchronized(steps.get(new Random().nextInt(steps.size())), 0.0F);
    });


    /**
     * 传送到玩家前面
     *
     * @param invert invert为true则传背后
     */
    public static <T extends MobPatch<?>> Consumer<T> teleportToFront(boolean invert, int minDis, int maxDis) {
        return (humanoidMobPatch -> {
            if (humanoidMobPatch.getTarget() != null) {
                LivingEntity target = humanoidMobPatch.getTarget();
                Vec3 targetPos = target.position();
                Vec3 view = target.getViewVector(1.0F);
                if (invert) {
                    view = view.scale(-1);
                }
                Vec3 dir = new Vec3(view.x, 0, view.z);
                Vec3 toTeleport;
                if (minDis == maxDis) {
                    toTeleport = targetPos.add(dir.normalize().scale(minDis));//传送范围
                } else {
                    toTeleport = targetPos.add(dir.normalize().scale(target.getRandom().nextInt(minDis, maxDis)));//传送范围
                }
                humanoidMobPatch.getOriginal().teleportTo(toTeleport.x, toTeleport.y, toTeleport.z);
                humanoidMobPatch.getOriginal().getLookControl().setLookAt(humanoidMobPatch.getTarget());
            }
        });
    }


    /**
     * 传送到玩家头上
     *
     * @param invert invert为true则传底下
     */
    public static <T extends MobPatch<?>> Consumer<T> teleportToUp(boolean invert, int minDis, int maxDis) {
        return (humanoidMobPatch -> {
            if (humanoidMobPatch.getTarget() != null) {
                LivingEntity target = humanoidMobPatch.getTarget();
                Vec3 targetPos = target.position();
                Vec3 dir = new Vec3(0, invert ? -1 : 1, 0);
                Vec3 toTeleport;
                if (minDis == maxDis) {
                    toTeleport = targetPos.add(dir.normalize().scale(minDis));//传送范围
                } else {
                    toTeleport = targetPos.add(dir.normalize().scale(target.getRandom().nextInt(minDis, maxDis)));//传送范围
                }
                humanoidMobPatch.getOriginal().teleportTo(toTeleport.x, toTeleport.y, toTeleport.z);
                humanoidMobPatch.getOriginal().getLookControl().setLookAt(humanoidMobPatch.getTarget());
            }
        });
    }

    /**
     * 追击玩家
     *
     * @param dis 追击到距离玩家多远的距离
     */
    public static <T extends MobPatch<?>> Consumer<T> chaseTarget(float dis) {
        return (humanoidMobPatch -> {
            if (humanoidMobPatch.getTarget() != null) {
                LivingEntity target = humanoidMobPatch.getTarget();
                LivingEntity self = humanoidMobPatch.getOriginal();
                Vec3 toTeleport = target.position().add(self.position().subtract(target.position()).normalize().scale(dis));
                humanoidMobPatch.getOriginal().moveTo(toTeleport.x, toTeleport.y, toTeleport.z);
                humanoidMobPatch.getOriginal().getLookControl().setLookAt(humanoidMobPatch.getTarget());
            }
        });
    }


    /**
     * 随机传送到玩家边上
     *
     * @param dis 传送点到玩家的距离
     */
    public static Consumer<HumanoidMobPatch<?>> randomTeleport(int dis) {
        return (humanoidMobPatch) -> {
            if (humanoidMobPatch.getTarget() != null) {
                LivingEntity target = humanoidMobPatch.getTarget();
                Vec3 targetPos = target.position();
                double angle = target.getRandom().nextDouble() * 2 * Math.PI;
                double newX = targetPos.x + dis * Math.cos(angle);
                double newZ = targetPos.z + dis * Math.sin(angle);
                Vec3 toTeleport = new Vec3(newX, targetPos.y, newZ);
                humanoidMobPatch.getOriginal().setPos(toTeleport);
                humanoidMobPatch.getOriginal().getLookControl().setLookAt(humanoidMobPatch.getTarget());
            }
        };
    }

    /**
     * 随机传送到玩家边上
     */
    public static final Consumer<HumanoidMobPatch<?>> RANDOM_TELEPORT = (humanoidMobPatch -> {
        if (humanoidMobPatch.getTarget() != null) {
            LivingEntity target = humanoidMobPatch.getTarget();
            Vec3 targetPos = target.position();
            double angle = target.getRandom().nextDouble() * 2 * Math.PI;
            double dis = 5.0;
            double newX = targetPos.x + dis * Math.cos(angle);
            double newZ = targetPos.z + dis * Math.sin(angle);
            Vec3 toTeleport = new Vec3(newX, targetPos.y, newZ);
            humanoidMobPatch.getOriginal().setPos(toTeleport);
            humanoidMobPatch.getOriginal().getLookControl().setLookAt(humanoidMobPatch.getTarget());
        }
    });

    /**
     * 世界等级检测
     */
    public static Function<HumanoidMobPatch<?>, Boolean> worldLevelCheck(int min, int max) {
        return humanoidMobPatch -> SMCArchiveManager.getWorldLevel() >= min && SMCArchiveManager.getWorldLevel() <= max;
    }

    /**
     * 世界等级检测
     */
    public static Function<HumanoidMobPatch<?>, Boolean> worldLevelCheck(int min) {
        return humanoidMobPatch -> true;
//        return humanoidMobPatch -> SMCArchiveManager.getWorldLevel() >= min;
    }

    /**
     * boss阶段检测
     */
    public static Function<HumanoidMobPatch<?>, Boolean> phaseCheck(int min, int max) {
        return (patch) -> patch instanceof IMultiPhaseEntityPatch phaseEntityPatch && phaseEntityPatch.checkPhase(min, max);
    }

    public static Function<HumanoidMobPatch<?>, Boolean> attackLevelCheck(int min, int max) {
        return (patch) -> {
            LivingEntityPatch<?> targetPatch = EpicFightCapabilities.getEntityPatch(patch.getTarget(), LivingEntityPatch.class);
            if (targetPatch == null) {
                return false;
            } else {
                int level = targetPatch.getEntityState().getLevel();
                return min <= level && level <= max;
            }
        };
    }

    public static Function<HumanoidMobPatch<?>, Boolean> targetUsingItem(boolean isEdible) {
        return humanoidMobPatch -> {
            LivingEntity target = humanoidMobPatch.getTarget();
            if (!target.isUsingItem()) {
                return false;
            } else {
                ItemStack item = target.getUseItem();
                if (isEdible) {
                    return item.getItem() instanceof PotionItem || item.getItem().isEdible();
                } else {
                    return !(item.getItem() instanceof PotionItem) && !item.getItem().isEdible();
                }
            }
        };
    }

}
