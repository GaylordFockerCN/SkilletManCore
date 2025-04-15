package com.p1nero.smc.entity.custom.boss.goldenflame;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * 神王吸人黑洞
 */
public class BlackHoleEntity extends Entity {
    public BlackHoleEntity(EntityType<?> type, Level level) {
        super(type, level);
        setNoGravity(true);
        noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();
        double range = 10 + tickCount * 0.2; // 有效范围
        Player player = level().getNearestPlayer(this, range);
        if(player != null){
            double dis = player.position().subtract(this.position()).length();
            Vec3 dir = this.position().subtract(player.position()).normalize().scale(0.04);//吸力
            if(dis < range){
                player.push(dir.x, dir.y, dir.z);
            }
        }
        if(tickCount > 100){
            discard();
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {

    }
}
