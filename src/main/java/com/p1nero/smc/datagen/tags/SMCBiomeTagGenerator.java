package com.p1nero.smc.datagen.tags;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.worldgen.biome.SMCBiomeTags;
import com.p1nero.smc.worldgen.biome.SMCBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class SMCBiomeTagGenerator extends BiomeTagsProvider {

    public SMCBiomeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, provider, SkilletManCoreMod.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(SMCBiomeTags.IN_SMC).add(SMCBiomes.AIR);
    }

    @Override
    public @NotNull String getName() {
        return "SMC Biome Tags";
    }
}