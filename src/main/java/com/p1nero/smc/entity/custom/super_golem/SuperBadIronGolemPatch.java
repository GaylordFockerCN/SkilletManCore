package com.p1nero.smc.entity.custom.super_golem;

import com.merlin204.supergolem.gameassets.Animations;
import com.p1nero.smc.gameasset.SMCSuperGolemCombatBehavior;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

import java.util.Set;

public class SuperBadIronGolemPatch<T extends IronGolem> extends MobPatch<T> {

    public void initAnimator(Animator animator) {
        animator.addLivingAnimation(LivingMotions.IDLE, Animations.GOLEM_IDLE);
        animator.addLivingAnimation(LivingMotions.WALK, Animations.GOLEM_WALK);
        animator.addLivingAnimation(LivingMotions.DEATH, Animations.GOLEM_DEATH);
        animator.addLivingAnimation(LivingMotions.CHASE, Animations.GOLEM_RUN);
        animator.addLivingAnimation(LivingMotions.FALL, Animations.GOLEM_FALL);
    }

    protected void initAI() {
        super.initAI();
        this.original.goalSelector.addGoal(0, new AnimatedAttackGoal<>(this, SMCSuperGolemCombatBehavior.SUPER_IRON_GOLEM.build(this)));
        this.original.goalSelector.addGoal(1, new TargetChasingGoal(this, this.original, 1.0, false));
    }

    protected void selectGoalToRemove(Set<Goal> toRemove) {
        super.selectGoalToRemove(toRemove);

        for (WrappedGoal wrappedGoal : this.original.goalSelector.getAvailableGoals()) {
            Goal goal = wrappedGoal.getGoal();
            if (goal instanceof MoveTowardsTargetGoal) {
                toRemove.add(goal);
            }
        }

    }

    public void updateMotion(boolean considerInaction) {
        super.commonAggressiveMobUpdateMotion(considerInaction);
    }

    public SoundEvent getWeaponHitSound(InteractionHand hand) {
        return EpicFightSounds.BLUNT_HIT_HARD.get();
    }

    public SoundEvent getSwingSound(InteractionHand hand) {
        return EpicFightSounds.WHOOSH_BIG.get();
    }

    @Override
    public StaticAnimation getHitAnimation(StunType stunType) {
        return switch (stunType) {
            case NONE -> null;
            case SHORT -> Animations.GOLEM_SHORT_HIT;
            case LONG, HOLD -> Animations.GOLEM_LONG_HIT;
            case KNOCKDOWN, NEUTRALIZE -> Animations.GOLEM_KNOCKDOWN;
            case FALL -> Animations.GOLEM_FALL;
        };
    }

    @Override
    public boolean isTeammate(Entity entity) {
        return entity instanceof Villager;
    }
}
