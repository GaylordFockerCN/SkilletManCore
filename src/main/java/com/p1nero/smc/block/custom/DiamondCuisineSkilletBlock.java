package com.p1nero.smc.block.custom;

import com.p1nero.smc.registrate.SMCRegistrateBlocks;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlock;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiamondCuisineSkilletBlock extends CuisineSkilletBlock {
    public DiamondCuisineSkilletBlock(Properties properties) {
        super(properties);
    }

    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return SMCRegistrateBlocks.DIAMOND_BE_SKILLET.create(pos, state);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof CuisineSkilletBlockEntity be) {
            be.cookingData.setSpeed(0.5F);
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return type == SMCRegistrateBlocks.DIAMOND_BE_SKILLET.get() ? (BlockEntityTicker) Wrappers.cast(getTicker()) : null;
    }
    private static BlockEntityTicker<CuisineSkilletBlockEntity> getTicker() {
        return (level, pos, state, be) -> {
            be.tick(level, pos, state);
        };
    }
}
