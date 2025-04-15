package com.p1nero.smc.entity.custom.boss.goldenflame.ai;

import com.p1nero.smc.entity.custom.boss.goldenflame.GoldenFlamePatch;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

public class GoldenFlameAnimatedAttackGoal<T extends MobPatch<?>> extends AnimatedAttackGoal<T> {

    public GoldenFlameAnimatedAttackGoal(T mobpatch, CombatBehaviors<T> combatBehaviors) {
        super(mobpatch, combatBehaviors);
    }

    public void tick() {
        if (this.mobpatch.getTarget() != null) {
            EntityState state = this.mobpatch.getEntityState();
            this.combatBehaviors.tick();
            if (mobpatch instanceof GoldenFlamePatch goldenFlamePatch) {
                CombatBehaviors.Behavior<T> result;
                if (this.combatBehaviors.hasActivatedMove()) {
                    if (state.canBasicAttack() && goldenFlamePatch.getOriginal().getStrafingTime() <= 0) {//漫游中则暂停当前序列
                        result = this.combatBehaviors.tryProceed();
                        if (result != null) {
                            result.execute(this.mobpatch);
                        }
                    }
                    //当前无序列则选新序列执行
                } else if (!state.inaction()
                        && !goldenFlamePatch.getOriginal().isCharging()
                        && goldenFlamePatch.getOriginal().getInactionTime() <= 0
                        && goldenFlamePatch.getOriginal().getStrafingTime() <= 0
                        && goldenFlamePatch.getOriginal().shouldRender()
                        && !goldenFlamePatch.getAnimator().getPlayerFor(null).getAnimation().equals(WOMAnimations.TORMENT_CHARGE)) {
                    result = this.combatBehaviors.selectRandomBehaviorSeries();
                    if (result != null) {
                        result.execute(this.mobpatch);
                    }
                }
            }
        }

    }

}
