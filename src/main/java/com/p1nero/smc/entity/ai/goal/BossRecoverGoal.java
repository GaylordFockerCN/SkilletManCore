package com.p1nero.smc.entity.ai.goal;

import com.p1nero.smc.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * 周围没人就回满血
 */
public class BossRecoverGoal extends Goal {
    private final LivingEntity boss;
    private final int dis;
    public BossRecoverGoal(LivingEntity boss, int dis){
        this.boss = boss;
        this.dis = dis;
    }
    @Override
    public boolean canUse() {
        return EntityUtil.getNearByEntities(boss, dis).isEmpty();
    }

    @Override
    public void start() {
        boss.setHealth(boss.getMaxHealth());
    }
}
