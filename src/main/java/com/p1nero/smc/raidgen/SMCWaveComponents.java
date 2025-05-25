//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.p1nero.smc.raidgen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.p1nero.smc.SkilletManCoreMod;
import hungteen.htlib.api.interfaces.raid.IPositionComponent;
import hungteen.htlib.api.interfaces.raid.ISpawnComponent;
import hungteen.htlib.api.interfaces.raid.IWaveComponent;
import hungteen.htlib.common.impl.position.HTPositionComponents;
import hungteen.htlib.common.impl.spawn.HTSpawnComponents;
import hungteen.htlib.common.impl.wave.CommonWave;
import hungteen.htlib.common.impl.wave.HTWaveComponents;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;

public interface SMCWaveComponents {
    ResourceKey<IWaveComponent> TRIAL_1 = create("trial_1");
    ResourceKey<IWaveComponent> TRIAL_2 = create("trial_2");
    ResourceKey<IWaveComponent> TRIAL_2_2 = create("trial_2_2");
    ResourceKey<IWaveComponent> TRIAL_3 = create("trial_3");
    ResourceKey<IWaveComponent> TRIAL_3_2 = create("trial_3_2");
    List<ResourceKey<IWaveComponent>> RAID_WAVES_1 = new ArrayList<>();
    List<ResourceKey<IWaveComponent>> RAID_WAVES_2 = new ArrayList<>();
    List<ResourceKey<IWaveComponent>> RAID_WAVES_3 = new ArrayList<>();
    List<ResourceKey<IWaveComponent>> NETHER_RAID_WAVES_1 = new ArrayList<>();
    static void register(BootstapContext<IWaveComponent> context) {
        HolderGetter<ISpawnComponent> spawns = HTSpawnComponents.registry().helper().lookup(context);
        HolderGetter<IPositionComponent> positions = HTPositionComponents.registry().helper().lookup(context);
        Holder<ISpawnComponent> wither = spawns.getOrThrow(SMCSpawnComponents.WITHER);
        Holder<ISpawnComponent> wither2 = spawns.getOrThrow(SMCSpawnComponents.WITHER2);
        Holder<ISpawnComponent> superGolem1 = spawns.getOrThrow(SMCSpawnComponents.SUPER_GOLEM_1);
        Holder<ISpawnComponent> superGolem2 = spawns.getOrThrow(SMCSpawnComponents.SUPER_GOLEM_2);
        Holder<IPositionComponent> raidPosition = positions.getOrThrow(SMCPositionComponents.RAID);
        context.register(TRIAL_1, new CommonWave(HTWaveComponents.builder()
                .prepare(100)
                .wave(3200)
                .placement(raidPosition)
                .build(),
                List.of(Pair.of(ConstantInt.of(10), superGolem1))));
        context.register(TRIAL_2, new CommonWave(HTWaveComponents.builder()
                .prepare(100)
                .wave(3200)
                .placement(raidPosition)
                .build(),
                List.of(Pair.of(ConstantInt.of(10), superGolem2))));
        context.register(TRIAL_2_2, new CommonWave(HTWaveComponents.builder()
                .prepare(100)
                .wave(9600)
                .placement(raidPosition)
                .build(),
                List.of(Pair.of(ConstantInt.of(10), superGolem2), Pair.of(ConstantInt.of(500), wither))));
        context.register(TRIAL_3, new CommonWave(HTWaveComponents.builder()
                .prepare(100)
                .wave(3200)
                .placement(raidPosition)
                .build(),
                List.of(Pair.of(ConstantInt.of(10), superGolem2))));
        context.register(TRIAL_3_2, new CommonWave(HTWaveComponents.builder()
                .prepare(100)
                .wave(9600)
                .placement(raidPosition)
                .build(),
                List.of(Pair.of(ConstantInt.of(10), superGolem2), Pair.of(ConstantInt.of(500), wither), Pair.of(ConstantInt.of(600), wither2))));

        for(int i = 0; i <= 30; i ++) {
            RAID_WAVES_1.add(create("raid_wave_" + i));
            RAID_WAVES_2.add(create("raid_wave2_" + i));
            RAID_WAVES_3.add(create("raid_wave3_" + i));
            NETHER_RAID_WAVES_1.add(create("nether_raid_wave_" + i));
            ImmutableList.Builder<Pair<IntProvider, Holder<ISpawnComponent>>> wave1builder = ImmutableList.builder();
            ImmutableList.Builder<Pair<IntProvider, Holder<ISpawnComponent>>> wave2builder = ImmutableList.builder();
            ImmutableList.Builder<Pair<IntProvider, Holder<ISpawnComponent>>> wave3builder = ImmutableList.builder();
            ImmutableList.Builder<Pair<IntProvider, Holder<ISpawnComponent>>> netherWave = ImmutableList.builder();

            spawns.get(SMCSpawnComponents.PIGLIN.get(i)).ifPresent(reference -> {
                netherWave.add(Pair.of(ConstantInt.of(10), reference));
            });
            spawns.get(SMCSpawnComponents.PIGLIN_BRUTE.get(i)).ifPresent(reference -> {
                netherWave.add(Pair.of(ConstantInt.of(10), reference));
            });
            spawns.get(SMCSpawnComponents.GHAST.get(i)).ifPresent(reference -> {
                netherWave.add(Pair.of(ConstantInt.of(10), reference));
            });
            spawns.get(SMCSpawnComponents.ZOMBIES.get(i)).ifPresent(reference -> {
                wave1builder.add(Pair.of(ConstantInt.of(10), reference));
                wave2builder.add(Pair.of(ConstantInt.of(10), reference));
                wave3builder.add(Pair.of(ConstantInt.of(10), reference));
            });
            spawns.get(SMCSpawnComponents.SKELETONS.get(i)).ifPresent(reference -> {
                wave2builder.add(Pair.of(ConstantInt.of(50), reference));
                wave3builder.add(Pair.of(ConstantInt.of(50), reference));
            });
            if(i > 3) {
                spawns.get(SMCSpawnComponents.SPIDERS.get(i)).ifPresent(reference -> {
                    wave1builder.add(Pair.of(ConstantInt.of(70), reference));
                    wave2builder.add(Pair.of(ConstantInt.of(70), reference));
                    wave3builder.add(Pair.of(ConstantInt.of(70), reference));
                });
                spawns.get(SMCSpawnComponents.CREEPERS.get(i)).ifPresent(reference -> {
                    wave1builder.add(Pair.of(ConstantInt.of(70), reference));
                    wave2builder.add(Pair.of(ConstantInt.of(70), reference));
                    wave3builder.add(Pair.of(ConstantInt.of(70), reference));
                });
            }
            if(i > 5) {
                spawns.get(SMCSpawnComponents.WITCHES.get(i)).ifPresent(reference -> {
                    wave2builder.add(Pair.of(ConstantInt.of(90), reference));
                    wave3builder.add(Pair.of(ConstantInt.of(90), reference));
                });
                spawns.get(SMCSpawnComponents.PILLAGERS.get(i)).ifPresent(reference -> {
                    wave2builder.add(Pair.of(ConstantInt.of(90), reference));
                    wave3builder.add(Pair.of(ConstantInt.of(90), reference));
                });
            }
            if(i > 7) {
                spawns.get(SMCSpawnComponents.VINDICATORS.get(i)).ifPresent(reference -> {
                    wave2builder.add(Pair.of(ConstantInt.of(100), reference));
                    wave3builder.add(Pair.of(ConstantInt.of(100), reference));
                });
                spawns.get(SMCSpawnComponents.EVOKERS.get(i)).ifPresent(reference -> wave3builder.add(Pair.of(ConstantInt.of(100), reference)));
            }

            if(i > 9) {
                spawns.get(SMCSpawnComponents.WITHER_SKELETONS.get(i)).ifPresent(reference -> wave3builder.add(Pair.of(ConstantInt.of(100), reference)));
                spawns.get(SMCSpawnComponents.BLAZES.get(i)).ifPresent(reference -> wave3builder.add(Pair.of(ConstantInt.of(100), reference)));
            }
            if(i > 11) {
                spawns.get(SMCSpawnComponents.ENDER_MANS.get(i)).ifPresent(reference -> wave3builder.add(Pair.of(ConstantInt.of(100), reference)));
            }

            if(i > 1 && i % 10 == 0) {
                wave1builder.add(Pair.of(ConstantInt.of(10), wither2));
            }

            context.register(RAID_WAVES_1.get(i), new CommonWave(HTWaveComponents.builder()
                    .prepare(600)
                    .wave(3200)
                    .placement(raidPosition)
                    .build(),
                    wave1builder.build()));
            context.register(RAID_WAVES_2.get(i), new CommonWave(HTWaveComponents.builder()
                    .prepare(100)
                    .wave(9600)
                    .placement(raidPosition)
                    .build(),
                    wave2builder.build()));
            context.register(RAID_WAVES_3.get(i), new CommonWave(HTWaveComponents.builder()
                    .prepare(100)
                    .wave(12800)
                    .placement(raidPosition)
                    .build(),
                    wave3builder.build()));
            context.register(NETHER_RAID_WAVES_1.get(i), new CommonWave(HTWaveComponents.builder()
                    .prepare(100)
                    .wave(12800)
                    .placement(raidPosition)
                    .build(),
                    netherWave.build()));
        }


    }
    static ResourceKey<IWaveComponent> create(String name) {
        return HTWaveComponents.registry().createKey(SkilletManCoreMod.prefix(name));
    }

}
