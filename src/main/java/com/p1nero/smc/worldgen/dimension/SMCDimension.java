package com.p1nero.smc.worldgen.dimension;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.worldgen.biome.*;
import com.p1nero.smc.worldgen.noise.SMCNoiseSettings;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.OptionalLong;

public class SMCDimension {
    public static final ResourceKey<LevelStem> P_SKY_ISLAND_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "realm_of_the_dream"));
    public static final ResourceKey<Level> P_SKY_ISLAND_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "realm_of_the_dream"));
    public static final ResourceKey<DimensionType> P_SKY_ISLAND_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "realm_of_the_dream_type"));


    public static void bootstrapType(ResourceKey<DimensionType> typeResourceKey, BootstapContext<DimensionType> context){
        context.register(typeResourceKey, new DimensionType(
                OptionalLong.of(12000), // fixedTime
                true, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                true, // natural
                1.0, // coordinateScale
                true, // bedWorks
                true, // respawnAnchorWorks
                32, // minY
                512, // height
                512, // logicalHeight
                BlockTags.INFINIBURN_NETHER, // infiniburn
                BuiltinDimensionTypes.END_EFFECTS, // effectsLocation
                0.0f, // ambientLight
                new DimensionType.MonsterSettings(true, false, ConstantInt.of(0), 0)));
    }
    public static void bootstrapType(BootstapContext<DimensionType> context) {
        bootstrapType(P_SKY_ISLAND_TYPE, context);
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator wrappedChunkGenerator = new NoiseBasedChunkGenerator(SMCBiomeProvider.create(biomeRegistry), noiseGenSettings.getOrThrow(SMCNoiseSettings.PLAIN));

        SMCChunkGenerator chunkGenerator = new SMCChunkGenerator(wrappedChunkGenerator, noiseGenSettings.getOrThrow(SMCNoiseSettings.PLAIN));
        LevelStem stem = new LevelStem(dimTypes.getOrThrow(SMCDimension.P_SKY_ISLAND_TYPE), chunkGenerator);
        context.register(P_SKY_ISLAND_KEY, stem);
    }

}