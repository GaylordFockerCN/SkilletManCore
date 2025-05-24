package com.p1nero.smc.block.entity;

import com.p1nero.smc.block.SMCBlockEntities;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.start_npc.StartNPC;
import com.p1nero.smc.registrate.SMCRegistrateBlocks;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainCookBlockEntity2 extends BlockEntity {
    @Nullable
    private StartNPC startNPC;
    private boolean isWorking;
    public static final int WORKING_RADIUS = 8;
    private final List<Customer> customers = new ArrayList<>();
    public static final List<VillagerProfession> PROFESSION_LIST = ForgeRegistries.VILLAGER_PROFESSIONS.getValues().stream().toList();
    public boolean firstCustomerSummoned = false;//摆锅马上就有客户
    public static final int SEARCH_DIS = 4;

    public MainCookBlockEntity2(BlockEntityType<MainCookBlockEntity2> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }

    public @Nullable StartNPC getStartNPC() {
        return startNPC;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        if(level.isClientSide) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel) level;
        if (t instanceof MainCookBlockEntity2 mainCookBlockEntity) {
           //TODO 生成NPC2和小僵
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putBoolean("isWorking", isWorking);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        isWorking = compoundTag.getBoolean("isWorking");

    }

    public BlockPos getSkilletPos(){
        return this.getBlockPos().above(2);
    }

    public BlockState getSkilletBlock() {
        if(level == null){
            return null;
        }
        BlockState blockState = level.getBlockState(this.getSkilletPos());
        return blockState.getBlock().asItem() instanceof CuisineSkilletItem ? blockState : null;
    }

    public boolean hasSkillet() {
        return this.getSkilletBlock() != null;
    }

    public int getDayTime(){
        if(level == null){
            return 0;
        }
        return (int) (level.getDayTime() / 24000);
    }

}
