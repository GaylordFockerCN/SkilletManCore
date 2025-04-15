package com.p1nero.smc.worldgen;

import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class SMCOrePlacement {
    public static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier placementModifier) {
        return orePlacement(CountPlacement.of(count), placementModifier);
    }

    public static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier placementModifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chance), placementModifier);
    }
}
