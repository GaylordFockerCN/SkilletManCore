package com.p1nero.smc.datagen;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.raidgen.*;
import hungteen.htlib.common.impl.position.HTPositionComponents;
import hungteen.htlib.common.impl.raid.HTRaidComponents;
import hungteen.htlib.common.impl.result.HTResultComponents;
import hungteen.htlib.common.impl.spawn.HTSpawnComponents;
import hungteen.htlib.common.impl.wave.HTWaveComponents;
import hungteen.htlib.util.helper.HTLibHelper;
import hungteen.htlib.util.helper.VanillaHelper;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.NotNull;

public class SMCRaidDatapackEntriesGen extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(HTPositionComponents.registry().getRegistryKey(), SMCPositionComponents::register)
            .add(HTResultComponents.registry().getRegistryKey(), SMCResultComponents::register)
            .add(HTSpawnComponents.registry().getRegistryKey(), SMCSpawnComponents::register)
            .add(HTWaveComponents.registry().getRegistryKey(), SMCWaveComponents::register)
            .add(HTRaidComponents.registry().getRegistryKey(), SMCRaidComponents::register);
    private final String modId;

    public SMCRaidDatapackEntriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder datapackEntriesBuilder, String modId, Set<String> modIds) {
        super(output, registries, datapackEntriesBuilder, modIds);
        this.modId = modId;
    }

    SMCRaidDatapackEntriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        this(output, provider, BUILDER, SkilletManCoreMod.MOD_ID, Set.of(VanillaHelper.get().getModID(), HTLibHelper.get().getModID(), SkilletManCoreMod.MOD_ID));
    }

    public @NotNull String getName() {
        return this.modId + " datapack raid entries";
    }
}
