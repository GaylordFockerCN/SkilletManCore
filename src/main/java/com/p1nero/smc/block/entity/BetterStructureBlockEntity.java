package com.p1nero.smc.block.entity;

import com.p1nero.smc.DOTEConfig;
import com.p1nero.smc.block.SMCBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BetterStructureBlockEntity extends StructureBlockEntity {
    private static boolean IS_LOADING;

    public BetterStructureBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(pPos, pBlockState);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return SMCBlockEntities.BETTER_STRUCTURE_BLOCK_ENTITY.get();
    }

    /**
     * ticker 里调用，记得自毁 {@link com.p1nero.smc.block.custom.BetterStructureBlock#getTicker(Level, BlockState, BlockEntityType)}
     */
    @Override
    public boolean loadStructure(@NotNull ServerLevel level, boolean p_59849_, @NotNull StructureTemplate template) {
        if(DOTEConfig.ENABLE_BETTER_STRUCTURE_BLOCK_LOAD.get()){
            if(!IS_LOADING){
                IS_LOADING = true;
                if(super.loadStructure(level, p_59849_, template)){
                    level.destroyBlock(this.getBlockPos(), false);
                    //清理掉落物
                    List<Integer> toClear = new ArrayList<>();
                    level.getEntities().getAll().forEach((entity -> {
                        if(entity instanceof ItemEntity){
                            toClear.add(entity.getId());
                        }
                    }));
                    for(Integer entityId : toClear){
                        Entity entity = level.getEntity(entityId);
                        if(entity != null){
                            entity.discard();
                        }
                    }
                    IS_LOADING = false;
                    return true;
                }
                IS_LOADING = false;
            }
            return false;
        }
        return super.loadStructure(level, p_59849_, template);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        int i = nbt.getInt("posX");
        int j = nbt.getInt("posY");
        int k = nbt.getInt("posZ");
        setStructurePos(new BlockPos(i, j, k));
        int l = Math.max(nbt.getInt("sizeX"), 0);
        int i1 = Math.max(nbt.getInt("sizeY"), 0);
        int j1 = Math.max(nbt.getInt("sizeZ"), 0);
        setStructureSize(new BlockPos(l, i1, j1));
        this.updateBlockState();

        //客户端保险
        if(this.level != null && DOTEConfig.ENABLE_BETTER_STRUCTURE_BLOCK_LOAD.get()){
            if(this.level.isClientSide){
                HandleStructureBlockLoad.load(this);
            }
        }
    }

    private void updateBlockState() {
        if (this.level != null) {
            BlockPos blockpos = this.getBlockPos();
            BlockState blockstate = this.level.getBlockState(blockpos);
            if (blockstate.is(Blocks.STRUCTURE_BLOCK)) {
                this.level.setBlock(blockpos, blockstate.setValue(StructureBlock.MODE, this.getMode()), 2);
            }

        }
    }

}
