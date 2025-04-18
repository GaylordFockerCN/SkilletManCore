//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.p1nero.smc.raidgen;

import com.mojang.datafixers.util.Pair;
import com.p1nero.smc.SkilletManCoreMod;
import hungteen.htlib.api.interfaces.raid.IPositionComponent;
import hungteen.htlib.api.interfaces.raid.ISpawnComponent;
import hungteen.htlib.api.interfaces.raid.IWaveComponent;
import hungteen.htlib.common.impl.position.HTPositionComponents;
import hungteen.htlib.common.impl.spawn.HTSpawnComponents;
import hungteen.htlib.common.impl.wave.CommonWave;
import hungteen.htlib.common.impl.wave.HTWaveComponents;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;

public interface SMCWaveComponents {
    ResourceKey<IWaveComponent> TEST_1 = create("test_1");

    static void register(BootstapContext<IWaveComponent> context) {
        HolderGetter<ISpawnComponent> spawns = HTSpawnComponents.registry().helper().lookup(context);
        HolderGetter<IPositionComponent> positions = HTPositionComponents.registry().helper().lookup(context);
        Holder<ISpawnComponent> skeleton = spawns.getOrThrow(SMCSpawnComponents.WITHER_SKELETON);
        context.register(TEST_1, new CommonWave(HTWaveComponents.builder().prepare(100).wave(800).skip(false).placement(positions.getOrThrow(SMCPositionComponents.TRIAL)).build(), List.of(Pair.of(ConstantInt.of(10), skeleton))));
    }
    static ResourceKey<IWaveComponent> create(String name) {
        return HTWaveComponents.registry().createKey(SkilletManCoreMod.prefix(name));
    }

}
