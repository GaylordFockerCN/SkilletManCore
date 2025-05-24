package com.p1nero.smc.block.custom;

import com.p1nero.smc.block.entity.MainCookBlockEntity2;
import com.p1nero.smc.registrate.SMCRegistrateBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 核心方块，用来开始营业和吸引顾客
 */
public class MainCookBlock2 extends BaseEntityBlock {

    public MainCookBlock2(Properties properties) {
        super(properties);
    }

    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return SMCRegistrateBlocks.MAIN_COOK_BLOCK_ENTITY2.create(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return MainCookBlockEntity2::tick;
    }
}
