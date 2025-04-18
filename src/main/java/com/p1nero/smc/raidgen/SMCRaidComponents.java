package com.p1nero.smc.raidgen;

import com.p1nero.smc.SkilletManCoreMod;
import hungteen.htlib.api.interfaces.raid.IRaidComponent;
import hungteen.htlib.api.interfaces.raid.IResultComponent;
import hungteen.htlib.api.interfaces.raid.IWaveComponent;
import hungteen.htlib.common.HTSounds;
import hungteen.htlib.common.impl.raid.CommonRaid;
import hungteen.htlib.common.impl.raid.HTRaidComponents;
import hungteen.htlib.common.impl.result.HTResultComponents;
import hungteen.htlib.common.impl.wave.HTWaveComponents;
import java.util.Arrays;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.BossEvent.BossBarColor;

public interface SMCRaidComponents {
    ResourceKey<IRaidComponent> RAID = create("raid");
    ResourceKey<IRaidComponent> TRIAL = create("trial");

    static void register(BootstapContext<IRaidComponent> context) {
        HolderGetter<IResultComponent> results = HTResultComponents.registry().helper().lookup(context);
        HolderGetter<IWaveComponent> waves = HTWaveComponents.registry().helper().lookup(context);
        context.register(RAID, new CommonRaid(HTRaidComponents.builder().blockInside(false).blockOutside(false).renderBorder(false).color(BossBarColor.BLUE).raidSound(HTSounds.PREPARE.getHolder()).waveSound(HTSounds.HUGE_WAVE.getHolder()).victorySound(HTSounds.VICTORY.getHolder()).lossSound(HTSounds.LOSS.getHolder()).build(), Arrays.asList(waves.getOrThrow(SMCWaveComponents.TEST_1), waves.getOrThrow(SMCWaveComponents.TEST_1))));
        context.register(TRIAL, new CommonRaid(HTRaidComponents.builder().blockInside(true).blockOutside(true).renderBorder(true).victoryResult(results.getOrThrow(SMCResultComponents.STAGE_UP)).color(BossBarColor.RED).raidSound(HTSounds.PREPARE.getHolder()).waveSound(HTSounds.HUGE_WAVE.getHolder()).victorySound(HTSounds.VICTORY.getHolder()).lossSound(HTSounds.LOSS.getHolder()).build(), Arrays.asList(waves.getOrThrow(SMCWaveComponents.TEST_1), waves.getOrThrow(SMCWaveComponents.TEST_1), waves.getOrThrow(SMCWaveComponents.TEST_1))));
    }
    static ResourceKey<IRaidComponent> create(String name) {
        return HTRaidComponents.registry().createKey(SkilletManCoreMod.prefix(name));
    }
}
