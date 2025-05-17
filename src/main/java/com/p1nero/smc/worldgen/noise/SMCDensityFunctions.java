package com.p1nero.smc.worldgen.noise;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;


public class SMCDensityFunctions {
    public static final ResourceKey<DensityFunction> BASE_3D_NOISE_PLAIN = createKey("base_3d_noise_plain");

    private static ResourceKey<DensityFunction> createKey(String name) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, name));
    }

    public static void bootstrap(BootstapContext<DensityFunction> context) {
        context.register(BASE_3D_NOISE_PLAIN, BlendedNoise.createUnseeded(0.25, 0.125, 80.0, 160.0, 4.0));
    }

    private static DensityFunction getFunction(HolderGetter<DensityFunction> densityFunctions, ResourceKey<DensityFunction> key) {
        return new net.minecraft.world.level.levelgen.DensityFunctions.HolderHolder(densityFunctions.getOrThrow(key));
    }

}
