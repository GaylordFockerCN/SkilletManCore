package com.p1nero.smc.datagen;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.worldgen.SMCBiomeModifiers;
import com.p1nero.smc.worldgen.SMCConfiguredFeatures;
import com.p1nero.smc.worldgen.SMCPlacedFeatures;
import com.p1nero.smc.worldgen.biome.SMCBiomes;
import com.p1nero.smc.worldgen.noise.SMCDensityFunctions;
import com.p1nero.smc.worldgen.dimension.SMCDimension;
import com.p1nero.smc.worldgen.noise.SMCNoiseSettings;
import com.p1nero.smc.worldgen.noise.SMCNoises;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SMCWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
//            .add(Registries.STRUCTURE, TCRStructures::bootstrap)
//            .add(Registries.STRUCTURE_SET, TCRStructureSets::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, SMCConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, SMCPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, SMCBiomeModifiers::bootstrap)
            .add(Registries.DENSITY_FUNCTION, SMCDensityFunctions::bootstrap)
            .add(Registries.NOISE, SMCNoises::bootstrap)
            .add(Registries.NOISE_SETTINGS, SMCNoiseSettings::bootstrap)
            .add(Registries.BIOME, SMCBiomes::boostrap)
            .add(Registries.LEVEL_STEM, SMCDimension::bootstrapStem)
            .add(Registries.DIMENSION_TYPE, SMCDimension::bootstrapType);

    public SMCWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(SkilletManCoreMod.MOD_ID));
    }
}
