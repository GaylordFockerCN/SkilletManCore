package com.p1nero.smc.entity.custom;

import com.p1nero.smc.entity.api.LevelableEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class SMCMonster extends PathfinderMob implements LevelableEntity {
    protected static final EntityDataAccessor<Integer> BLOCK_COUNT = SynchedEntityData.defineId(SMCMonster.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> NEUTRALIZE_COUNT = SynchedEntityData.defineId(SMCMonster.class, EntityDataSerializers.INT);

    protected SMCMonster(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        setNeutralizeCount(getMaxNeutralizeCount());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(BLOCK_COUNT, 0);
        getEntityData().define(NEUTRALIZE_COUNT, getMaxNeutralizeCount());
    }

    public abstract int getMaxNeutralizeCount();

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.getEntityData().set(BLOCK_COUNT,tag.getInt("block_cnt"));
        this.getEntityData().set(NEUTRALIZE_COUNT,tag.getInt("neutralize_cnt"));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("block_cnt", this.getEntityData().get(BLOCK_COUNT));
        tag.putInt("neutralize_cnt", this.getEntityData().get(NEUTRALIZE_COUNT));
    }

    public void setBlockCount(int count){
        getEntityData().set(BLOCK_COUNT, Math.max(count, 0));
    }

    public int getBlockCount() {
        return getEntityData().get(BLOCK_COUNT);
    }

    public void setNeutralizeCount(int count){
        getEntityData().set(NEUTRALIZE_COUNT, Math.max(count, 0));
    }

    public int getNeutralizeCount() {
        return getEntityData().get(NEUTRALIZE_COUNT);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

}
