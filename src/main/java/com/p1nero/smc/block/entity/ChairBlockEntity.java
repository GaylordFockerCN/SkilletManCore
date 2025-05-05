package com.p1nero.smc.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ChairBlockEntity extends BlockEntity {
    public ChairBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }
}
