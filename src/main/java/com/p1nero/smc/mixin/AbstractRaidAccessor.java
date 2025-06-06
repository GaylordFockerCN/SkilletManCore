package com.p1nero.smc.mixin;

import hungteen.htlib.common.world.raid.AbstractRaid;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = AbstractRaid.class, remap = false)
public interface AbstractRaidAccessor {

    @Accessor("raidLocation")
    ResourceLocation getRaidLocation();

}
