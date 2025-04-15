package com.p1nero.smc.entity.ai.behavior;

import com.p1nero.smc.entity.api.NpcDialogue;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import org.jetbrains.annotations.NotNull;

/**
 * 对话的时候不能动还要看着玩家
 */
public class NPCDialogueTask extends Behavior<Mob> {
    public NPCDialogueTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel p_22538_, @NotNull Mob mob) {
        return check(mob);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel p_22545_, @NotNull Mob mob, long p_22547_) {
        return check(mob);
    }

    private boolean check(Mob mob){
        return mob instanceof NpcDialogue npc && npc.getConversingPlayer() != null;
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull Mob mob, long p_22553_) {
        if(mob instanceof NpcDialogue npc && npc.getConversingPlayer() != null){
//            mob.lookAt(npc.getConversingPlayer(), 30, 30);
            mob.getLookControl().setLookAt(npc.getConversingPlayer());
        }
        mob.getNavigation().stop();
    }
}
