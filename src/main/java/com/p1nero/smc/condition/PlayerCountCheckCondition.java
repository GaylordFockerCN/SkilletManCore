package com.p1nero.smc.condition;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.data.conditions.Condition;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.List;

public class PlayerCountCheckCondition implements Condition<LivingEntityPatch<?>> {

    private double distance;
    private int count;
    @Override
    public Condition<LivingEntityPatch<?>> read(CompoundTag compoundTag) {
        if (!compoundTag.contains("distance") && !compoundTag.contains("count")) {
            throw new IllegalArgumentException("custom player count check condition error: distance or count not specified!");
        }  else {
            this.distance = compoundTag.getDouble("distance");
            this.count = compoundTag.getInt("count");
            return this;
        }
    }

    @Override
    public CompoundTag serializePredicate() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("distance", this.distance);
        tag.putInt("count", count);
        return tag;
    }

    @Override
    public boolean predicate(LivingEntityPatch<?> entityPatch) {
        Level level = entityPatch.getOriginal().level();
        if(distance <= 0 && level instanceof ServerLevel serverLevel){
            return serverLevel.getServer().getPlayerList().getPlayers().size() == count;
        }
        Vec3 pos = entityPatch.getOriginal().position();
        return level.getNearbyPlayers(TargetingConditions.forCombat(), entityPatch.getOriginal(), new AABB(pos.add(distance, distance, distance), pos.add(-distance, -distance, -distance))).size() == count;
    }

    @Override
    public List<ParameterEditor> getAcceptingParameters(Screen screen) {
        return null;
    }
}
