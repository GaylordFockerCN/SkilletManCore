package com.p1nero.smc.raidgen;

import com.p1nero.smc.SkilletManCoreMod;
import hungteen.htlib.api.interfaces.raid.IPositionComponent;
import hungteen.htlib.common.impl.position.CenterAreaPosition;
import hungteen.htlib.common.impl.position.HTPositionComponents;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.phys.Vec3;

public interface SMCPositionComponents {
    ResourceKey<IPositionComponent> TRIAL = create("trial");
    ResourceKey<IPositionComponent> RAID = create("raid");

    static void register(BootstapContext<IPositionComponent> context) {
        context.register(TRIAL, new CenterAreaPosition(Vec3.ZERO, 10, 15, true, 0.0, true));
        context.register(RAID, new CenterAreaPosition(Vec3.ZERO, 10, 15, true, 0.0, true));
    }

    static ResourceKey<IPositionComponent> create(String name) {
        return HTPositionComponents.registry().createKey(SkilletManCoreMod.prefix(name));
    }

}
