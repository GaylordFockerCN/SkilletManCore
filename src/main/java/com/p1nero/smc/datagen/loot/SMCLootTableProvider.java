package com.p1nero.smc.datagen.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;

public class SMCLootTableProvider {
    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(output, SMCLoot.IMMUTABLE_LOOT_TABLES,List.of(
                new LootTableProvider.SubProviderEntry(SMCBlockLootTables::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(SMCChestLootTables::new, LootContextParamSets.CHEST),
                new LootTableProvider.SubProviderEntry(SMCEntityLootTables::new, LootContextParamSets.ENTITY)
        ));
    }
}
