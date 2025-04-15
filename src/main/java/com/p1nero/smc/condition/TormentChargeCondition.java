package com.p1nero.smc.condition;

import com.p1nero.smc.entity.custom.boss.goldenflame.GoldenFlame;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import yesman.epicfight.data.conditions.Condition;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.List;

public class TormentChargeCondition extends Condition.EntityPatchCondition {
    private final int tick;
    private final boolean isLess;
    public TormentChargeCondition(int tick, boolean isLess){
        this.isLess = isLess;
        this.tick = tick;
    }
    @Override
    public Condition<LivingEntityPatch<?>> read(CompoundTag compoundTag) {
        return new TormentChargeCondition(compoundTag.getInt("ticks"), compoundTag.getBoolean("isLess"));
    }

    @Override
    public CompoundTag serializePredicate() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt("ticks", tick);
        compoundTag.putBoolean("isLess", isLess);
        return compoundTag;
    }

    @Override
    public boolean predicate(LivingEntityPatch<?> entityPatch) {
        if(entityPatch.getOriginal() instanceof GoldenFlame goldenFlame){
            return isLess ^ goldenFlame.getChargingTimer() > tick;
        }
        return false;
    }

    @Override
    public List<ParameterEditor> getAcceptingParameters(Screen screen) {
        return null;
    }
}
