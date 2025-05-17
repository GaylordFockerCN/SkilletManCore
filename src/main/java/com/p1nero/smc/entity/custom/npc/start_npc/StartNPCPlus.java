package com.p1nero.smc.entity.custom.npc.start_npc;

import com.p1nero.smc.entity.custom.npc.SMCNpc;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;

public class StartNPCPlus extends SMCNpc {
    public StartNPCPlus(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void openDialogueScreen(CompoundTag senderData) {

    }
}
