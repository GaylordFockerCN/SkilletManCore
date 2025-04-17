package com.p1nero.smc.entity.ai.behavior;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.p1nero.smc.entity.custom.npc.SMCNpc;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.npc.Villager;

/**
 * 根据不同村民给予不同脑子
 */
public class VillagerTasks {

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getSMCVillagerCorePackage(SMCNpc smcNpc) {
        if(smcNpc instanceof Customer) {
            return ImmutableList.of(Pair.of(0, new NPCDialogueTask()), Pair.of(1, new NPCMoveToStoveTask()));
        }
        return ImmutableList.of(Pair.of(0, new NPCDialogueTask()));
    }
}
