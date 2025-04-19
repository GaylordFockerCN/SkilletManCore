package com.p1nero.smc.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import org.jetbrains.annotations.NotNull;

/**
 * 往炉子移动 （不知为何要bug，因此直接换tick里写
 */
public class NPCMoveToStoveTask extends Behavior<Mob> {
    public NPCMoveToStoveTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel serverLevel, @NotNull Mob customer) {
        return check(customer);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel serverLevel, @NotNull Mob customer, long p_22547_) {
        return check(customer);
    }

    private boolean check(Mob customer){
        return ((Customer) customer).getConversingPlayer() == null;
    }

}
