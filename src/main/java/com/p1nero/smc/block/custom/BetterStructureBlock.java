package com.p1nero.smc.block.custom;

import com.p1nero.smc.SMCConfig;
import com.p1nero.smc.block.entity.BetterStructureBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BetterStructureBlock extends StructureBlock {

    public BetterStructureBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new BetterStructureBlockEntity(pPos, pState);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack p_49816_, @Nullable BlockGetter p_49817_, List<Component> components, @NotNull TooltipFlag p_49819_) {
        components.add(Component.literal("（感觉...不如Mixin）"));
        super.appendHoverText(p_49816_, p_49817_, components, p_49819_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return ((level1, blockPos, blockState, t) -> {
            if(level1 instanceof ServerLevel serverLevel && SMCConfig.ENABLE_BETTER_STRUCTURE_BLOCK_LOAD.get()){
                if(t instanceof BetterStructureBlockEntity blockEntity){
                    blockEntity.loadStructure(serverLevel);
                }
            }
        });
    }

}
