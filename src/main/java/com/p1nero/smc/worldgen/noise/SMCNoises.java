package com.p1nero.smc.worldgen.noise;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

public class SMCNoises {
    public static final ResourceKey<NoiseParameters> TEMPERATURE = createKey("temperature");
    public static final ResourceKey<NoiseParameters> VEGETATION = createKey("vegetation");

    private static ResourceKey<NoiseParameters> createKey(String name) {
        return ResourceKey.create(Registries.NOISE, ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, name));
    }

    public static void bootstrap(BootstapContext<NoiseParameters> context) {
        register(context, TEMPERATURE, -8, 1.5, 0.0, 1.0, 0.0, 0.0, 0.0);
        register(context, VEGETATION, -7, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0);
    }

    public static void register(BootstapContext<NoiseParameters> context, ResourceKey<NormalNoise.NoiseParameters> key, int firstOctave, double firstAmplitude, double... amplitudes) {
        context.register(key, new NormalNoise.NoiseParameters(firstOctave, firstAmplitude, amplitudes));
    }
}
