package com.p1nero.smc.mixin;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = EndDragonFight.class, remap = false)
public interface EndDragonFightAccessor {
    @Accessor("dragonEvent")
    ServerBossEvent getDragonEvent();
}
