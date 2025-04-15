package com.p1nero.smc.capability.epicfight;

import com.p1nero.smc.entity.ai.epicfight.SMCBossTargetChasingGoal;
import com.p1nero.smc.entity.ai.epicfight.api.*;
import com.p1nero.smc.entity.custom.boss.SMCBoss;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.world.capabilities.entitypatch.Faction;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

import java.util.ArrayList;
import java.util.List;

import static com.p1nero.smc.entity.custom.boss.SMCBoss.ATTACK_SPEED;

public abstract class SMCBossPatch<T extends SMCBoss> extends HumanoidMobPatch<T> implements IModifyStunTypeEntityPatch, IModifyAttackDamageEntityPatch, ITimeEventListEntityPatch, IModifyAttackSpeedEntityPatch , IMultiPhaseEntityPatch {

    @Nullable
    protected StunType stunTypeModify;

    protected float damageModify = 0;

    protected final List<TimeStampedEvent> list = new ArrayList<>();

    public SMCBossPatch(Faction faction) {
        super(faction);
    }

    @Override
    public void onConstructed(T entityIn) {
        super.onConstructed(entityIn);
    }

    @Override
    public void updateEntityState() {
        super.updateEntityState();
        //用于蓄力的时候暂停序列，可看AnimatedAttackGoal
        if (this.getOriginal().getInactionTime() > 0) {
            state.setState(EntityState.INACTION, true);
            state.setState(EntityState.CAN_BASIC_ATTACK, false);
        }
    }

    @Override
    public void setAIAsInfantry(boolean holdingRangedWeapon) {
        CombatBehaviors.Builder<HumanoidMobPatch<?>> builder = this.getHoldingItemWeaponMotionBuilder();
        if (builder != null) {
//            this.original.goalSelector.addGoal(0, new GoldenFlameAnimatedAttackGoal<>(this, builder.build(this)));
            this.original.goalSelector.addGoal(0, new AnimatedAttackGoal<>(this, builder.build(this)));
            this.original.goalSelector.addGoal(1, new SMCBossTargetChasingGoal(this, this.getOriginal(), 1.0, true));
        }
    }

    @Override
    public void setStunType(StunType stunType) {
        stunTypeModify = stunType;
    }

    @Override
    @Nullable
    public StunType getStunType() {
        return stunTypeModify;
    }

    @Override
    public void setNewDamage(float damage) {
        damageModify = damage;
    }

    @Override
    public float getNewDamage() {
        return damageModify;
    }

    @Override
    public List<TimeStampedEvent> getTimeEventList() {
        return list;
    }

    @Override
    public boolean addEvent(TimeStampedEvent event) {
        return list.add(event);
    }

    @Override
    public float getAttackSpeed() {
        return this.original.getEntityData().get(ATTACK_SPEED);
    }

    @Override
    public void setAttackSpeed(float value) {
        this.original.getEntityData().set(ATTACK_SPEED, Math.abs(value));
    }

    @Override
    public int getPhase() {
        return getOriginal().getPhase();
    }

    @Override
    public void setPhase(int phase) {
        getOriginal().setPhase(phase);
    }

}
