package com.p1nero.smc.datagen.loot;

import com.p1nero.smc.entity.SMCEntities;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class SMCEntityLootTables extends EntityLootSubProvider {

    protected SMCEntityLootTables() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {

        add(SMCEntities.GOLDEN_FLAME.get(), emptyLootTable());
        add(SMCEntities.FLAME_CIRCLE.get(), emptyLootTable());

        add(SMCEntities.VILLAGER_NO_BRAIN.get(), emptyLootTable());
        add(SMCEntities.START_NPC.get(), emptyLootTable());
        add(SMCEntities.CUSTOMER.get(), emptyLootTable());
        add(SMCEntities.FAKE_CUSTOMER.get(), emptyLootTable());
        add(SMCEntities.HE_SHEN.get(), emptyLootTable());
        add(SMCEntities.TWO_KID.get(), emptyLootTable());
        add(SMCEntities.THIEF1.get(), emptyLootTable());
        add(SMCEntities.THIEF2.get(), emptyLootTable());
    }

    public LootTable.Builder emptyLootTable() {
        return LootTable.lootTable();
    }

    public LootTable.Builder fromEntityLootTable(EntityType<?> parent) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootTableReference.lootTableReference(parent.getDefaultLootTable())));
    }

    private static LootTable.Builder sheepLootTableBuilderWithDrop(ItemLike wool) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(wool))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootTableReference.lootTableReference(EntityType.SHEEP.getDefaultLootTable())));
    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return SMCEntities.REGISTRY.getEntries().stream().map(RegistryObject::get);
    }
}