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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.BossEvent.BossBarColor;

public interface SMCRaidComponents {
    ResourceKey<IRaidComponent> TRIAL_1 = create("trial_1");
    ResourceKey<IRaidComponent> TRIAL_2 = create("trial_2");
    ResourceKey<IRaidComponent> TRIAL_3 = create("trial_3");
    List<ResourceKey<IRaidComponent>> RAIDS = new ArrayList<>();

    static void register(BootstapContext<IRaidComponent> context) {
        HolderGetter<IResultComponent> results = HTResultComponents.registry().helper().lookup(context);
        HolderGetter<IWaveComponent> waves = HTWaveComponents.registry().helper().lookup(context);

        for (int i = 0; i <= 30; i++) {
            RAIDS.add(create("raid_" + i));
            context.register(RAIDS.get(i), new CommonRaid(HTRaidComponents.builder()
                    .title(SkilletManCoreMod.getInfo("raid_title", String.valueOf(i)))
                    .victoryTitle(SkilletManCoreMod.getInfo("raid_victory"))
                    .lossTitle(SkilletManCoreMod.getInfo("raid_loss"))
                    .range(72)
                    .blockInside(false)
                    .blockOutside(false)
                    .renderBorder(false)
                    .color(BossBarColor.RED)
                    .raidSound(HTSounds.PREPARE.getHolder())
                    .waveSound(HTSounds.HUGE_WAVE.getHolder())
                    .victorySound(HTSounds.VICTORY.getHolder())
                    .lossSound(HTSounds.LOSS.getHolder())
                    .lossResult(results.getOrThrow(SMCResultComponents.DEFEND_FAILED))
                    .victoryResult(results.getOrThrow(SMCResultComponents.DEFEND_SUCCESS))
                    .build(),
                    Arrays.asList(waves.getOrThrow(SMCWaveComponents.RAID_WAVES_1.get(i)),
                            waves.getOrThrow(SMCWaveComponents.RAID_WAVES_2.get(i)),
                            waves.getOrThrow(SMCWaveComponents.RAID_WAVES_3.get(i)))));
        }

        context.register(TRIAL_1, new CommonRaid(HTRaidComponents.builder()
                .title(SkilletManCoreMod.getInfo("trail_title"))
                .victoryTitle(SkilletManCoreMod.getInfo("trail_success"))
                .lossTitle(SkilletManCoreMod.getInfo("trail_failed"))
                .blockInside(false)
                .blockOutside(false)
                .renderBorder(true)
                .victoryResult(results.getOrThrow(SMCResultComponents.STAGE_UP))
                .color(BossBarColor.YELLOW)
                .raidSound(HTSounds.PREPARE.getHolder())
                .waveSound(HTSounds.HUGE_WAVE.getHolder())
                .victorySound(HTSounds.VICTORY.getHolder())
                .lossSound(HTSounds.LOSS.getHolder()).build(),
                List.of(waves.getOrThrow(SMCWaveComponents.TRIAL_1))));
        context.register(TRIAL_2, new CommonRaid(HTRaidComponents.builder()
                .title(SkilletManCoreMod.getInfo("trail_title"))
                .victoryTitle(SkilletManCoreMod.getInfo("trail_success"))
                .lossTitle(SkilletManCoreMod.getInfo("trail_failed"))
                .blockInside(false)
                .blockOutside(false)
                .renderBorder(true)
                .victoryResult(results.getOrThrow(SMCResultComponents.STAGE_UP))
                .color(BossBarColor.YELLOW)
                .raidSound(HTSounds.PREPARE.getHolder())
                .waveSound(HTSounds.HUGE_WAVE.getHolder())
                .victorySound(HTSounds.VICTORY.getHolder())
                .lossSound(HTSounds.LOSS.getHolder()).build(),
                List.of(waves.getOrThrow(SMCWaveComponents.TRIAL_2), waves.getOrThrow(SMCWaveComponents.TRIAL_2_2))));
        context.register(TRIAL_3, new CommonRaid(HTRaidComponents.builder()
                .title(SkilletManCoreMod.getInfo("trail_title"))
                .victoryTitle(SkilletManCoreMod.getInfo("trail_success"))
                .lossTitle(SkilletManCoreMod.getInfo("trail_failed"))
                .blockInside(false)
                .blockOutside(false)
                .renderBorder(true)
                .victoryResult(results.getOrThrow(SMCResultComponents.STAGE_UP))
                .color(BossBarColor.YELLOW)
                .raidSound(HTSounds.PREPARE.getHolder())
                .waveSound(HTSounds.HUGE_WAVE.getHolder())
                .victorySound(HTSounds.VICTORY.getHolder())
                .lossSound(HTSounds.LOSS.getHolder()).build(),
                List.of(waves.getOrThrow(SMCWaveComponents.TRIAL_3), waves.getOrThrow(SMCWaveComponents.TRIAL_3_2))));
    }

    static ResourceKey<IRaidComponent> create(String name) {
        return HTRaidComponents.registry().createKey(SkilletManCoreMod.prefix(name));
    }
}
