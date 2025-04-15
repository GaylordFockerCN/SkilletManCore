package com.p1nero.smc.block.custom.spawner;

import com.p1nero.smc.block.entity.spawner.EntitySpawnerBlockEntity;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public abstract class EntitySpawnerBlock extends BaseEntityBlock {
    Supplier<BlockEntityType<? extends EntitySpawnerBlockEntity<?>>> blockEntityType;
    protected EntitySpawnerBlock(Properties pProperties, Supplier<BlockEntityType<? extends EntitySpawnerBlockEntity<?>>> blockEntityType) {
        super(pProperties);
        this.blockEntityType = blockEntityType;
    }

}