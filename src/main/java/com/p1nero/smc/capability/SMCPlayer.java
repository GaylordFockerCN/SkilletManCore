package com.p1nero.smc.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * 记录飞行和技能使用的状态，被坑了，这玩意儿也分服务端和客户端...
 * 懒得换成DataKey了将就一下吧
 */
public class SMCPlayer {
    private int level;
    private int stage;

    private int moneyCount;

    public int getMoneyCount() {
        return moneyCount;
    }

    public void setMoneyCount(int moneyCount) {
        this.moneyCount = moneyCount;
        //TODO 同步到客户端
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return stage;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    @Nullable
    private PathfinderMob currentTalkingEntity;

    public void setCurrentTalkingEntity(@Nullable PathfinderMob currentTalkingEntity) {
        this.currentTalkingEntity = currentTalkingEntity;
    }

    public @Nullable PathfinderMob getCurrentTalkingEntity() {
        return currentTalkingEntity;
    }

    private CompoundTag data = new CompoundTag();
    public boolean getBoolean(String key){
        return data.getBoolean(key);
    }
    public double getDouble(String key){
        return data.getDouble(key);
    }
    public String getString(String key){
        return data.getString(key);
    }
    public void putBoolean(String key, boolean value){
        data.putBoolean(key, value);
    }
    public void putDouble(String key, double v){
        data.putDouble(key, v);
    }
    public void putString(String k, String v){
        data.putString(k, v);
    }
    public void setData(Consumer<CompoundTag> consumer) {
        consumer.accept(data);
    }

    public CompoundTag getData() {
        return data;
    }

    public void saveNBTData(CompoundTag tag){
        tag.putInt("tradeLevel", level);
        tag.putInt("tradeStage", stage);
        tag.putInt("money_count", moneyCount);
        tag.put("customDataManager", data);
    }

    public void loadNBTData(CompoundTag tag){
        level = tag.getInt("tradeLevel");
        stage = tag.getInt("tradeStage");
        moneyCount = tag.getInt("money_count");
        data = tag.getCompound("customDataManager");
    }

    public void copyFrom(SMCPlayer old){
        data = old.data;
        this.level = old.level;
        this.stage = old.stage;
    }

    public void syncToClient(ServerPlayer serverPlayer) {

    }

    public void tick(Player player){
        if(this.currentTalkingEntity != null && this.currentTalkingEntity.isAlive()){
            this.currentTalkingEntity.getLookControl().setLookAt(player);
            this.currentTalkingEntity.getNavigation().stop();
        }
    }

}
