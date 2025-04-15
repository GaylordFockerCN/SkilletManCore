package com.p1nero.smc.block.entity;

import com.p1nero.smc.block.SMCBlockEntities;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.UUID;

public class MainCookBlockEntity extends BlockEntity {
    private UUID ownerUUID = UUID.randomUUID();

    public MainCookBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SMCBlockEntities.MAIN_COOK_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        if(t instanceof MainCookBlockEntity mainCookBlockEntity) {
            Player player = mainCookBlockEntity.getOwner();
            if(player != null && player.isAlive()) {
                if(mainCookBlockEntity.checkWorking()) {

                }
            }
        }
    }

    public void setOwnerUUID(ServerPlayer serverPlayer) {
        this.ownerUUID = serverPlayer.getUUID();
    }

    @Nullable
    public Player getOwner(){
        return level == null ? null : level.getPlayerByUUID(ownerUUID);
    }

    /**
     * 判断是否开始营业
     */
    public boolean checkWorking(){
        if(this.level == null) {
            return false;
        }
        System.out.println("up 1 " + this.level.getBlockState(this.getBlockPos().above()));
        System.out.println("up 2 " + this.level.getBlockState(this.getBlockPos().above(2)));
        return this.level.getBlockState(this.getBlockPos().above()).is(ModBlocks.STOVE.get()) && this.level.getBlockState(this.getBlockPos().above(2)).getBlock().asItem() instanceof CuisineSkilletItem;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putUUID("ownerUUID", ownerUUID);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        ownerUUID = compoundTag.getUUID("ownerUUID");
    }
}
