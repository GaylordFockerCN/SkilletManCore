package com.p1nero.smc.condition;

import com.p1nero.smc.archive.SMCArchiveManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import yesman.epicfight.data.conditions.Condition;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.List;

public class WorldLevelCheckCondition implements Condition<LivingEntityPatch<?>> {

    private int worldLevel;
    @Override
    public Condition<LivingEntityPatch<?>> read(CompoundTag compoundTag) {
        if (!compoundTag.contains("world_level")) {
            throw new IllegalArgumentException("custom world level check condition error: world_level not specified!");
        }  else {
            this.worldLevel = compoundTag.getInt("world_level");
            return this;
        }
    }

    @Override
    public CompoundTag serializePredicate() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("world_level", this.worldLevel);
        return tag;
    }

    @Override
    public boolean predicate(LivingEntityPatch<?> entityPatch) {
        return SMCArchiveManager.getWorldLevel() >= worldLevel;
    }

    @Override
    public List<ParameterEditor> getAcceptingParameters(Screen screen) {
        return null;
    }
}
