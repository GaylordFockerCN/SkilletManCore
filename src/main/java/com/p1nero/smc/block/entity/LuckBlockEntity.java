package com.p1nero.smc.block.entity;

import com.p1nero.smc.block.custom.INpcDialogueBlock;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class LuckBlockEntity extends BlockEntity implements INpcDialogueBlock {

    private int leftItemCount;
    private List<ItemStack> common;
    private List<ItemStack> purple;
    private List<ItemStack> gold;

    public LuckBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }

    public void updatePool(ServerPlayer serverPlayer) {
        int stage = SMCCapabilityProvider.getSMCPlayer(serverPlayer).getStage();
        //根据不同进度来更新奖池

    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        leftItemCount = compoundTag.getInt("leftItemCount");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("leftItemCount", leftItemCount);
    }

    public void setLeftItemCount(int count) {
        this.leftItemCount = count;
    }

    /**
     * 抽卡核心
     * TODO 出金提示并且加发光
     */
    public ItemStack getItemStack(ServerLevel serverLevel){
        return ItemStack.EMPTY;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        if (t instanceof LuckBlockEntity luckBlockEntity) {
            if(luckBlockEntity.leftItemCount > 0) {
                if(level instanceof ServerLevel serverLevel) {
                    ItemUtil.addItemEntity(serverLevel, pos, luckBlockEntity.getItemStack(serverLevel));

                }

            }
        }
    }

}
