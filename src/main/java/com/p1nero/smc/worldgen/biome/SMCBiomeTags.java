package com.p1nero.smc.worldgen.biome;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class SMCBiomeTags {
    public static final TagKey<Biome> IN_SMC = TagKey.create(Registries.BIOME, SkilletManCoreMod.prefix("in_dote"));

}
