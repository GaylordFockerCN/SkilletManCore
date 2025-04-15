package com.p1nero.smc.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Iterator;

/**
 * copy from ParrotWanderGoal
 */
public class BirdFlyGoal extends WaterAvoidingRandomFlyingGoal {
    public BirdFlyGoal(PathfinderMob p_186224_, double p_186225_) {
        super(p_186224_, p_186225_);
    }

    @Nullable
    protected Vec3 getPosition() {
        Vec3 vec3 = null;
        if (this.mob.isInWater()) {
            vec3 = LandRandomPos.getPos(this.mob, 15, 15);
        }

        if (this.mob.getRandom().nextFloat() >= this.probability) {
            vec3 = this.getTreePos();
        }

        return vec3 == null ? super.getPosition() : vec3;
    }

    @Nullable
    private Vec3 getTreePos() {
        BlockPos blockpos = this.mob.blockPosition();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
        Iterator<BlockPos> var4 = BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 13.0), Mth.floor(this.mob.getY() - 16.0), Mth.floor(this.mob.getZ() - 13.0), Mth.floor(this.mob.getX() + 13.0), Mth.floor(this.mob.getY() + 16.0), Mth.floor(this.mob.getZ() + 13.0)).iterator();

        BlockPos blockpos1;
        boolean flag;
        do {
            do {
                if (!var4.hasNext()) {
                    return null;
                }

                blockpos1 = var4.next();
            } while(blockpos.equals(blockpos1));

            BlockState blockstate = this.mob.level().getBlockState(blockpos$mutableblockpos1.setWithOffset(blockpos1, Direction.DOWN));
            flag = blockstate.getBlock() instanceof LeavesBlock || blockstate.is(BlockTags.LOGS);
        } while(!flag || !this.mob.level().isEmptyBlock(blockpos1) || !this.mob.level().isEmptyBlock(blockpos$mutableblockpos.setWithOffset(blockpos1, Direction.UP)));

        return Vec3.atBottomCenterOf(blockpos1);
    }
}