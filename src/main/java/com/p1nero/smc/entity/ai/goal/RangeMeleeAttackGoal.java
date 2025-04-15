package com.p1nero.smc.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

/**
 * 增加攻击判断范围和间隔
 */
public class RangeMeleeAttackGoal extends MeleeAttackGoal {

    private final float range;
    private final int attackInterval;
    public RangeMeleeAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, float range) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.range = range;
        attackInterval = 20;
    }

    public RangeMeleeAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, float range, int attackInterval) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.range = range;
        this.attackInterval = attackInterval;
    }

    @Override
    protected int adjustedTickDelay(int adjustment) {
        return super.adjustedTickDelay(attackInterval);
    }

    @Override
    protected double getAttackReachSqr(@NotNull LivingEntity pAttackTarget) {
        return range;
    }

}
